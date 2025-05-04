package com.khokhlov.tendermonitoring.util;

import com.khokhlov.tendermonitoring.model.dto.TenderDTO;
import com.khokhlov.tendermonitoring.model.entity.SearchResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TenderParser {

    public static SearchResult parseTenders(String url) {
        SearchResult searchResult = new SearchResult();
        List<TenderDTO> tenders = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36")
                    .timeout(10000)
                    .get();

            searchResult.setTotalCount(TotalElementParser.parseTotalElement(doc));

            Elements entries = doc.select(".search-registry-entry-block");

            for (Element entry : entries) {
                String title = entry.select(".registry-entry__header-top__title").text();
                String link = entry.select("a[href*='regNumber']").attr("href");
                String fullLink = "https://zakupki.gov.ru" + link;
                String regNumber = entry.select("div.registry-entry__header-mid__number a").text().replaceAll("[^0-9]", "");
                String stage = entry.select("div.registry-entry__header-mid__title.text-normal").text();

                LocalDate deadline = null;
                try {
                    deadline = DateParser.parseDate(entry.select(".data-block__title:contains(Окончание подачи заявок)")
                            .first()
                            .nextElementSibling()
                            .text());
                } catch (Exception e) {
                }

                LocalDate publicationDate = null;
                try {
                    publicationDate = DateParser.parseDate(entry.select(".data-block__title:contains(Размещено)")
                            .first()
                            .nextElementSibling()
                            .text());
                } catch (Exception e) {
                }

                LocalDate updatedDate = null;
                try {
                    updatedDate = DateParser.parseDate(entry.select(".data-block__title:contains(Обновлено)")
                            .first()
                            .nextElementSibling()
                            .text());
                } catch (Exception e) {
                }

                BigDecimal price = null;
                try {
                    price = PriceParser.parsePrice(entry.select(".price-block__value").text());
                } catch (Exception e) {

                }
                String customer = null;
                try {
                    customer = entry.select(".registry-entry__body-href a").text();
                } catch (Exception e) {

                }
                String purchaseObject = null;
                try {
                    purchaseObject = entry.select(".registry-entry__body-value").text();
                } catch (Exception e) {

                }

                TenderDTO tender = new TenderDTO(
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

                tenders.add(tender);
            }

        } catch (Exception e) {
            System.err.println("Error parsing search page: " + e.getMessage());
        }

        searchResult.setTenders(tenders);

        return searchResult;
    }
}
