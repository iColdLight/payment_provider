package com.coldlight.payment_provider.service;

import com.coldlight.payment_provider.model.Transaction;
import com.coldlight.payment_provider.model.TransactionStatus;
import com.coldlight.payment_provider.repository.TransactionRepository;
import com.coldlight.payment_provider.repository.WebHookRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

//TODO: add Scheduled job
@Service
@Transactional
public class TransactionProcessor {

    //Get all transactions
    //Set status: 80% - success, 20% - fail
    //Send webhook to notification_url
    //Save all webhooks
    //Add retry logic - 3 times in the worst case
    private final WebClient webClient; // WebClient для отправки HTTP-запросов
    private final TransactionRepository transactionRepository;
    private final WebHookRepository webHookRepository;


    public TransactionProcessor(WebClient.Builder webClientBuilder, TransactionRepository transactionRepository, WebHookRepository webHookRepository) {
        this.webClient = webClientBuilder.build();
        this.transactionRepository = transactionRepository;
        this.webHookRepository = webHookRepository;
    }

    @Scheduled(fixedDelay = 10000)
    public void processTransactions() {
        Flux<Transaction> transactions = getAllTransactions();
        transactions
                .flatMap(transaction -> {
                    boolean isSuccess = Math.random() < 0.8;
                    transaction.setTransactionStatus(isSuccess ? TransactionStatus.SUCCESS : TransactionStatus.FAILURE);
                    Mono<Void> sendWebhook = sendWebhook(transaction);
                    Mono<Void> saveWebhook = saveWebhook(transaction);
                    Mono<Void> retryLogic = retryLogic(transaction, 3);
                    return Mono.when(sendWebhook, saveWebhook, retryLogic);
                })
                .subscribe();
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
        return Mono.defer(() -> {
            return Mono.just(transaction)
                    .flatMap(this::processTransaction)
                    .onErrorResume(error -> {
                        int currentAttempt = attempt.incrementAndGet();
                        if (currentAttempt < maxRetryAttempts) {
                            System.err.println("Произошла ошибка, повторная попытка через 1 секунду...");
                            return Mono.delay(Duration.ofSeconds(1))
                                    .then(retryLogic(transaction, maxRetryAttempts));
                        } else {
                            return Mono.error(new RuntimeException("Достигнуто максимальное количество попыток повтора.", error));
                        }
                    });
        });
    }

    private Mono<Void> processTransaction(Transaction transaction) {
        return Mono.fromRunnable(() -> {
            try {
                System.out.println("Processing transaction: " + transaction.getId());
                System.out.println("Payment method: " + transaction.getPaymentMethod());

                Thread.sleep(1000);

                if (transaction.equals(null)) {
                    throw new RuntimeException("Transaction is not found.");
                }
                System.out.println("Transaction processed successfully: " + transaction.getId());
            } catch (InterruptedException e) {
                System.err.println("Transaction processing was interrupted: " + transaction.getId());
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("An error occurred while processing the transaction: " + transaction.getId());
                e.printStackTrace();
            }
        });
    }
}
