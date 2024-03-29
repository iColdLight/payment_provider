package com.coldlight.payment_provider.model;

import javax.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "transaction")
public class Transaction extends BaseEntity{

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "external_transaction_id")
    private Long externalTransactionId;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "transaction_language")
    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "wallet_id")
    private Long walletId;

    @Column(name = "notification_url")
    private String notificationUrl;

    @Transient
    private CardData cardData;

    @Transient
    private Customer customer;

    @Transient
    private Wallet wallet;
}
