package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.model.dto.TrackedTenderViewDTO;
import com.khokhlov.tendermonitoring.model.dto.UserDTO;
import com.khokhlov.tendermonitoring.service.TrackingViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/new-tenders")
public class NewTendersController {

    private final TrackingViewService trackingViewService;

    @GetMapping
    public String showNewTenders(@SessionAttribute("user") UserDTO userDTO, Model model) {
        List<TrackedTenderViewDTO> tenders = trackingViewService.getNewTendersForUser(userDTO);
        model.addAttribute("tenders", tenders);
        return "tenders/tenders";
    }
}
