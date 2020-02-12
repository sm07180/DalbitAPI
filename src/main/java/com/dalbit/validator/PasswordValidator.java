package com.dalbit.validator;

import com.dalbit.util.DalbitUtil;
import com.dalbit.validator.annotation.Password;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class PasswordValidator implements ConstraintValidator<Password, String> {
    @Override
    public void initialize(Password password) {
        log.debug("Password pattern.initialize()...");
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        log.debug(password);
        return DalbitUtil.isPasswordCheck(password);
    }
}
