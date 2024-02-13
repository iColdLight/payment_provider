package com.coldlight.payment_provider.repository;

import com.coldlight.payment_provider.model.Merchant;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface MerchantRepository extends R2dbcRepository<Merchant, Long> {
}
