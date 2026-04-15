package com.trademind.order.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trademind.events.common.SellerSnapshotDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SellerSnapshotMapper {

    private final ObjectMapper objectMapper;

    // --------------------------------------------------
    // DTO → JSON
    // --------------------------------------------------
    public String toJson(SellerSnapshotDto dto) {
        if (dto == null) return null;

        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert SellerSnapshotDto to JSON", e);
        }
    }

    // --------------------------------------------------
    // JSON → DTO
    // --------------------------------------------------
    public SellerSnapshotDto fromJson(String json) {
        if (json == null || json.isBlank()) return null;

        try {
            return objectMapper.readValue(json, SellerSnapshotDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse SellerSnapshot JSON", e);
        }
    }
}