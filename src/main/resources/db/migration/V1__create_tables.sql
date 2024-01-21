create SCHEMA IF NOT EXISTS paymentprovider;
CREATE SEQUENCE paymentprovider.hibernate_sequence START 1 INCREMENT 1;
create table if not exists paymentprovider.customer (
    id BIGINT NOT NULL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255)
);
create table if not exists paymentprovider.merchant (
    id BIGINT NOT NULL PRIMARY KEY,
    merchant_name VARCHAR(255),
    contact VARCHAR(255),
    balance BIGINT
);
create table if not exists paymentprovider.transaction (
    id BIGINT NOT NULL PRIMARY KEY,
    payment_method VARCHAR(255),
    amount BIGINT,
    currency VARCHAR(255),
    external_transaction_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    card_id BIGINT NOT NULL,
    transaction_language VARCHAR(255),
    merchant_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    FOREIGN KEY (card_id) REFERENCES paymentprovider.card_data (id),
    FOREIGN KEY (merchant_id) REFERENCES paymentprovider.merchant (id),
    FOREIGN KEY (customer_id) REFERENCES paymentprovider.customer (id)
);
create table if not exists paymentprovider.card_data (
    id BIGINT NOT NULL PRIMARY KEY,
    card_number SMALLINT,
    exp_date SMALLINT,
    cvv SMALLINT
);
create table if not exists paymentprovider.webhook (
    id BIGINT NOT NULL,
    webhook_event_type VARCHAR(255),
    url VARCHAR(255)
)
