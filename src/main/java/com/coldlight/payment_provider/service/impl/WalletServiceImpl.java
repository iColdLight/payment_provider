package com.coldlight.payment_provider.service.impl;

import com.coldlight.payment_provider.model.Currency;
import com.coldlight.payment_provider.model.Wallet;
import com.coldlight.payment_provider.repository.WalletRepository;
import com.coldlight.payment_provider.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    private final DatabaseClient databaseClient;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, DatabaseClient databaseClient) {
        this.walletRepository = walletRepository;
        this.databaseClient = databaseClient;
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
    @Override
    public Mono<Wallet> getWalletByIdForUpdate(Long walletId) {
        String query = "SELECT * FROM wallet WHERE id = :walletId FOR UPDATE";
        return databaseClient
                .sql(query)
                .bind("walletId", walletId)
                .map((row, metadata) -> {
                    Long id = row.get("id", Long.class);
                    Long merchantId = row.get("merchant_id", Long.class);
                    Long balance = row.get("balance", Long.class);
                    Currency currency = row.get("currency", Currency.class);

                    Wallet wallet = new Wallet();
                    wallet.setId(id);
                    wallet.setMerchantId(merchantId);
                    wallet.setBalance(balance);
                    wallet.setCurrency(currency);

                    return wallet;
                })
                .one();
    }
}
