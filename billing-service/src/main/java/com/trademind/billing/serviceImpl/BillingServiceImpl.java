package com.trademind.billing.serviceImpl;

import com.trademind.billing.dto.BillItemRequest;
import com.trademind.billing.dto.BillResponse;
import com.trademind.billing.dto.CreateBillRequest;
import com.trademind.billing.entity.Bill;
import com.trademind.billing.entity.BillHash;
import com.trademind.billing.entity.BillItem;
import com.trademind.billing.enums.PaymentStatus;
import com.trademind.billing.kafka.BillingEventProducer;
import com.trademind.billing.repository.BillHashRepository;
import com.trademind.billing.repository.BillItemRepository;
import com.trademind.billing.repository.BillRepository;
import com.trademind.billing.service.BillingService;
import com.trademind.billing.util.HashUtil;
import com.trademind.billing.webclient.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BillingServiceImpl implements BillingService {

    private final BillRepository billRepo;
    private final BillItemRepository itemRepo;
    private final BillHashRepository hashRepo;
    private final ProductClient productClient;
    private final BillingEventProducer producer;
    private final HashUtil hashUtil;

    @Override
    public BillResponse generateBill(CreateBillRequest request) {

        Bill bill = billRepo.save(
                Bill.builder()
                        .billNumber(UUID.randomUUID().toString())
                        .retailerId(request.retailerId())
                        .customerId(request.customerId())
                        .paymentStatus(PaymentStatus.UNPAID)
                        .billDate(LocalDateTime.now())
                        .totalAmount(BigDecimal.ZERO)
                        .build()
        );

        BigDecimal total = BigDecimal.ZERO;

        for (BillItemRequest reqItem : request.items()) {

            BigDecimal price = productClient.getProductPrice(reqItem.productId());
            BigDecimal lineTotal = price.multiply(
                    BigDecimal.valueOf(reqItem.quantity())
            );

            itemRepo.save(new BillItem(
                    null,
                    bill.getId(),
                    reqItem.productId(),
                    reqItem.quantity(),
                    price,
                    lineTotal
            ));

            producer.publishStockOut(
                    reqItem.productId(),
                    reqItem.quantity(),
                    bill.getId()
            );

            total = total.add(lineTotal);
        }

        bill.setTotalAmount(total);
        billRepo.save(bill);

        String hash = hashUtil.sha256(
                bill.getBillNumber() + total + bill.getBillDate()
        );

        hashRepo.save(new BillHash(
                null,
                bill.getId(),
                hash,
                "SHA-256",
                LocalDateTime.now()
        ));

        return BillResponse.builder()
                .billId(bill.getId())
                .billNumber(bill.getBillNumber())
                .totalAmount(total)
                .hash(hash)
                .build();
    }
}
