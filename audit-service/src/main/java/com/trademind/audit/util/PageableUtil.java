package com.trademind.audit.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class PageableUtil {

    private static final List<String> ALLOWED_SORT_FIELDS = List.of(
            "timestamp",
            "userId",
            "action",
            "entityType",
            "serviceName"
    );

    public static Pageable buildPageable(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        // fallback safety
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "timestamp";
        }

        Sort sort = direction.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return PageRequest.of(page, size, sort);
    }
}