package com.coldlight.payment_provider.rest;

import com.coldlight.payment_provider.model.Transaction;
import com.coldlight.payment_provider.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@Transactional
public class TransactionRestControllerV1 {
    private final TransactionService transactionService;

    @PostMapping("/top-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Transaction> createTransaction(@RequestBody Transaction request,
                                               @RequestHeader("Authorization") String authorizationHeader) {
        // Проверяем авторизацию
        checkAuthorization(authorizationHeader);

        // Вызываем сервисный метод для создания транзакции
        return transactionService.deposit(request)
                .map(transaction -> new Transaction());
    }

    @GetMapping("/payments/transaction/list")
    public Mono<ResponseEntity<List<Transaction>>> getTransactionList() {
        return transactionService.getAllTransactions()
                .collectList()
                .flatMap(transactions -> {
                    if (transactions.isEmpty()) {
                        return Mono.just(ResponseEntity.notFound().build());
                    } else {
                        return Mono.just(ResponseEntity.ok(transactions));
                    }
                });
    }

    @GetMapping("/payments/transaction/list-by-date")
    public Mono<ResponseEntity<List<Transaction>>> getTransactionsByDateRange(
            @RequestParam("start_date") long startDate,
            @RequestParam("end_date") long endDate) {
        return transactionService.getTransactionsByDateRange(startDate, endDate)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{TransactionId}/details")
    public Mono<ResponseEntity<Transaction>> getTransactionDetails(@PathVariable("TransactionId") Long id) {
        return transactionService.getTransactionDetailsById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/payments/payout")
    public Mono<ResponseEntity<Transaction>> makePayout(@RequestBody Transaction request) {
        return transactionService.withdraw(request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/payments/payout/{PayoutId}/details")
    public Mono<ResponseEntity<Transaction>> getPayoutDetails(@PathVariable("PayoutId") Long payoutId) {
        return transactionService.getTransactionDetailsById(payoutId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/payments/payout/list-by-date")
    public Mono<ResponseEntity<List<Transaction>>> getPayoutByDateRange(
            @RequestParam("start_date") long startDate,
            @RequestParam("end_date") long endDate) {
        return transactionService.getTransactionsByDateRange(startDate, endDate)
                .collectList()
                .map(ResponseEntity::ok);
    }


    private void checkAuthorization(String authorizationHeader) {
        // Проверка авторизации
        // Реализуйте код для проверки авторизации, используя заголовок Authorization
        // и расшифровку Base64-кодирования для извлечения учетных данных
    }
}
