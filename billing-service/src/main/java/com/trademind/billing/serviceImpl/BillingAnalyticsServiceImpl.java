package com.trademind.billing.serviceImpl;

import com.trademind.billing.dto.*;
import com.trademind.billing.enums.SourceType;
import com.trademind.billing.repository.InvoiceRepository;
import com.trademind.billing.service.BillingAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingAnalyticsServiceImpl implements BillingAnalyticsService {

    private final InvoiceRepository repository;

    @Override
    public String getTotalRevenue() {
        return repository.getTotalRevenue().toString();
    }

    @Override
    public String getRevenueByMerchant(Long merchantId) {
        return repository.getRevenueByMerchant(merchantId).toString();
    }

    @Override
    public String getRevenueByRetailer(Long retailerId) {
        return repository.getRevenueByRetailer(retailerId).toString();
    }

    @Override
    public String getTotalSpentByCustomer(Long customerId) {
        return repository.getTotalSpentByCustomer(customerId).toString();
    }

    @Override
    public String getTodaySales(Long retailerId) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        return repository.getTodaySales(retailerId, startOfDay).toString();
    }

    @Override
    public List<RevenueTrendDto> getRevenueTrend(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return repository.getRevenueTrend(startDate);
    }

    @Override
    public List<TopMerchantRawDto> getTopMerchants(int limit) {
        return repository.getTopMerchants(PageRequest.of(0, limit));
    }

    @Override
    public RevenueResponseDto getRevenue(RevenueRequestDto request) {

        LocalDateTime start = request.getStartDate().atStartOfDay();
        LocalDateTime end = request.getEndDate().atTime(23,59,59);

        BigDecimal total = repository.getRevenueBetween(
                start,
                end,
                request.getEntityType(),
                request.getEntityId()
        );


        List<Object[]> rawData = repository.getDailyRevenue(
                start,
                end,
                request.getEntityType(),
                request.getEntityId()
        );


        List<RevenueDataPointDto> data = rawData.stream()
                .map(r -> RevenueDataPointDto.builder()
                        .label(r[0].toString())
                        .revenue(r[1].toString())
                        .build())
                .toList();

        return RevenueResponseDto.builder()
                .totalRevenue(total.toString())
                .data(data)
                .build();
    }

    @Override
    public AdminRevenueSummaryDto getAdminRevenueSummary() {
        return repository.getAdminRevenueSummary();
    }

    public List<RevenueGraphDto> getRevenueGraph(
            Long sourceId,
            SourceType sourceType,
            Long userId) {

        LocalDateTime start = LocalDateTime.now().minusDays(7);

        return repository.getRevenueGraph(start, sourceId, sourceType, userId)
                .stream()
                .map(r -> new RevenueGraphDto(
                        r[0].toString(),
                        r[1].toString()
                ))
                .toList();
    }
}