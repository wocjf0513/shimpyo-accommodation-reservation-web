package com.fc.shimpyo_be.domain.product.util;

import com.fc.shimpyo_be.domain.product.entity.Category;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CategoryConverter implements
    AttributeConverter<Category, String> {

    @Override
    public String convertToDatabaseColumn(Category attribute) {
        if(attribute == null){
            return Category.MOTEL.getName();
        }
        return attribute.getName();
    }

    @Override
    public Category convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty()){
            return Category.MOTEL;
        }
        return Category.getByName(dbData);
    }
}
