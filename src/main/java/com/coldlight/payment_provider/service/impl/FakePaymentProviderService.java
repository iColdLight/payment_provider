package com.coldlight.payment_provider.service.impl;

import com.coldlight.payment_provider.model.Merchant;
import com.coldlight.payment_provider.model.Transaction;
import com.coldlight.payment_provider.model.Webhook;
import com.coldlight.payment_provider.service.PaymentProviderAPI;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FakePaymentProviderService implements PaymentProviderAPI {

    public Mono<String> createTransaction(Transaction request) {
        // Реализуйте код для взаимодействия с фейковым платежным провайдером
        // и создания транзакции
        // Возвращаем Mono с идентификатором созданной транзакции
        return null;
    }

    @Override
    public Mono<Void> sendWebhook(Webhook webhook) {
        return null;
    }

    @Override
    public Mono<Merchant> getMerchantBalance() {
        return null;
    }
}
