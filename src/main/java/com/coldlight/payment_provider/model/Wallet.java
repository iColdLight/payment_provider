package com.coldlight.payment_provider.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "wallet")
public class Wallet extends BaseEntity{

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column (name = "balance")
    private Long balance;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Transient
    private Merchant merchant;
}
