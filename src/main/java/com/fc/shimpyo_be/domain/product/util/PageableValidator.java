package com.fc.shimpyo_be.domain.product.util;

import com.fc.shimpyo_be.domain.product.util.model.PageableConstraint;
import jakarta.persistence.Entity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class PageableValidator implements ConstraintValidator<PageableConstraint, Pageable> {

    private Class<?>[] entityClasses;

    public void initialize(PageableConstraint constraintAnnotation) {
        this.entityClasses = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Pageable pageable, ConstraintValidatorContext context) {

        Sort sort = pageable.getSort();

        for (Order order : sort.toList()) {
            String field = order.getProperty();

            if (order.getDirection() != null) {
                String direction = order.getDirection().toString();

                if (!direction.equals("ASC") && !direction.equals("DESC")) {
                    return false;
                }
            }

            for (Class<?> entityClass : entityClasses) {
                if (isInvalidClass(entityClass, field)) {
                    return false;
                }
            }
        }
        return true;

    }

    private boolean isInvalidClass(Class<?> entityClass, String field) {
        try {
            Class<?> clazz = entityClass.getDeclaredField(field).getType();
            if (clazz.getAnnotation(Entity.class) != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
