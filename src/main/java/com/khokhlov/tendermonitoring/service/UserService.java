package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.error.exception.auth.InvalidPasswordException;
import com.khokhlov.tendermonitoring.error.exception.auth.InvalidUsernameException;
import com.khokhlov.tendermonitoring.mapper.UserMapper;
import com.khokhlov.tendermonitoring.model.dto.UserCreateDTO;
import com.khokhlov.tendermonitoring.model.dto.UserDTO;
import com.khokhlov.tendermonitoring.model.dto.UserLoginDTO;
import com.khokhlov.tendermonitoring.model.entity.Role;
import com.khokhlov.tendermonitoring.model.entity.User;
import com.khokhlov.tendermonitoring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    public UserDTO login(UserLoginDTO userDTO) {
        User user = userRepository.findByUsernameIgnoreCase(userDTO.getUsername())
                .orElseThrow(() -> new InvalidUsernameException("User with username \"" + userDTO.getUsername() + "\" does not exist"));

        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Wrong password");
        }

        return mapper.toDTO(user);
    }

    @Transactional
    public void registration(UserCreateDTO createDTO) {
        User userToSave = mapper.toEntity(createDTO);
        userToSave.setUsername(createDTO.getUsername().trim().toLowerCase());
        userToSave.setRole(Role.USER);
        userToSave.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        try {
            userRepository.save(userToSave);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidUsernameException("Пользователь с именем: \"" + createDTO.getUsername().trim() + "\" уже существует");
        }
    }

    @Transactional
    public void updateTelegramChatId(Long userId, Long chatId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUsernameException("User not found"));
        user.setTelegramChatId(chatId);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().getAuthority())
                .build();
    }

    public long count() {
        return userRepository.count();
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void toggleRole(Long id) {
        User u = userRepository.findById(id).orElseThrow();
        u.setRole(u.getRole() == Role.ADMIN ? Role.USER : Role.ADMIN);
    }

}
