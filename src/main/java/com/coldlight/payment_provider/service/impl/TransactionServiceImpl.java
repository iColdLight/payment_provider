package com.coldlight.payment_provider.service.impl;

import com.coldlight.payment_provider.model.Transaction;
import com.coldlight.payment_provider.model.TransactionStatus;
import com.coldlight.payment_provider.repository.TransactionRepository;
import com.coldlight.payment_provider.service.CustomerService;
import com.coldlight.payment_provider.service.TransactionService;
import com.coldlight.payment_provider.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;
    private final WalletService walletService;


    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, CustomerService customerService, WalletService walletService) {
        this.transactionRepository = transactionRepository;
        this.customerService = customerService;
        this.walletService = walletService;
    }

    @Override
    public Mono<Transaction> deposit(Transaction request) {
        // Проверка валидности транзакции и обработка операций с клиентом и кошельком
        return customerService.getCustomerById(request.getCustomerId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found")))
                .flatMap(customer -> walletService.getWalletById(request.getWalletId())
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("Wallet not found")))
                        .flatMap(wallet -> {
                            if (customer.getBalance() < request.getAmount()) {
                                return Mono.error(new IllegalArgumentException("Insufficient balance"));
                            }

                            return Mono.zip(
                                    withdraw(customerService.getAccountBalance(request.getCustomerId()), request.getAmount()),
                                    deposit(walletService.getAccountBalance(request.getWalletId()), request.getAmount())
                            ).then(Mono.defer(() -> {
                                Transaction transaction = new Transaction();
                                transaction.setPaymentMethod(request.getPaymentMethod());
                                transaction.setTransactionType(request.getTransactionType());
                                transaction.setAmount(request.getAmount());
                                transaction.setCurrency(request.getCurrency());
                                transaction.setExternalTransactionId(request.getExternalTransactionId());
                                transaction.setCreatedAt(request.getCreatedAt());
                                transaction.setUpdatedAt(request.getUpdatedAt());
                                transaction.setCardId(request.getCardId());
                                transaction.setLanguage(request.getLanguage());
                                transaction.setMerchantId(request.getMerchantId());
                                transaction.setCustomerId(request.getCustomerId());
                                transaction.setWalletId(request.getWalletId());
                                transaction.setNotificationUrl(request.getNotificationUrl());
                                transaction.setTransactionStatus(TransactionStatus.PENDING);

                                transaction.setTransactionStatus(TransactionStatus.SUCCESS);

                                return transactionRepository.save(transaction)
                                        .thenReturn(transaction);
                            }));
                        }));
    }

    @Override
    public Mono<Transaction> withdraw(Transaction request) {
        return customerService.getCustomerById(request.getCustomerId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found")))
                .flatMap(customer -> walletService.getWalletById(request.getWalletId())
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("Wallet not found")))
                        .flatMap(wallet -> {
                            if (customer == null || wallet == null) {
                                return Mono.error(new IllegalArgumentException("Invalid customer or wallet"));
                            }
                            if (wallet.getBalance() < request.getAmount()) {
                                return Mono.error(new IllegalArgumentException("Insufficient balance"));
                            }

                            Mono<Long> walletBalanceMono = Mono.just(wallet.getBalance());

                            return withdraw(walletBalanceMono, request.getAmount())
                                    .then(deposit(customerService.getAccountBalance(request.getCustomerId()), request.getAmount()))
                                    .flatMap(balance -> {
                                        Transaction transaction = new Transaction();
                                        transaction.setPaymentMethod(request.getPaymentMethod());
                                        transaction.setTransactionType(request.getTransactionType());
                                        transaction.setAmount(request.getAmount());
                                        transaction.setCurrency(request.getCurrency());
                                        transaction.setExternalTransactionId(request.getExternalTransactionId());
                                        transaction.setCreatedAt(request.getCreatedAt());
                                        transaction.setUpdatedAt(request.getUpdatedAt());
                                        transaction.setCardId(request.getCardId());
                                        transaction.setLanguage(request.getLanguage());
                                        transaction.setMerchantId(request.getMerchantId());
                                        transaction.setCustomerId(request.getCustomerId());
                                        transaction.setWalletId(request.getWalletId());
                                        transaction.setNotificationUrl(request.getNotificationUrl());
                                        transaction.setTransactionStatus(TransactionStatus.SUCCESS);

                                        return transactionRepository.save(transaction)
                                                .thenReturn(transaction);
                                    });
                        }));
    }

    @Override
    public Flux<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Flux<Transaction> getTransactionsByDateRange(Long startDate, Long endDate) {
        return transactionRepository.findTransactionsByDateRange(startDate, endDate);
    }

    @Override
    public Mono<Transaction> getTransactionDetailsById(Long id) {
        return transactionRepository.getTransactionById(id);
    }

    private Mono<Void> withdraw(Mono<Long> accountBalanceMono, Long amount) {
        return accountBalanceMono.flatMap(accountBalance -> {
            if (amount <= 0) {
                return Mono.error(new IllegalArgumentException("Invalid withdrawal amount"));
            }

            if (accountBalance < amount) {
                return Mono.error(new IllegalArgumentException("Insufficient account balance"));
            }

            Long updatedBalance = accountBalance - amount;
            return Mono.just(updatedBalance);
        }).then();
    }

    private Mono<Void> deposit(Mono<Long> accountBalanceMono, Long amount) {
        return accountBalanceMono.flatMap(accountBalance -> {
            if (amount <= 0) {
                return Mono.error(new IllegalArgumentException("Invalid deposit amount"));
            }

            Long updatedBalance = accountBalance + amount;
            return Mono.just(updatedBalance);
        }).then();
    }
}


