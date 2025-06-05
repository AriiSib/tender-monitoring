package com.khokhlov.tendermonitoring.model.dto;

import com.khokhlov.tendermonitoring.validation.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class UserCreateDTO {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 4, max = 20, message = "Имя пользователя должно быть от 4 до 20 символов")
    private String username;

    @NotBlank(message = "Не указан пароль")
    @Size(min = 4, max = 32, message = "Пароль должен быть от 4 до 32 символов")
    private String password;
    private String repeatPassword;
}
