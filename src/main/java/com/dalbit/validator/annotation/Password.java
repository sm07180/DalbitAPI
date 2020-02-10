package com.dalbit.validator.annotation;

import com.dalbit.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "8자~20자, 영문,숫자,특수문자 중 2가지 이상 조합 확인하세요.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
