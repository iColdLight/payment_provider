package com.coldlight.payment_provider.repository;

import com.coldlight.payment_provider.model.Wallet;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories
public interface WalletRepository extends R2dbcRepository<Wallet, Long> {
}
