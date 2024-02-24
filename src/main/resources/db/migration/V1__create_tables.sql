create SCHEMA IF NOT EXISTS paymentprovider;
CREATE SEQUENCE paymentprovider.hibernate_sequence START 1 INCREMENT 1;
create table if not exists paymentprovider.customer (
    id BIGINT NOT NULL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    balance BIGINT,
    email VARCHAR(255)
);
create table if not exists paymentprovider.merchant (
    id BIGINT NOT NULL PRIMARY KEY,
    merchant_name VARCHAR(255),
    contact VARCHAR(255)
);
create table if not exists paymentprovider.transaction (
    id BIGINT NOT NULL PRIMARY KEY,
    payment_method VARCHAR(255),
    transaction_type VARCHAR(255),
    transaction_status VARCHAR(255),
    amount BIGINT,
    currency VARCHAR(255),
    external_transaction_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    card_id BIGINT NOT NULL,
    transaction_language VARCHAR(255),
    customer_id BIGINT NOT NULL,
    wallet_id BIGINT NOT NULL,
    notification_url VARCHAR(255),
    FOREIGN KEY (card_id) REFERENCES paymentprovider.card_data (id),
    FOREIGN KEY (customer_id) REFERENCES paymentprovider.customer (id),
    FOREIGN KEY (wallet_id) REFERENCES paymentprovider.wallet(id)
);
create table if not exists paymentprovider.card_data (
    id BIGINT NOT NULL PRIMARY KEY,
    card_number SMALLINT,
    exp_date SMALLINT,
    cvv SMALLINT
);
create table if not exists paymentprovider.webhook (
    id BIGINT NOT NULL PRIMARY KEY,
    webhook_event_type VARCHAR(255),
    url VARCHAR(255),
    retry_counter BIGINT,
    request_body VARCHAR(255),
    response_status BIGINT NOT NULL,
    response_body VARCHAR(255)
);
create table if not exists paymentprovider.wallet (
    id BIGINT NOT NULL PRIMARY KEY,
    merchant_id BIGINT NOT NULL,
    balance BIGINT,
    currency VARCHAR(255),
    FOREIGN KEY (merchant_id) REFERENCES paymentprovider.merchant (id)
);

