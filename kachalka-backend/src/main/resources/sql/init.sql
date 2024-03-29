DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS certificates CASCADE;
DROP TABLE IF EXISTS certificates_to_users CASCADE;
DROP TABLE IF EXISTS certificates_transactions CASCADE;

CREATE TABLE IF NOT EXISTS users (
    login           VARCHAR(128) PRIMARY KEY,
    password        VARCHAR(128) NOT NULL,
    fullname        VARCHAR(128) NOT NULL,
    phone_number    VARCHAR(12) NOT NULL,
    email           VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS certificates (
    id              UUID PRIMARY KEY,
    title           VARCHAR(128) NOT NULL,
    price           FLOAT NOT NULL,
    duration        BIGINT NOT NULL,
    start_at        TIMESTAMPTZ NOT NULL,
    end_at          TIMESTAMPTZ NOT NULL,
    description     TEXT,
    picture         VARCHAR(128)
);

CREATE TABLE IF NOT EXISTS certificates_to_users (
    user_login      VARCHAR(128) NOT NULL,
    certificate_id  UUID NOT NULL,
    expires_at      TIMESTAMPTZ NOT NULL,
    is_active       BOOLEAN NOT NULL,
    CONSTRAINT user_login_fk
    FOREIGN KEY (user_login)
    REFERENCES users(login),
    CONSTRAINT certificate_id_fk
    FOREIGN KEY (certificate_id)
    REFERENCES certificates(id),
    CONSTRAINT certificates_to_users_pk
    PRIMARY KEY (user_login, certificate_id)
);

CREATE TABLE certificates_transactions (
    transaction_id      UUID PRIMARY KEY,
    user_login          VARCHAR(128),
    certificate_id      UUID,
    transaction_status  TEXT,
    CONSTRAINT certificate_id_fk
        FOREIGN KEY (certificate_id)
            REFERENCES certificates(id),
    CONSTRAINT user_login_fk
        FOREIGN KEY (user_login)
            REFERENCES users(login)
);