package com.coldlight.payment_provider.service.impl;

import com.coldlight.payment_provider.model.Wallet;
import com.coldlight.payment_provider.repository.WalletRepository;
import com.coldlight.payment_provider.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Mono<Wallet> getWalletById(Long id) {
        return walletRepository.findById(id);
    }
    @Override
    public Mono<Long> getAccountBalance(Long walletId) {
        return walletRepository.findById(walletId)
                .map(Wallet::getBalance)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Wallet not found")));
    }
}
