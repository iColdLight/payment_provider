package com.coldlight.payment_provider.model;

import javax.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
