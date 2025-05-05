package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.model.entity.SearchFormAttribute;
import com.khokhlov.tendermonitoring.model.entity.SearchResult;
import com.khokhlov.tendermonitoring.util.TenderParser;
import com.khokhlov.tendermonitoring.util.UrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TenderService {

    @Transactional()
    public SearchResult searchTenders(SearchFormAttribute attribute) {
        String url = UrlBuilder.buildSearchUrl(attribute);
        SearchResult result = TenderParser.parseTenders(url);
        result.setTotalPages(calculatePageCount(attribute, result));
        return result;
    }

    private int calculatePageCount(SearchFormAttribute attribute, SearchResult result) {
        int totalPage = ((int) Math.ceil((double) result.getTotalCount() / attribute.getPageSize()));
        if(totalPage > 100)
            totalPage = 100;
        return totalPage;
    }

}
