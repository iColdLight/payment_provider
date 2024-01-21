package com.coldlight.payment_provider.service;

import com.coldlight.payment_provider.model.Merchant;
import com.coldlight.payment_provider.model.Transaction;
import com.coldlight.payment_provider.model.Webhook;
import reactor.core.publisher.Mono;

public interface PaymentProviderAPI {
    Mono<String> createTransaction(Transaction transaction);
    Mono<Void> sendWebhook(Webhook webhook);
    Mono<Merchant> getMerchantBalance();
}
