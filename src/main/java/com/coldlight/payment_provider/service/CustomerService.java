package com.coldlight.payment_provider.service;

import com.coldlight.payment_provider.model.Customer;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<Customer> getCustomerById(Long id);

    Mono<Long> getAccountBalance(Long id);
}
