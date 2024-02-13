package com.coldlight.payment_provider.repository;

import com.coldlight.payment_provider.model.Transaction;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionRepository extends R2dbcRepository<Transaction, Long> {
    @Query("SELECT * FROM transaction WHERE createdAt >= :startDate AND createdAt <= :endDate")
    Flux<Transaction> findTransactionsByDateRange(Long startDate, Long endDate);

    Mono<Transaction> getTransactionById(Long id);

}
