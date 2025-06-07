package com.khokhlov.tendermonitoring.controller;

import com.khokhlov.tendermonitoring.error.exception.auth.AuthenticationException;
import com.khokhlov.tendermonitoring.model.dto.UserCreateDTO;
import com.khokhlov.tendermonitoring.model.dto.UserDTO;
import com.khokhlov.tendermonitoring.model.dto.UserLoginDTO;
import com.khokhlov.tendermonitoring.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
//@SessionAttributes({"user"})
public class UserAuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new UserLoginDTO());
        return "auth/sign-in";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        return "auth/sign-up";
    }

    @PostMapping("/registration")
    public String registration(@Validated @ModelAttribute("user") UserCreateDTO user,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "auth/sign-up";

        userService.registration(user);
        return "redirect:/auth/login";
    }

}
