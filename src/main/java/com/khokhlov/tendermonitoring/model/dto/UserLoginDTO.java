package com.khokhlov.tendermonitoring.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    private @NotBlank(message = "Не введено имя пользователя") String username;
    private @NotBlank(message = "Не введен пароль") String password;
}
