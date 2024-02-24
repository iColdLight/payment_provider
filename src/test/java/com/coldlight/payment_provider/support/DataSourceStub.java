package com.coldlight.payment_provider.support;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.SQLException;

@TestConfiguration
public class DataSourceStub {
    private static final String CLEAN_FORMAT = "DROP SCHEMA IF EXISTS %s CASCADE; CREATE SCHEMA %s;";
    private static final String CLEAN_PUBLIC = "DROP SCHEMA IF EXISTS public CASCADE; CREATE SCHEMA public;";
    private static final String SELECT_FORMAT = "ALTER USER %s SET search_path to %s;";

    private final static PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:10")
            .withDatabaseName("paymentprovider")
            .withCommand("postgres -c max_connections=300");

    static {
        POSTGRES.start();
    }

    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String schema;

    @Value("${spring.flyway.table}")
    private String table;

    @Value("${spring.flyway.schemas}")
    private String schemas;

    /*@Primary
    @Bean
    public ConnectionFactory connectionFactory() {
        PostgresqlConnectionConfiguration configuration = PostgresqlConnectionConfiguration.builder()
                .host(POSTGRES.getHost())
                .port(POSTGRES.getFirstMappedPort())
                .database(POSTGRES.getDatabaseName())
                .username(POSTGRES.getUsername())
                .password(POSTGRES.getPassword())
                .build();

        ConnectionFactory connectionFactory = new PostgresqlConnectionFactory(configuration);

        try (Connection connection = connectionFactory.create().block()) {
            connection.createStatement(CLEAN_PUBLIC).execute();
            connection.createStatement(String.format(CLEAN_FORMAT, schema, schema)).execute();
            connection.createStatement(String.format(SELECT_FORMAT, POSTGRES.getUsername(), schema)).execute();
        } catch (SQLException e) {
            // Обработка исключения
        }

        Flyway.configure()
                .dataSource(connectionFactory)
                .schemas(schemas)
                .table(table)
                .load()
                .migrate();

        return connectionFactory;
    }*/
}

