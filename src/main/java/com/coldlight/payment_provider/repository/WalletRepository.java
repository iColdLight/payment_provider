package com.coldlight.payment_provider.repository;

import com.coldlight.payment_provider.model.Wallet;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface WalletRepository extends R2dbcRepository<Wallet, Long> {
}
