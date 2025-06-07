package com.khokhlov.tendermonitoring.error.handler;

import com.khokhlov.tendermonitoring.mapper.UserMapper;
import com.khokhlov.tendermonitoring.model.dto.UserDTO;
import com.khokhlov.tendermonitoring.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String username = authentication.getName();

        UserDTO userDTO = userMapper.toDTO(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username))
        );
        request.getSession().setAttribute("user", userDTO);

        response.sendRedirect("/home");
    }
}
