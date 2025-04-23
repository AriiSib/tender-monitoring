package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.model.dto.UserCreateDTO;
import com.khokhlov.tendermonitoring.model.dto.UserDTO;
import com.khokhlov.tendermonitoring.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/sign-in";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {

        // TODO: validation
        UserDTO user = userService.login(new UserDTO(username, password));
        session.setAttribute("username", user.username());

        return "redirect:/index";
    }

    @GetMapping("/registration")
    public String registrationPage() {
        return "auth/sign-up";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute UserCreateDTO user,
                               Model model) {

        userService.registration(user);

        return "redirect:/auth/login";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/auth/login";
    }
}
