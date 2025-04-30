package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.model.dto.UserCreateDTO;
import com.khokhlov.tendermonitoring.model.dto.UserDTO;
import com.khokhlov.tendermonitoring.model.dto.UserLoginDTO;
import com.khokhlov.tendermonitoring.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@SessionAttributes({"user"})
public class UserAuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/sign-in";
    }

    @PostMapping("/login")
    public String login(UserLoginDTO userDTO,
                        Model model) {

        // TODO: validation
        UserDTO user = userService.login(userDTO);
        model.addAttribute("user", user);

        return "redirect:/home";
    }

    @GetMapping("/registration")
    public String registrationPage() {
        return "auth/sign-up";
    }

    @PostMapping("/registration")
    public String registration(UserCreateDTO user) {

        userService.registration(user);

        return "redirect:/auth/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, SessionStatus status) {
        status.setComplete();
        session.invalidate();
        return "redirect:/auth/login";
    }
}
