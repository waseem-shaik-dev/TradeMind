package com.trademind.order.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CodPaymentReceivedRequestDto(

        @NotBlank
        String transactionReference

) {}
