ALTER TABLE transactions
    ADD COLUMN source_amount NUMERIC(19,2),
    ADD COLUMN target_amount NUMERIC(19,2),
    ADD COLUMN source_currency VARCHAR(10),
    ADD COLUMN target_currency VARCHAR(10),
    ADD COLUMN exchange_rate NUMERIC(19,6),
    DROP COLUMN amount,
    DROP COLUMN currency;