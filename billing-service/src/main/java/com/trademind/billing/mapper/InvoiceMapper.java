package com.trademind.billing.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trademind.billing.dto.*;
import com.trademind.billing.entity.Invoice;
import com.trademind.billing.entity.InvoiceLineItem;
import com.trademind.billing.enums.PaymentMethod;
import com.trademind.billing.enums.PaymentStatus;
import com.trademind.billing.enums.SourceType;
import com.trademind.events.order.OrderBillingEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {

    private final ObjectMapper objectMapper;

    private static final String SECRET = "billing-secret-key"; // later move to config

    // ============================================================
    // 1️⃣ EVENT → ENTITY
    // ============================================================

    public Invoice fromEvent(OrderBillingEvent event) {

        Invoice invoice = Invoice.builder()
                .invoiceNumber(generateInvoiceNumber())
                .orderId(event.orderId())
                .orderNumber(event.orderNumber())
                .userId(event.userId())
                .sourceId(event.sourceId())
                .sourceType(SourceType.valueOf(event.sourceType()))
                .paymentMethod(PaymentMethod.valueOf(event.paymentMethod()))
                .paymentStatus(PaymentStatus.valueOf(event.paymentStatus()))
                .subtotal(event.subtotalAmount())
                .taxAmount(event.taxAmount())
                .discountAmount(event.discountAmount())
                .deliveryFee(event.deliveryFee())
                .grandTotal(event.grandTotal())
                .currency(event.currency())
                .addressSnapshot(convertAddressToJson(event.address()))
                .build();

        // Line Items
        event.items().forEach(item -> {
            InvoiceLineItem lineItem = InvoiceLineItem.builder()
                    .productId(item.productId())
                    .productName(item.productName())
                    .sku(item.sku())
                    .imageUrl(item.imageUrl())
                    .unitPrice(item.unitPrice())
                    .quantity(item.quantity())
                    .totalPrice(item.totalPrice())
                    .build();

            invoice.addItem(lineItem);
        });

        // Hash
        invoice.setInvoiceHash(generateHash(invoice));

        return invoice;
    }

    // ============================================================
    // 2️⃣ ENTITY → FULL DTO
    // ============================================================

    public InvoiceResponseDto toResponseDto(Invoice invoice) {

        return new InvoiceResponseDto(
                invoice.getId(),
                invoice.getInvoiceNumber(),

                invoice.getOrderId(),
                invoice.getOrderNumber(),

                invoice.getUserId(),
                invoice.getSourceId(),
                invoice.getSourceType(),

                invoice.getPaymentMethod(),
                invoice.getPaymentStatus(),

                invoice.getSubtotal(),
                invoice.getTaxAmount(),
                invoice.getDiscountAmount(),
                invoice.getDeliveryFee(),
                invoice.getGrandTotal(),
                invoice.getCurrency(),

                invoice.getAddressSnapshot(),

                mapLineItems(invoice.getItems()),

                invoice.getInvoiceHash(),

                invoice.getCreatedAt()
        );
    }

    // ============================================================
    // 3️⃣ ENTITY → SUMMARY DTO
    // ============================================================

    public InvoiceSummaryDto toSummaryDto(Invoice invoice) {

        return new InvoiceSummaryDto(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getOrderNumber(),
                invoice.getGrandTotal(),
                invoice.getCurrency(),
                invoice.getPaymentStatus(),
                invoice.getCreatedAt()
        );
    }

    // ============================================================
    // 4️⃣ HELPERS
    // ============================================================

    private List<InvoiceLineItemDto> mapLineItems(List<InvoiceLineItem> items) {
        return items.stream()
                .map(i -> new InvoiceLineItemDto(
                        i.getProductId(),
                        i.getProductName(),
                        i.getSku(),
                        i.getImageUrl(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.getTotalPrice()
                ))
                .collect(Collectors.toList());
    }

    private String convertAddressToJson(Object address) {
        try {
            return objectMapper.writeValueAsString(address);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize address", e);
        }
    }

    private String generateInvoiceNumber() {
        return "BILL-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private String generateHash(Invoice invoice) {
        try {
            String raw = invoice.getOrderId() + "|" +
                    invoice.getGrandTotal() + "|" +
                    LocalDateTime.now();

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((raw + SECRET).getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();

        } catch (Exception e) {
            throw new RuntimeException("Hash generation failed", e);
        }
    }
}