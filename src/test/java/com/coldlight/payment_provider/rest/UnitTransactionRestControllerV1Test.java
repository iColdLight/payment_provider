package com.coldlight.payment_provider.rest;

import com.coldlight.payment_provider.model.*;
import com.coldlight.payment_provider.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@WebFluxTest(TransactionRestControllerV1.class)
public class UnitTransactionRestControllerV1Test {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void testCreateTransaction() {
        Transaction request = new Transaction();
        request.setPaymentMethod(PaymentMethod.CARD);
        request.setTransactionType(TransactionType.TOP_UP);
        request.setAmount(100L);
        request.setCurrency(Currency.USD);

        Transaction createdTransaction = new Transaction();
        createdTransaction.setId(1L);
        createdTransaction.setPaymentMethod(request.getPaymentMethod());
        createdTransaction.setTransactionType(request.getTransactionType());
        createdTransaction.setAmount(request.getAmount());
        createdTransaction.setCurrency(request.getCurrency());

        when(transactionService.deposit(request)).thenReturn(Mono.just(createdTransaction));

        webTestClient.post()
                .uri("/api/v1/top-up")
                .header(HttpHeaders.AUTHORIZATION, "Bearer your-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Transaction.class)
                .value(response -> {
                    assertEquals(createdTransaction.getPaymentMethod(), response.getPaymentMethod());
                    assertEquals(createdTransaction.getTransactionType(), response.getTransactionType());
                    assertEquals(createdTransaction.getAmount(), response.getAmount());
                    assertEquals(createdTransaction.getCurrency(), response.getCurrency());
                });
    }

    @Test
    public void testCreateTransactionFailed() {
        Transaction request = new Transaction();
        request.setPaymentMethod(PaymentMethod.CRYPTO);
        request.setTransactionType(TransactionType.TOP_UP);
        request.setAmount(100L);
        request.setCurrency(Currency.USD);

        Transaction createdTransaction = new Transaction();
        createdTransaction.setId(2L);
        createdTransaction.setPaymentMethod(request.getPaymentMethod());
        createdTransaction.setTransactionType(request.getTransactionType());
        createdTransaction.setAmount(request.getAmount());
        createdTransaction.setCurrency(request.getCurrency());

        when(transactionService.deposit(request)).thenReturn(Mono.error(new RuntimeException("Server Error")));

        webTestClient.post()
                .uri("/api/v1/top-up")
                .header(HttpHeaders.AUTHORIZATION, "Bearer your-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    public void testGetTransactionList() {
        Transaction t1 = Transaction.builder().paymentMethod(PaymentMethod.CARD).amount(100L).currency(Currency.USD).build();
        Transaction t2 = Transaction.builder().paymentMethod(PaymentMethod.CASH).amount(200L).currency(Currency.BRL).build();
        Transaction t3 = Transaction.builder().paymentMethod(PaymentMethod.CRYPTO).amount(300L).currency(Currency.KZT).build();

        List<Transaction> transactions = Arrays.asList(t1, t2, t3);

        when(transactionService.getAllTransactions()).thenReturn(Flux.fromIterable(transactions));

        webTestClient.get()
                .uri("/api/v1/payments/transaction/list")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Transaction.class)
                .isEqualTo(transactions);
    }

    @Test
    public void testGetTransactionListNotFound() {
        when(transactionService.getAllTransactions()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/v1/payments/transaction/list")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testGetTransactionsByDateRange() {
        Transaction t1 = Transaction.builder().amount(100L).createdAt(LocalDateTime.of(2023,1,21,0,0)).build();
        Transaction t2 = Transaction.builder().amount(200L).createdAt(LocalDateTime.of(2023, 3,15,0,0)).build();
        Transaction t3 = Transaction.builder().amount(300L).createdAt(LocalDateTime.of(2023,9,19,0,0)).build();

        List<Transaction> transactions = Arrays.asList(t1, t2, t3);

        long startDateEpochSeconds = LocalDateTime.of(2023, 1, 1, 0, 0).atZone(ZoneId.systemDefault()).toEpochSecond();
        long endDateEpochSeconds = LocalDateTime.of(2023, 12, 30, 0, 0).atZone(ZoneId.systemDefault()).toEpochSecond();

        when(transactionService.getTransactionsByDateRange(startDateEpochSeconds, endDateEpochSeconds))
                .thenReturn(Flux.fromIterable(transactions));

        webTestClient.get()
                .uri("/api/v1/payments/transaction/list-by-date?start_date=" + startDateEpochSeconds + "&end_date=" + endDateEpochSeconds)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Transaction.class)
                .isEqualTo(transactions);
    }

    @Test
    public void testGetTransactionDetails() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setPaymentMethod(PaymentMethod.CARD);
        transaction.setTransactionType(TransactionType.TOP_UP);
        transaction.setAmount(100L);
        transaction.setCurrency(Currency.USD);
        transaction.setExternalTransactionId(1254896L);
        transaction.setCreatedAt(LocalDateTime.of(2022,5,6,0,0));
        transaction.setUpdatedAt(LocalDateTime.of(2022,6,8,0,0));
        transaction.setCardId(21L);
        transaction.setLanguage(Language.en);
        transaction.setCustomerId(23L);
        transaction.setWalletId(24L);
        transaction.setNotificationUrl("URL");

        when(transactionService.getTransactionDetailsById(1L))
                .thenReturn(Mono.justOrEmpty(transaction));

        webTestClient.get()
                .uri("/api/v1/transaction/1/details")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Transaction.class)
                .isEqualTo(transaction);
    }

    @Test
    public void testGetTransactionDetailsNotFound() {
        when(transactionService.getTransactionDetailsById(15L))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/transaction/15/details")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testMakePayout() {
        Transaction payoutTransaction = new Transaction();
        payoutTransaction.setAmount(100L);
        payoutTransaction.setTransactionType(TransactionType.PAY_OUT);
        payoutTransaction.setCurrency(Currency.USD);
        payoutTransaction.setPaymentMethod(PaymentMethod.CARD);

        when(transactionService.withdraw(payoutTransaction))
                .thenReturn(Mono.just(payoutTransaction));

        webTestClient.post()
                .uri("/api/v1/payments/payout")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payoutTransaction)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Transaction.class)
                .isEqualTo(payoutTransaction);
    }

    @Test
    public void testMakePayoutFailed() {
        Transaction payoutTransaction = new Transaction();
        payoutTransaction.setAmount(100L);
        payoutTransaction.setTransactionType(TransactionType.PAY_OUT);
        payoutTransaction.setCurrency(Currency.USD);
        payoutTransaction.setPaymentMethod(PaymentMethod.CARD);

        when(transactionService.withdraw(payoutTransaction))
                .thenReturn(Mono.error(new RuntimeException("Payout failed. Server error")));

        webTestClient.post()
                .uri("/api/v1/payments/payout")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payoutTransaction)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    public void testGetPayoutDetails() {
        Transaction payoutTransaction = new Transaction();
        payoutTransaction.setId(1L);
        payoutTransaction.setPaymentMethod(PaymentMethod.CARD);
        payoutTransaction.setTransactionType(TransactionType.PAY_OUT);
        payoutTransaction.setAmount(100L);
        payoutTransaction.setCurrency(Currency.USD);
        payoutTransaction.setExternalTransactionId(1254896L);
        payoutTransaction.setCreatedAt(LocalDateTime.of(2022,5,6,0,0));
        payoutTransaction.setUpdatedAt(LocalDateTime.of(2022,6,8,0,0));
        payoutTransaction.setCardId(21L);
        payoutTransaction.setLanguage(Language.en);
        payoutTransaction.setCustomerId(23L);
        payoutTransaction.setWalletId(24L);
        payoutTransaction.setNotificationUrl("URL");

        when(transactionService.getTransactionDetailsById(1L))
                .thenReturn(Mono.just(payoutTransaction));

        webTestClient.get()
                .uri("/api/v1/payments/payout/1/details")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Transaction.class)
                .isEqualTo(payoutTransaction);
    }

    @Test
    public void testGetPayoutDetailsNotFound() {
        when(transactionService.getTransactionDetailsById(10L))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/payments/payout/10/details")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testGetPayoutByDateRange() {
        Transaction t1 = Transaction.builder().amount(100L).createdAt(LocalDateTime.of(2023,1,21,0,0)).build();
        Transaction t2 = Transaction.builder().amount(200L).createdAt(LocalDateTime.of(2023, 3,15,0,0)).build();
        Transaction t3 = Transaction.builder().amount(300L).createdAt(LocalDateTime.of(2023,9,19,0,0)).build();

        List<Transaction> transactions = Arrays.asList(t1, t2, t3);

        long startDateEpochSeconds = LocalDateTime.of(2023, 1, 1, 0, 0).atZone(ZoneId.systemDefault()).toEpochSecond();
        long endDateEpochSeconds = LocalDateTime.of(2023, 12, 30, 0, 0).atZone(ZoneId.systemDefault()).toEpochSecond();

        when(transactionService.getTransactionsByDateRange(startDateEpochSeconds, endDateEpochSeconds))
                .thenReturn(Flux.fromIterable(transactions));

        webTestClient.get()
                .uri("/api/v1/payments/payout/list-by-date?start_date=" + startDateEpochSeconds + "&end_date=" + endDateEpochSeconds)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Transaction.class)
                .isEqualTo(transactions);
    }


}
