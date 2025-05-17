package com.khokhlov.tendermonitoring.util;

import com.khokhlov.tendermonitoring.model.dto.TrackingAttributeDTO;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.khokhlov.tendermonitoring.consts.Consts.ZAKUPKI_GOV_RSS_URL;

public class RssUrlBuilder {

    public static String build(TrackingAttributeDTO attributes) {
        StringBuilder url = new StringBuilder(ZAKUPKI_GOV_RSS_URL);

        if (attributes.keyword() != null && !attributes.keyword().isBlank()) {
            url.append("searchString=").append(URLEncoder.encode(attributes.keyword(), StandardCharsets.UTF_8)).append("&");
        }

        if (attributes.types() != null) {
            if (attributes.types().contains("44-ФЗ")) url.append("fz44=on&");
            if (attributes.types().contains("223-ФЗ")) url.append("fz223=on&");
            if (attributes.types().stream().anyMatch(t -> t.contains("615"))) url.append("ppRf615=on&");
        }

        if (attributes.stages() != null) {
            if (attributes.stages().contains("Подача заявок")) url.append("af=on&");
            if (attributes.stages().contains("Работа комиссии")) url.append("ca=on&");
            if (attributes.stages().contains("Закупка завершена")) url.append("pc=on&");
            if (attributes.stages().contains("Закупка отменена")) url.append("pa=on&");
        }

        url.append("morphology=on&");
        url.append("search-filter=Дате+размещения&");
        url.append("pageNumber=1&");
        url.append("sortDirection=false&");
        url.append("showLotsInfoHidden=false&");
        url.append("sortBy=PUBLISH_DATE&");

        if (attributes.pageSize() != null) {
            url.append("recordsPerPage=_").append(attributes.pageSize()).append("&");
        } else {
            url.append("recordsPerPage=_10&");
        }

        url.append("currencyIdGeneral=-1&");
        url.append("gws=Выберите+тип+закупки");

        return url.toString();
    }
}
