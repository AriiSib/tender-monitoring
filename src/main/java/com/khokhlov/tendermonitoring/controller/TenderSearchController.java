package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.model.dto.TenderDTO;
import com.khokhlov.tendermonitoring.model.entity.SearchFormAttribute;
import com.khokhlov.tendermonitoring.service.TenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class TenderSearchController {

    private final TenderService tenderService;

    @PostMapping("/search")
    public String processSearch(@ModelAttribute("searchForm") SearchFormAttribute attribute,
                                Model model) {
        List<TenderDTO> result = tenderService.searchTenders(attribute);
        model.addAttribute("searchForm", attribute);
        model.addAttribute("tenders", result);
        return "home";
    }

}
