package com.fc.shimpyo_be.domain.product.util.model;

import com.fc.shimpyo_be.domain.product.util.PageableValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PageableValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PageableConstraint {

    String message() default "정확하지 않은 Pagination입니다.";

    Class<?>[] value() default {};

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}