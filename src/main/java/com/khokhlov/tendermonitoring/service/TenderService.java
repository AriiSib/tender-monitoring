package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.model.entity.Tender;
import com.khokhlov.tendermonitoring.repository.TenderRepository;
import com.khokhlov.tendermonitoring.util.DateParser;
import com.khokhlov.tendermonitoring.util.PriceParser;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TenderService {

    private final TenderRepository tenderRepository;

    private static final String SEARCH_URL = "https://zakupki.gov.ru/epz/order/extendedsearch/results.html?searchString=%D1%83%D0%B1%D0%BE%D1%80%D0%BA%D0%B0+%D0%BF%D0%BE%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D0%B9&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&pageNumber=1&sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false&sortBy=UPDATE_DATE&fz44=on&fz223=on&af=on&currencyIdGeneral=-1&gws=%D0%92%D1%8B%D0%B1%D0%B5%D1%80%D0%B8%D1%82%D0%B5+%D1%82%D0%B8%D0%BF+%D0%B7%D0%B0%D0%BA%D1%83%D0%BF%D0%BA%D0%B8";

    @Transactional()
    public List<Tender> fetchAndSave() {
        List<Tender> tenders = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(SEARCH_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36")
                    .timeout(10000)
                    .get();

            Elements entries = doc.select(".search-registry-entry-block");

            for (Element entry : entries) {
                String title = entry.select(".registry-entry__header-top__title").text();
                String link = entry.select("a[href*='regNumber']").attr("href");
                String fullLink = "https://zakupki.gov.ru" + link;
                String regNumber = entry.select("div.registry-entry__header-mid__number a").text().replaceAll("[^0-9]", "");
                String stage = entry.select("div.registry-entry__header-mid__title.text-normal").text();


                LocalDate deadline = DateParser.parseDate(entry.select(".data-block__title:contains(Окончание подачи заявок)")
                        .first()
                        .nextElementSibling()
                        .text());

                LocalDate publicationDate = DateParser.parseDate(entry.select(".data-block__title:contains(Размещено)")
                        .first()
                        .nextElementSibling()
                        .text());

                LocalDate updatedDate = DateParser.parseDate(entry.select(".data-block__title:contains(Обновлено)")
                        .first()
                        .nextElementSibling()
                        .text());

                BigDecimal price = PriceParser.parsePrice(entry.select(".price-block__value").text());

                String customer = entry.select(".registry-entry__body-href a").text();

                String purchaseObject = entry.select(".registry-entry__body-value").text();

                Tender tender = new Tender(
                        null,
                        title,
                        fullLink,
                        purchaseObject,
                        stage,
                        purchaseObject,
                        price,
                        publicationDate,
                        updatedDate,
                        deadline,
                        regNumber,
                        customer
                );

                tenders.add(tenderRepository.save(tender));
            }

        } catch (Exception e) {
            System.err.println("Error parsing search page: " + e.getMessage());
        }

        return tenders;
    }

    public List<Tender> getAll() {
        return tenderRepository.findAll();
    }
}
