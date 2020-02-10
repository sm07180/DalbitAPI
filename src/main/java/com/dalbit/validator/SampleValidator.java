package com.dalbit.validator;

import com.dalbit.validator.annotation.Sample;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

@Slf4j
public class SampleValidator implements ConstraintValidator<Sample, String> {
    @Override
    public void initialize(Sample sample) {
        log.debug("SampleValidator.initialize()...");
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        log.debug(name);
        return false;
    }
}
