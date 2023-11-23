package com.fc.shimpyo_be.domain.product.repository.model;

import com.fc.shimpyo_be.domain.product.entity.Category;
import com.fc.shimpyo_be.domain.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> likeProductName(String productName) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("productName"), "%"+productName+"%");
    }
    public static Specification<Product> likeCategroy(String category) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("category"),"%"+category+"%");
    }
    public static Specification<Product> likeAddress(String address) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("address"),"%"+address+"%");
    }

}
