package com.coldlight.payment_provider.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transaction")
public class Transaction extends BaseEntity{

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "external_transaction_id")
    private Long externalTransactionId;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    @Transient
    private CardData cadData;

    @Column(name = "transaction_language")
    @Enumerated(EnumType.STRING)
    private Language language;

    @Transient
    private Merchant merchantId;

    @Transient
    private Customer customerId;
}
