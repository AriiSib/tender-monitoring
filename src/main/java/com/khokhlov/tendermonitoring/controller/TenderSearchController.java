package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.model.dto.UserDTO;
import com.khokhlov.tendermonitoring.model.entity.SearchFormAttribute;
import com.khokhlov.tendermonitoring.model.entity.SearchResult;
import com.khokhlov.tendermonitoring.repository.TrackedKeywordRepository;
import com.khokhlov.tendermonitoring.service.TenderService;
import com.khokhlov.tendermonitoring.service.TrackingViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class TenderSearchController {

    private final TenderService tenderService;
    private final TrackedKeywordRepository trackedKeywordRepository;

    @PostMapping("/search")
    public String processSearch(@SessionAttribute("user") UserDTO user,
                                @ModelAttribute("searchFormAttribute") SearchFormAttribute attribute,
                                Model model) {
        SearchResult result = tenderService.searchTenders(attribute);
        model.addAttribute("searchFormAttribute", attribute);
        model.addAttribute("searchResult", result);
        if (attribute.getKeyword() != null &&
                trackedKeywordRepository.findAllByUserId(user.id()).stream()
                        .anyMatch(t -> Objects.equals(t.getKeyword(), attribute.getKeyword()))
        ) {
            model.addAttribute("subscribe", true);
        } else {
            model.addAttribute("subscribe", false);
        }

        return "home";
    }
}
