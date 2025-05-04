package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.model.entity.SearchFormAttribute;
import com.khokhlov.tendermonitoring.model.entity.SearchResult;
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

    @GetMapping()
    public String homePage(Model model) {
        SearchFormAttribute searchFormAttribute = new SearchFormAttribute();
        searchFormAttribute.setStages(List.of("Подача заявок", "Работа комиссии"));
        searchFormAttribute.setTypes(List.of("44-ФЗ", "223-ФЗ"));

        model.addAttribute("searchFormAttribute", searchFormAttribute);
        model.addAttribute("searchResult", new SearchResult());
        return "home";
    }
}
