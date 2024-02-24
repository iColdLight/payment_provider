package com.coldlight.payment_provider.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "merchant")
public class Merchant extends BaseEntity{

    @Column(name = "merchant_name")
    private String name;

    @Column(name = "contact")
    private String contact;
}
