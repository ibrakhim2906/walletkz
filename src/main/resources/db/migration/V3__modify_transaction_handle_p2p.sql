ALTER TABLE transactions ADD COLUMN source_wallet_id BIGINT;
ALTER TABLE transactions ADD COLUMN target_wallet_id BIGINT;

ALTER TABLE transactions ADD CONSTRAINT fk_source_wallet_id
    FOREIGN KEY (source_wallet_id) REFERENCES wallets(id);

ALTER TABLE transactions ADD CONSTRAINT fk_target_wallet_id
    FOREIGN KEY (target_wallet_id) REFERENCES wallets(id);