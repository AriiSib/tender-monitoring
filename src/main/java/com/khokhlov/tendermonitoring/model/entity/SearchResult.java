package com.khokhlov.tendermonitoring.model.entity;

import com.khokhlov.tendermonitoring.model.dto.TenderDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    private List<TenderDTO> tenders;
    private int totalCount;
    private int totalPages;
    private int currentPage;
}
