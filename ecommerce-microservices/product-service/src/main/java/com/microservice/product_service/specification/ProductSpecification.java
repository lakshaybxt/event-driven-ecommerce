package com.microservice.product_service.specification;

import com.microservice.product_service.domain.dto.ProductSearchCriteria;
import com.microservice.product_service.domain.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> withCriteria(ProductSearchCriteria c) {
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(c.getKeyword() != null) {
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + c.getKeyword().toLowerCase() + "%"));
                }

                if(c.getCategoryId() != null) {
                    predicates.add(cb.equal(root.get("category").get("id"), c.getCategoryId()));
                }

                if(c.getBrandId() != null) {
                    predicates.add(cb.equal(root.get("brand").get("id"), c.getBrandId()));
                }

                if(c.getMinPrice() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("discountedPrice"), c.getMinPrice()));
                }

                if(c.getMaxPrice() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("discountedPrice"), c.getMaxPrice()));
                }

                if (c.getTags() != null && !c.getTags().isEmpty()) {
                    predicates.add(root.join("tags").in(c.getTags()));
                }

                if(c.isInStockOnly()) {
                    predicates.add(cb.greaterThan(root.get("stackQuantity"), 0));
                }

                return cb.and(predicates.toArray(new Predicate[0]));

            }
        };
    }
}
