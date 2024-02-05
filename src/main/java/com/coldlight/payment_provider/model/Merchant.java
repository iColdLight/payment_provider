package com.coldlight.payment_provider.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "merchant")
public class Merchant extends BaseEntity{

    @Column(name = "merchant_name")
    private String name;

    @Column(name = "contact")
    private String contact;

    @Column(name = "wallet_id")
    private Long walletId;

    @Transient
    private Wallet wallet;
}
