package com.coldlight.payment_provider.rest;

import com.coldlight.payment_provider.PaymentProviderApplication;
import com.coldlight.payment_provider.support.DataSourceStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringJUnitConfig
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
@SpringBootTest(classes = PaymentProviderApplication.class)
@ContextConfiguration(classes = {DataSourceStub.class, TransactionRestControllerV1.class})
@Import(value = ObjectMapper.class)
public class TransactionRestControllerV1Test {

    @Autowired
    private TransactionRestControllerV1 transactionRestControllerV1;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Sql({"/mock_data.sql"})
    public void getTransactionDetails() throws Exception {
        webTestClient.get()
                .uri("/api/v1/transaction/1/details")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.amount").isEqualTo(100);
    }
}
