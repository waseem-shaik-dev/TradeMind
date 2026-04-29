package com.trademind.product.repository.spec;

import com.trademind.product.entity.Product;
import com.trademind.product.entity.ProductPriceHistory;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> filter(
            String keyword,
            List<Long> categoryIds,
            Long brandId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean active
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            /* 🔍 KEYWORD */
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.toLowerCase() + "%";

                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("sku")), like)
                ));
            }

            /* 🧩 CATEGORY (WITH TREE SUPPORT) */
            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicates.add(root.get("categoryId").in(categoryIds));
            }

            /* 🏷️ BRAND */
            if (brandId != null) {
                predicates.add(cb.equal(root.get("brandId"), brandId));
            }

            /* ✅ ACTIVE */
            if (active != null) {
                predicates.add(cb.equal(root.get("active"), active));
            }

            /* 💰 PRICE FILTER (SUBQUERY) */
            if (minPrice != null || maxPrice != null) {

                Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
                Root<ProductPriceHistory> priceRoot =
                        subquery.from(ProductPriceHistory.class);

                subquery.select(priceRoot.get("price"))
                        .where(
                                cb.equal(priceRoot.get("productId"), root.get("id")),
                                cb.isNull(priceRoot.get("effectiveTo"))
                        );

                if (minPrice != null) {
                    predicates.add(cb.greaterThanOrEqualTo(subquery, minPrice));
                }

                if (maxPrice != null) {
                    predicates.add(cb.lessThanOrEqualTo(subquery, maxPrice));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}