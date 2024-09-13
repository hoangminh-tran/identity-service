package com.learning.identityservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolationException;

import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<UUIDConstraint, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            return false;
        }
        return true;
    }

    @Override
    public void initialize(UUIDConstraint constraintAnnotation) {
    }
}
