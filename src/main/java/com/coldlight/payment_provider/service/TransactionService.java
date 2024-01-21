package com.coldlight.payment_provider.service;

import com.coldlight.payment_provider.model.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {

    Mono<Transaction> deposit(Transaction transaction);

    Mono<Transaction> withdraw(Transaction transaction);

    Flux<Transaction> getAllTransactions();

    Mono<Transaction> getTransactionDetailsById(Long id);

    Flux<Transaction> getTransactionsByDateRange(Long startDate, Long endDate);




}
