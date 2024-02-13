package com.coldlight.payment_provider.repository;

import com.coldlight.payment_provider.model.Customer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CustomerRepository extends R2dbcRepository<Customer, Long> {
}
