package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.model.dto.TenderDTO;
import com.khokhlov.tendermonitoring.model.entity.SearchFormAttribute;
import com.khokhlov.tendermonitoring.repository.TenderRepository;
import com.khokhlov.tendermonitoring.util.TenderParser;
import com.khokhlov.tendermonitoring.util.UrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TenderService {

    private final TenderRepository tenderRepository;

    @Transactional()
    public List<TenderDTO> searchTenders(SearchFormAttribute attribute) {
        String url = UrlBuilder.buildSearchUrl(attribute);
        return TenderParser.parseTenders(url);
    }

}
