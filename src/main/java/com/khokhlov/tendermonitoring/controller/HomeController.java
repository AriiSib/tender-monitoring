package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.model.entity.SearchFormAttribute;
import com.khokhlov.tendermonitoring.service.TenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final TenderService tenderService;

    @GetMapping()
    public String homePage(Model model) {
        SearchFormAttribute searchForm = new SearchFormAttribute();
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("tenders", List.of());
        return "home";
    }
}
