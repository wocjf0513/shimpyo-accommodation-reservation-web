package com.fc.shimpyo_be.domain.product.dto.request;

import com.fc.shimpyo_be.domain.product.entity.Category;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import lombok.Builder;

public record SearchKeywordRequest(
    @NotNull
    String productName,
    @NotNull
    String address,
    @NotNull
    List<Category> category,
    @NotNull
    Long capacity) {

    final static List<Category> allCategories = Arrays.stream(Category.values()).toList();

    @Builder
    public SearchKeywordRequest(String productName, String address, String category,
        Long capacity) {
        this(productName, address,
            category.equals("") ? allCategories : List.of(Category.getByName(category)), capacity);
    }
}
