package com.coldlight.payment_provider.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import javax.persistence.Id;
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
