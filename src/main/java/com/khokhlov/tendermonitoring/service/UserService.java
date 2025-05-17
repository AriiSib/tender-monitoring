package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.mapper.UserMapper;
import com.khokhlov.tendermonitoring.model.dto.UserCreateDTO;
import com.khokhlov.tendermonitoring.model.dto.UserDTO;
import com.khokhlov.tendermonitoring.model.dto.UserLoginDTO;
import com.khokhlov.tendermonitoring.model.entity.User;
import com.khokhlov.tendermonitoring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    //    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    public UserDTO login(UserLoginDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.username())
                .orElseThrow(() -> new RuntimeException("User with username \"" + userDTO.username() + "\" does not exist"));

        if (!user.getPassword().equals(userDTO.password())) {
            throw new RuntimeException("Wrong password");
        }

//        if (!passwordEncoder.matches(userDTO.password(), user.getPassword())) {
//            throw new RuntimeException("Wrong password");
//        }

        return mapper.toDTO(user);
    }

    @Transactional
    public void registration(UserCreateDTO createDTO) {
        User userToSave = mapper.toEntity(createDTO);
//        userToSave.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        userRepository.save(userToSave);
    }
}
