package com.khokhlov.tendermonitoring.util;

import com.khokhlov.tendermonitoring.model.dto.SpecifiedDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TenderCardParser {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static SpecifiedDate parseDate(String url) {
        Document doc = getDocument(url);
        ZonedDateTime postedDate = parseDateByTitle(doc, "Дата и время начала срока подачи заявок");
        ZonedDateTime expirationDate = parseDateByTitle(doc, "Дата и время окончания срока подачи заявок");
        return new SpecifiedDate(postedDate, expirationDate);
    }

    private static ZonedDateTime parseDateByTitle(Document doc, String titleToMatch) {
        try {
            Elements sections = doc.select("section.blockInfo__section");

            for (Element section : sections) {
                Element title = section.selectFirst("span.section__title");
                if (title != null && title.text().contains(titleToMatch)) {
                    Element info = section.selectFirst("span.section__info");
                    if (info != null) {
                        String rawText = info.text().trim();

                        String dateTimeStr = rawText.replaceAll("\\s*\\(.*?\\)", "").trim();
                        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

                        Matcher matcher = Pattern.compile("\\(МСК([+-]\\d+)\\)").matcher(rawText);
                        int utcOffset = 3;

                        if (matcher.find()) {
                            int mskShift = Integer.parseInt(matcher.group(1));
                            utcOffset += mskShift;
                        }

                        if (utcOffset < -12 || utcOffset > 14) {
                            throw new IllegalArgumentException("Смещение UTC выходит за допустимые границы: UTC" + utcOffset);
                        }

                        return ZonedDateTime.of(localDateTime, ZoneOffset.ofHours(utcOffset));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при парсинге даты '" + titleToMatch + "': " + e.getMessage());
        }

        return null;
    }

    private static Document getDocument(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();
        } catch (IOException e) {
            System.err.println("Error fetching document: " + e.getMessage());
            return null;
        }
    }
}
