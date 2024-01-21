package com.coldlight.payment_provider.service;

import com.coldlight.payment_provider.model.Transaction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Override
    public Mono<Transaction> deposit(Transaction request) {
        // Вызываем сервис платежного провайдера для создания транзакции
        return null;
        /*return paymentProviderService.createTransaction(request)
                .map(id -> new Transaction(id, request.getAmount()));*/
    }

    @Override
    public Mono<Transaction> withdraw(Transaction transaction) {
        return null;
    }

    @Override
    public Flux<Transaction> getAllTransactions() {
        return null;
    }

    @Override
    public Flux<Transaction> getTransactionsByDateRange(Long startDate, Long endDate) {
        return null;
    }

    @Override
    public Mono<Transaction> getTransactionDetailsById(Long id) {
        return null;
    }
}
