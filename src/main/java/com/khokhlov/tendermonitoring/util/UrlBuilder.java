package com.khokhlov.tendermonitoring.util;

import com.khokhlov.tendermonitoring.model.entity.SearchFormAttribute;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.khokhlov.tendermonitoring.consts.Consts.ZAKUPKI_GOV_URL;


public class UrlBuilder {

    public static String buildSearchUrl(SearchFormAttribute attribute) {
        StringBuilder url = new StringBuilder(ZAKUPKI_GOV_URL);

        if (attribute.getKeyword() != null && !attribute.getKeyword().isBlank()) {
            url.append("searchString=").append(URLEncoder.encode(attribute.getKeyword(), StandardCharsets.UTF_8)).append("&");
        }

        if (attribute.getTypes() != null) {
            if (attribute.getTypes().contains("44-ФЗ"))     url.append("fz44=on&");
            if (attribute.getTypes().contains("223-ФЗ"))    url.append("fz223=on&");
            if (attribute.getTypes().contains("615 ПП РФ")) url.append("ppRf615=on&");
        }

        if (attribute.getStages() != null) {
            if (attribute.getStages().contains("Подача заявок"))     url.append("af=on&");
            if (attribute.getStages().contains("Работа комиссии"))   url.append("ca=on&");
            if (attribute.getStages().contains("Закупка завершена")) url.append("pc=on&");
            if (attribute.getStages().contains("Закупка отменена"))  url.append("pa=on&");
        }

        url.append("morphology=on&search-filter=Дате+размещения&sortBy=PUBLISH_DATE");
        url.append("&pageNumber=").append(attribute.getCurrentPage());
        url.append("&recordsPerPage=_").append(attribute.getPageSize());

        return url.toString();
    }
}
