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