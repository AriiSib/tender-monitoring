package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.model.entity.Tender;
import com.khokhlov.tendermonitoring.service.TenderRssService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rss")
@RequiredArgsConstructor
public class RssController {

    private final TenderRssService tenderService;

    @GetMapping("/search")
    public List<Tender> fetchFromRSS() {
        return tenderService.fetchAndSaveFromRSS();
    }

    @GetMapping("/all")
    public List<Tender> getAllTenders() {
        return tenderService.getAllTenders();
    }
}
