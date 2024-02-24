package com.coldlight.payment_provider.repository;

import com.coldlight.payment_provider.model.Customer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories
public interface CustomerRepository extends R2dbcRepository<Customer, Long> {
}
