package org.trips.service_framework.utils;

import org.trips.service_framework.exceptions.ValidationException;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;
import java.util.stream.Collectors;
import java.util.Set;

public class ValidationUtils {
    public static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    public static final Validator validator = factory.getValidator();

    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            String errMsg = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
            throw new ValidationException(errMsg);
        }
    }
}
