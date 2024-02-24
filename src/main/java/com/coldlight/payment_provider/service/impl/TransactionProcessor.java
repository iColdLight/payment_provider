package com.coldlight.payment_provider.service.impl;

import com.coldlight.payment_provider.model.Transaction;
import com.coldlight.payment_provider.model.TransactionStatus;
import com.coldlight.payment_provider.repository.TransactionRepository;
import com.coldlight.payment_provider.repository.WebHookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

//TODO: add Scheduled job
@Service
@Slf4j
@Transactional
public class TransactionProcessor {

    private final WebClient webClient; // WebClient для отправки HTTP-запросов
    private final TransactionRepository transactionRepository;
    private final WebHookRepository webHookRepository;


    public TransactionProcessor(WebClient.Builder webClientBuilder, TransactionRepository transactionRepository, WebHookRepository webHookRepository) {
        this.webClient = webClientBuilder.build();
        this.transactionRepository = transactionRepository;
        this.webHookRepository = webHookRepository;
    }


    public Flux<Void> processTransactions() {
        return getAllTransactions()
                .flatMap(transaction -> {
                    boolean isSuccess = Math.random() < 0.8;
                    transaction.setTransactionStatus(isSuccess ? TransactionStatus.SUCCESS : TransactionStatus.PENDING);
                    Mono<Void> sendWebhook = sendWebhook(transaction);
                    Mono<Void> saveWebhook = saveWebhook(transaction);
                    Mono<Void> retryLogic = retryLogic(transaction, 3);
                    return Mono.when(sendWebhook, saveWebhook, retryLogic).then();
                });
    }

    private Flux<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    private Mono<Void> sendWebhook(Transaction transaction) {
        return webClient
                .post()
                .uri(transaction.getNotificationUrl())
                .bodyValue(transaction)
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    private Mono<Void> saveWebhook(Transaction transaction) {
        return webHookRepository.save(transaction).then();
    }

    private Mono<Void> retryLogic(Transaction transaction, int maxRetryAttempts) {
        AtomicInteger attempt = new AtomicInteger(0);
        return Mono.defer(() -> Mono.just(transaction)
                .flatMap(this::processTransaction)
                .onErrorResume(error -> {
                    int currentAttempt = attempt.incrementAndGet();
                    if (currentAttempt < maxRetryAttempts) {
                        log.error("Произошла ошибка, повторная попытка через 1 секунду...");
                        return Mono.delay(Duration.ofSeconds(1))
                                .then(retryLogic(transaction, maxRetryAttempts));
                    } else {
                        return Mono.error(new RuntimeException("Достигнуто максимальное количество попыток повтора.", error));
                    }
                }));
    }

    private Mono<Void> processTransaction(Transaction transaction) {
        return Mono.fromRunnable(() -> {
            try {
                log.info("Processing transaction: " + transaction.getId());
                log.info("Payment method: " + transaction.getPaymentMethod());
                log.info("Transaction processed successfully: " + transaction.getId());
            } catch (Exception e) {
                log.error("An error occurred while processing the transaction: " + transaction.getId());
                e.printStackTrace();
            }
        });
    }
}
