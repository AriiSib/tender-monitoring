package com.khokhlov.tendermonitoring.validation.impl;

import com.khokhlov.tendermonitoring.model.dto.UserCreateDTO;
import com.khokhlov.tendermonitoring.validation.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserCreateDTO> {

    @Override
    public boolean isValid(UserCreateDTO value, ConstraintValidatorContext context) {
        return value.getPassword().equals(value.getRepeatPassword());
    }
}
