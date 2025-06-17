package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.error.exception.auth.InvalidUsernameException;
import com.khokhlov.tendermonitoring.model.dto.UserCreateDTO;
import com.khokhlov.tendermonitoring.model.entity.TrackedTender;
import com.khokhlov.tendermonitoring.model.entity.User;
import com.khokhlov.tendermonitoring.repository.TrackedKeywordRepository;
import com.khokhlov.tendermonitoring.repository.TrackedTenderRepository;
import com.khokhlov.tendermonitoring.repository.UserRepository;
import com.khokhlov.tendermonitoring.service.TrackedKeywordService;
import com.khokhlov.tendermonitoring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final TrackedKeywordService trackedKeywordService;
    private final TrackedKeywordRepository keywordRepository;
    private final TrackedTenderRepository trackedTenderRepository;
    private final UserRepository userRepository;

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        return "admin/new-users";
    }

    @PostMapping("/registration")
    public String registration(@Validated @ModelAttribute("user") UserCreateDTO user,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/new-users";

        try {
            userService.registration(user);
        } catch (InvalidUsernameException e) {
            bindingResult.rejectValue("username", "error.user", e.getMessage());
            return "admin/new-users";
        }
        return "redirect:/admin/users";
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("userCount", userService.count());
        model.addAttribute("keywordCount", keywordRepository.count());
        model.addAttribute("tenderCount", getUniqueTenders(trackedTenderRepository.findAll()).size());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userRepository.getUserById(id).orElseThrow();
        if (user.getUsername().equalsIgnoreCase("admin")) {
            redirectAttributes.addFlashAttribute("error", "Нельзя удалить администратора.");
            return "redirect:/admin/users";
        }
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
        List<TrackedTender> tenders = trackedTenderRepository.findAll();
        List<TrackedTender> uniqueTenders = getUniqueTenders(tenders);
        model.addAttribute("tenders", uniqueTenders);
        return "admin/tenders";
    }

    @NotNull
    private List<TrackedTender> getUniqueTenders(List<TrackedTender> tenders) {
        return tenders.stream()
                .collect(Collectors.toMap(
                        TrackedTender::getPurchaseCode,
                        tender -> tender,
                        (first, second) -> first
                ))
                .values()
                .stream()
                .toList();
    }

    @PostMapping("/tenders/{id}/delete")
    public String deleteTender(@PathVariable Long id) {
        trackedTenderRepository.deleteById(id);
        return "redirect:/admin/tenders";
    }
}
