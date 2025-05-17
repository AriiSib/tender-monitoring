package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.model.entity.SearchFormAttribute;
import com.khokhlov.tendermonitoring.model.entity.SearchResult;
import com.khokhlov.tendermonitoring.service.TenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class TenderSearchController {

    private final TenderService tenderService;

    @PostMapping("/search")
    public String processSearch(@ModelAttribute("searchFormAttribute") SearchFormAttribute attribute,
                                Model model) {
        SearchResult result = tenderService.searchTenders(attribute);
        model.addAttribute("searchFormAttribute", attribute);
        model.addAttribute("searchResult", result);
        model.addAttribute("subscribe", false);
        return "home";
    }

}
