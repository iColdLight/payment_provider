package com.coldlight.payment_provider.service.impl;

import com.coldlight.payment_provider.model.Customer;
import com.coldlight.payment_provider.repository.CustomerRepository;
import com.coldlight.payment_provider.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Mono<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }
    @Override
    public Mono<Long> getAccountBalance(Long customerId) {
        return customerRepository.findById(customerId)
                .map(Customer::getBalance)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found")));
    }
}
