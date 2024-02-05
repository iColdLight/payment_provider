package com.coldlight.payment_provider.repository;

import com.coldlight.payment_provider.model.Transaction;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface WebHookRepository extends R2dbcRepository<Transaction, Long> {

}
