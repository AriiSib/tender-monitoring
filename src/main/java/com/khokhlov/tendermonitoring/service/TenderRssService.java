package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.model.entity.Tender;
import com.khokhlov.tendermonitoring.repository.TenderRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TenderRssService {

    private final TenderRepository tenderRepository;

    private static final URL RSS_URL;
    private static final String SELENIUM_URL = "https://zakupki.gov.ru/epz/order/extendedsearch/results.html?searchString=%D1%83%D0%B1%D0%BE%D1%80%D0%BA%D0%B0+%D0%BF%D0%BE%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D0%B9&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&pageNumber=1&sortDirection=false&recordsPerPage=_100&showLotsInfoHidden=false&sortBy=UPDATE_DATE&fz44=on&fz223=on&af=on&currencyIdGeneral=-1&gws=%D0%92%D1%8B%D0%B1%D0%B5%D1%80%D0%B8%D1%82%D0%B5+%D1%82%D0%B8%D0%BF+%D0%B7%D0%B0%D0%BA%D1%83%D0%BF%D0%BA%D0%B8";

    static {
        try {
            RSS_URL = new URI("https://zakupki.gov.ru/tinyurl/8ff3829606587dd12a89611b1b3c52673d5cd4d960c73dadeec18443ba5390c0").toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Transactional()
    public List<Tender> fetchAndSaveFromRSS() {
        List<Tender> tenders = new ArrayList<>();

        try (XmlReader reader = new XmlReader(RSS_URL)) {
            SyndFeed feed = new SyndFeedInput().build(reader);

            for (SyndEntry entry : feed.getEntries()) {
                String title = entry.getTitle();
                String link = "https://zakupki.gov.ru" + entry.getLink();

                String description = entry.getDescription().getValue();

                String publishedDate = entry.getPublishedDate() != null
                        ? entry.getPublishedDate().toString()
                        : "Не указано";

                String author = entry.getAuthor();

                Document doc = Jsoup.parse(description);

                String searchParams = extractData(doc, "Искомое слово (словосочетание):");
                String procurementLaws = extractData(doc, "Закупки по:");
                String stage = extractData(doc, "Этап закупки:");
                String purchaseObject = extractData(doc, "Наименование объекта закупки:");
                String price = extractData(doc, "Начальная цена контракта:");
                String currency = extractData(doc, "Валюта:");
                String updatedDate = extractData(doc, "Обновлено:");
                String purchaseCode = extractData(doc, "Идентификационный код закупки (ИКЗ):");
                String deadline = null;

                Tender tender = new Tender(
                        null,
                        title,
                        link,
                        searchParams,
                        stage,
                        purchaseObject,
                        price,
                        publishedDate,
                        updatedDate,
                        deadline,
                        purchaseCode,
                        author
                );

                tenders.add(tenderRepository.save(tender));
            }

        } catch (Exception e) {
            System.err.println("RSS reading error: " + e.getMessage());
        }

        return tenders;
    }

    private String fetchDeadline(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36")
                    .timeout(10_000)
                    .get();

            Elements titles = doc.select("div.data-block__title");
            for (Element title : titles) {
                if (title.text().contains("Окончание подачи заявок")) {
                    Element value = title.nextElementSibling();
                    return value != null ? value.text().trim() : "Не указано";
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting application submission date: " + e.getMessage());
        }

        return "Не указано";
    }



    private String extractData(Document doc, String strongLabel) {
        Element strong = doc.select("strong:matchesOwn(^\\Q" + strongLabel + "\\E\\s*$)").first();
        if (strong == null) {
            strong = doc.select("strong:matchesOwn(^\\Q" + strongLabel + "\\E)").first();
        }
        if (strong != null) {
            Node sibling = strong.nextSibling();
            while (sibling != null) {
                if (sibling instanceof TextNode) {
                    return ((TextNode) sibling).text().trim();
                } else if (sibling instanceof Element && "br".equals(((Element) sibling).tagName())) {
                    sibling = sibling.nextSibling();
                } else {
                    break;
                }
            }
        }
        return "Не указано";
    }

    public List<Tender> getAllTenders() {
        return tenderRepository.findAll();
    }

}
