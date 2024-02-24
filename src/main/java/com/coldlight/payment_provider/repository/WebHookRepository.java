package com.coldlight.payment_provider.repository;

import com.coldlight.payment_provider.model.Transaction;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories
public interface WebHookRepository extends R2dbcRepository<Transaction, Long> {

}
