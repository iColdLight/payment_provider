package com.coldlight.payment_provider.service;

import com.coldlight.payment_provider.model.Wallet;
import reactor.core.publisher.Mono;

public interface WalletService {

    Mono<Wallet> getWalletById(Long id);
    Mono<Long> getAccountBalance(Long id);
    Mono<Wallet> getWalletByIdForUpdate(Long walletId);
}
