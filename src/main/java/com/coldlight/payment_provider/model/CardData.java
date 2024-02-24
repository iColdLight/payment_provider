package com.coldlight.payment_provider.model;

import javax.persistence.Column;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "card_data")
public class CardData extends BaseEntity {

    @Column(name = "card_number")
    private Long cardNumber;

    @Column(name = "exp_date")
    private Long expDate;

    @Column(name = "cvv")
    private Long cvv;
}
