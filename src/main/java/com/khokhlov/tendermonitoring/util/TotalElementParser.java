package com.khokhlov.tendermonitoring.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TotalElementParser {
    public static int parseTotalElement(Document doc) {
        Element totalElement = doc.selectFirst(".search-results__total");

        if (totalElement != null) {
            String text = totalElement.text();
            String numberOnly = text.replaceAll("[^\\d]", "");
            return Integer.parseInt(numberOnly);
        }

        return 0;
    }
}
