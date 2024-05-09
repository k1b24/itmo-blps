DROP TABLE IF EXISTS bank_users_data CASCADE;
DROP TABLE IF EXISTS transaction_status CASCADE;

CREATE TABLE bank_users_data (
    user_id                 UUID PRIMARY KEY,
    credit_card_number      TEXT,
    expiration_date         TEXT,
    cvv_code                TEXT,
    balance                 NUMERIC(10, 2)
);

CREATE TABLE transaction_status (
    id          UUID PRIMARY KEY,
    user_id     UUID,
    status      TEXT,
    amount      NUMERIC(10, 2),
    constraint user_id_fk FOREIGN KEY (user_id) REFERENCES bank_users_data(user_id)
);

INSERT INTO bank_users_data (user_id, credit_card_number, expiration_date, cvv_code, balance)
    VALUES ('ce7b7644-fce0-4204-81c3-ad5a6387effe','1234 5678 9101 1121', '12/30', '123', 50800.00);