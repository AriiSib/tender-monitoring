package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.repository.TrackedKeywordRepository;
import com.khokhlov.tendermonitoring.repository.TrackedTenderRepository;
import com.khokhlov.tendermonitoring.service.TrackedKeywordService;
import com.khokhlov.tendermonitoring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final TrackedKeywordService trackedKeywordService;
    private final TrackedKeywordRepository keywordRepository;
    private final TrackedTenderRepository trackedTenderRepository;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("userCount", userService.count());
        model.addAttribute("keywordCount", keywordRepository.count());
        model.addAttribute("tenderCount", trackedTenderRepository.count());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/toggleRole")
    public String toggleRole(@PathVariable Long id) {
        userService.toggleRole(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/keywords")
    public String keywords(Model model) {
        model.addAttribute("keywords", keywordRepository.findAll());
        return "admin/keywords";
    }

    @PostMapping("/keywords/{id}/interval")
    public String updateInterval(@PathVariable Long id,
                                 @RequestParam("minutes") int minutes) {
        trackedKeywordService.updateInterval(id, minutes);
        return "redirect:/admin/keywords";
    }

    @PostMapping("/keywords/{id}/delete")
    public String deleteKeyword(@PathVariable Long id) {
        keywordRepository.deleteById(id);
        return "redirect:/admin/keywords";
    }

    @GetMapping("/tenders")
    public String tenders(Model model) {
        model.addAttribute("tenders", trackedTenderRepository.findAll());
        return "admin/tenders";
    }

    @PostMapping("/tenders/{id}/delete")
    public String deleteTender(@PathVariable Long id) {
        trackedTenderRepository.deleteById(id);
        return "redirect:/admin/tenders";
    }
}
