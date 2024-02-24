package com.coldlight.payment_provider.config;

import com.coldlight.payment_provider.repository.TransactionRepository;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

@Configuration
public class R2dbcConfiguration {

    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }

    /*@Bean
    @DependsOn("r2dbcEntityTemplate")
    public TransactionRepository transactionRepository(R2dbcEntityTemplate r2dbcEntityTemplate) {
        return null;
    }*/
}
