package com.trademind.billing.kafka.consumer;

import com.trademind.billing.entity.Invoice;
import com.trademind.billing.mapper.InvoiceMapper;
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

    @KafkaListener(
            topics = "order.billing.generate",
            groupId = "billing-service-group"
    )
    @Transactional
    public void handleBillingEvent(OrderBillingEvent event, Acknowledgment ack) {

        try {


            log.info("Received OrderBillingEvent for orderId={}", event.orderId());

            // -------------------------------------------------------
            // 1️⃣ Only process PAID orders
            // -------------------------------------------------------
            if (!event.paymentStatus().equals("PAID")) {
                log.info("Skipping invoice generation for orderId={} as payment not PAID", event.orderId());
                return;
            }

            // -------------------------------------------------------
            // 2️⃣ Idempotency check
            // -------------------------------------------------------
            if (invoiceRepository.existsByOrderId(event.orderId())) {
                log.info("Invoice already exists for orderId={}, skipping", event.orderId());
                return;
            }

            // -------------------------------------------------------
            // 3️⃣ Create Invoice
            // -------------------------------------------------------
            Invoice invoice = invoiceMapper.fromEvent(event);

            invoiceRepository.save(invoice);

            log.info("Invoice created successfully for orderId={}, invoiceId={}",
                    event.orderId(),
                    invoice.getId());

            ack.acknowledge();

        } catch (Exception e) {
            log.error("Error in billing consumer ",e);
            ack.acknowledge();
        }
    }
}