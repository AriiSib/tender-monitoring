package com.khokhlov.tendermonitoring.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class SearchFormAttribute {
    private String keyword;
    private List<String> stages;
    private List<String> types;
    private Integer pageSize;
}
