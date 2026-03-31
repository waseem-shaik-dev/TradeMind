package com.trademind.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCountResponse {

    private long totalMerchants;
    private long totalRetailers;
    private long totalCustomers;
}