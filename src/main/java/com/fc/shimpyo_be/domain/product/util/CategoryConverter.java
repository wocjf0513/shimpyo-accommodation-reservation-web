package com.fc.shimpyo_be.domain.product.util;

import com.fc.shimpyo_be.domain.product.entity.Category;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CategoryConverter implements
    AttributeConverter<Category, String> {

    @Override
    public String convertToDatabaseColumn(Category attribute) {
        return attribute.getName();
    }

    @Override
    public Category convertToEntityAttribute(String dbData) {
        return dbData==null?Category.HOTEL:Category.getByName(dbData);
    }
}
