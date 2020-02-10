package com.dalbit.validator.annotation;

import com.dalbit.validator.SampleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SampleValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sample {
    String message() default "Sample annotation check";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
