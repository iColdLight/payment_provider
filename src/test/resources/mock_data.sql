INSERT INTO paymentprovider.customer (id, first_name, last_name, balance, email) VALUES
 (1, 'John', 'Doe', 1000, 'john.doe@example.com'),
 (2, 'Jane', 'Smith', 500, 'jane.smith@example.com'),
 (3, 'David', 'Johnson', 2000, 'david.johnson@example.com');

INSERT INTO paymentprovider.merchant (id, merchant_name, contact) VALUES
(1, 'Merchant A', 'contact@merchanta.com'),
(2, 'Merchant B', 'contact@merchantb.com'),
(3, 'Merchant C', 'contact@merchantc.com');

INSERT INTO paymentprovider.transaction (id, payment_method, transaction_type, transaction_status, amount, currency,
external_transaction_id, created_at, updated_at, card_id, transaction_language, customer_id, wallet_id, notification_url) VALUES
(1, 'CARD', 'TOP_UP', 'SUCCESS', 100, 'USD', 12345, NOW(), NOW(), 1, 'en', 1, 1, 'http://example.com/notification'),
(2, 'CARD', 'TOP_UP', 'SUCCESS', 50, 'USD', 67890, NOW(), NOW(), 2, 'en', 2, 2, 'http://example.com/notification'),
(3, 'CARD', 'PAY_OUT', 'SUCCESS', 200, 'USD', 54321, NOW(), NOW(), 3, 'de', 3, 3, 'http://example.com/notification');

INSERT INTO paymentprovider.card_data (id, card_number, exp_date, cvv) VALUES
(1, 1234567890123456, 1224, 123),
(2, 9876543210987654, 1124, 456),
(3, 5555666677778888, 0325, 789);

INSERT INTO paymentprovider.wallet (id, merchant_id, balance, currency) VALUES
(1, 1, 5000, 'USD'),
(2, 2, 2000, 'USD'),
(3, 3, 10000, 'USD');

INSERT INTO paymentprovider.webhook (id, webhook_event_type, url, retry_counter, request_body, response_status, response_body) VALUES
(1, 'PAYMENT_SUCCESSFUL', 'http://example.com/webhook', 0, 'Request Body', 200, 'Response Body'),
(2, 'PAYMENT_SUCCESSFUL', 'http://example.com/webhook', 0, 'Request Body', 200, 'Response Body');
