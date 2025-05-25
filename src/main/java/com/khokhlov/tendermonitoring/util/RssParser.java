package com.khokhlov.tendermonitoring.util;

import com.khokhlov.tendermonitoring.model.dto.RssItemDTO;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.khokhlov.tendermonitoring.consts.Consts.ZAKUPKI_GOV_URL;

public class RssParser {

    @SneakyThrows
    public static List<RssItemDTO> parse(String rssUrl) {
        List<RssItemDTO> rssItems = new ArrayList<>();

        try (XmlReader reader = new XmlReader(new URL(rssUrl))) {
            SyndFeed feed = new SyndFeedInput().build(reader);

            for (SyndEntry entry : feed.getEntries()) {
                String title = entry.getTitle().replaceAll(" №\\d+", "");
                String link = ZAKUPKI_GOV_URL + entry.getLink();
                String purchaseCode = link.replaceAll(".*regNumber=(\\d+).*", "$1");

                String description = entry.getDescription().getValue();

                ZonedDateTime publishedDate = entry.getPublishedDate().toInstant().atZone(ZoneId.of("GMT"));

                String author = entry.getAuthor();

                Document rssDescription = Jsoup.parse(description);

                String procurementLaws = extractData(rssDescription, "Размещение выполняется по:");
                String stage = extractData(rssDescription, "Этап размещения:");
                String purchaseObject = extractData(rssDescription, "Наименование объекта закупки:");
                BigDecimal price = PriceParser.parsePrice(extractData(rssDescription, "Начальная цена контракта:"));
                String currency = extractData(rssDescription, "Валюта:");
                LocalDate postedDate = DateFormatter.parseDate(extractData(rssDescription, "Размещено:"));
                LocalDate updatedDate = DateFormatter.parseDate(extractData(rssDescription, "Обновлено:"));
                String identificationCode = extractData(rssDescription, "Идентификационный код закупки (ИКЗ):");

                RssItemDTO item = new RssItemDTO(
                        title,
                        purchaseCode,
                        link,
                        procurementLaws,
                        stage,
                        purchaseObject,
                        price,
                        currency,
                        publishedDate,
                        postedDate,
                        updatedDate,
                        identificationCode,
                        author);

                rssItems.add(item);
            }

        } catch (Exception e) {
            System.err.println("RSS reading error: " + e.getMessage());
        }

        return rssItems;
    }

    private static String extractData(Document doc, String strongLabel) {
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
}
