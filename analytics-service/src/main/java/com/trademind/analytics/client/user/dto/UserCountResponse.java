package com.trademind.analytics.client.user.dto;

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