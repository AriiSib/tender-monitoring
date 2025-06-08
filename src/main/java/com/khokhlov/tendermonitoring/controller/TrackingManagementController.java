package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.model.dto.TrackedKeywordViewDTO;
import com.khokhlov.tendermonitoring.model.dto.UserDTO;
import com.khokhlov.tendermonitoring.service.TrackingViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tracking")
@RequiredArgsConstructor
public class TrackingManagementController {

    private final TrackingViewService trackingViewService;

    @GetMapping
    public String userSubscriptions(@SessionAttribute("user") UserDTO user, Model model) {
        List<TrackedKeywordViewDTO> subscriptions = trackingViewService.getAllForUser(user);
        model.addAttribute("subscriptions", subscriptions);
        return "tracking/subscriptions";
    }

    @PostMapping("/{id}/pause")
    public String pause(@PathVariable Long id) {
        trackingViewService.pause(id);
        return "redirect:/tracking";
    }

    @PostMapping("/{id}/resume")
    public String resume(@PathVariable Long id) {
        trackingViewService.resume(id);
        return "redirect:/tracking";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @RequestParam int interval) {
        trackingViewService.updateInterval(id, interval);
        return "redirect:/tracking";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        trackingViewService.delete(id);
        return "redirect:/tracking";
    }
}
