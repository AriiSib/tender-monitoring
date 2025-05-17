package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.model.dto.TrackingAttributeDTO;
import com.khokhlov.tendermonitoring.model.dto.UserDTO;
import com.khokhlov.tendermonitoring.model.entity.SearchFormAttribute;
import com.khokhlov.tendermonitoring.model.entity.SearchResult;
import com.khokhlov.tendermonitoring.model.entity.TrackedKeyword;
import com.khokhlov.tendermonitoring.service.DynamicMonitoringService;
import com.khokhlov.tendermonitoring.service.RssMonitorService;
import com.khokhlov.tendermonitoring.service.TenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class TrackingController {

    private final RssMonitorService rssMonitorService;
    private final DynamicMonitoringService dynamicMonitoringService;
    private final TenderService tenderService;

    @PostMapping("/tracking/subscribe")
    public String subscribe(@ModelAttribute("trackingAttributeDTO") TrackingAttributeDTO attribute,
                            @ModelAttribute("searchFormAttribute") SearchFormAttribute searchFormAttribute,
                            @SessionAttribute("user") UserDTO user,
                            Model model) {
        TrackedKeyword keyword = rssMonitorService.startMonitoring(user, attribute);

        if (keyword != null) {
            dynamicMonitoringService.schedule(keyword);
            model.addAttribute("subscribe", true);
        } else {
            model.addAttribute("subscribe", false);
        }

        SearchResult result = tenderService.searchTenders(searchFormAttribute);

        model.addAttribute("searchFormAttribute", searchFormAttribute);
        model.addAttribute("searchResult", result);
        model.addAttribute("trackingKeyword", attribute.keyword());
        return "home";
    }
}
