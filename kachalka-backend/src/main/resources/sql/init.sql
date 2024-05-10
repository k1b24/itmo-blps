DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS users_roles CASCADE;
DROP TABLE IF EXISTS certificates CASCADE;
DROP TABLE IF EXISTS certificates_to_users CASCADE;
DROP TABLE IF EXISTS certificates_transactions CASCADE;

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

INSERT INTO certificates (id, title, price, duration, start_at, end_at)
    VALUES ('c532033f-766c-44f3-aaf7-4e06f5505331', 'Кочалка', 123, 31536000000, '2024-03-13 00:00:00.000000 +00:00', '2025-03-13 00:00:00.000000 +00:00');

CREATE TABLE IF NOT EXISTS certificates_to_users (
    user_login      VARCHAR(128) NOT NULL,
    certificate_id  UUID NOT NULL,
    expires_at      TIMESTAMPTZ NOT NULL,
    is_active       BOOLEAN NOT NULL,
    CONSTRAINT certificate_id_fk
    FOREIGN KEY (certificate_id)
    REFERENCES certificates(id),
    CONSTRAINT certificates_to_users_pk
    PRIMARY KEY (user_login, certificate_id)
);

CREATE INDEX idx_is_active ON certificates_to_users USING btree(is_active);

CREATE TABLE certificates_transactions (
    transaction_id      UUID PRIMARY KEY,
    user_login          VARCHAR(128),
    user_email          VARCHAR(128),
    certificate_id      UUID,
    transaction_status  TEXT,
    CONSTRAINT certificate_id_fk
        FOREIGN KEY (certificate_id)
            REFERENCES certificates(id)
);