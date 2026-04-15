package com.trademind.cart.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trademind.events.common.SellerSnapshotDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SellerSnapshotBuilder {

    private final ObjectMapper objectMapper;

    public String toJson(SellerSnapshotDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (Exception e) {
            throw new RuntimeException("Snapshot conversion failed", e);
        }
    }
}
