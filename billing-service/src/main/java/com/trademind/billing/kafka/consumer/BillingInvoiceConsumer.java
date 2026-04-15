package com.trademind.billing.kafka.consumer;

import com.trademind.billing.entity.Invoice;
import com.trademind.billing.mapper.InvoiceMapper;
import com.trademind.billing.mapper.SellerSnapshotMapper;
import com.trademind.billing.repository.InvoiceRepository;
import com.trademind.events.order.OrderBillingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingInvoiceConsumer {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final SellerSnapshotMapper sellerSnapshotMapper;

    @KafkaListener(
            topics = "order.billing.generate",
            groupId = "billing-service-group"
    )
    @Transactional
    public void handleBillingEvent(OrderBillingEvent event, Acknowledgment ack) {

        try {

            log.info("Received OrderBillingEvent for orderId={}", event.orderId());

            // ✅ Only PAID
            if (!event.paymentStatus().equals("PAID")) {
                return;
            }

            // ✅ Idempotency
            if (invoiceRepository.existsByOrderId(event.orderId())) {
                return;
            }

            // -------------------------------------------------------
            // 1️⃣ CREATE INVOICE
            // -------------------------------------------------------
            Invoice invoice = invoiceMapper.fromEvent(event);

            // -------------------------------------------------------
            // 3️⃣ STORE SNAPSHOT
            // -------------------------------------------------------
            invoice.setSellerSnapshot(
                    sellerSnapshotMapper.toJson(event.seller())
            );
            // -------------------------------------------------------
            // 4️⃣ SAVE
            // -------------------------------------------------------
            invoiceRepository.save(invoice);

            log.info("Invoice created with seller snapshot for orderId={}", event.orderId());

            ack.acknowledge();

        } catch (Exception e) {
            log.error("Error in billing consumer", e);
            ack.acknowledge();
        }
    }
}