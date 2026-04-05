CREATE TABLE qr_payment_requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    token VARCHAR(100) NOT NULL UNIQUE,
    requester_wallet_id BIGINT NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    note VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    paid_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_qr_request_wallet
                FOREIGN KEY (requester_wallet_id) REFERENCES wallets(id)
);

