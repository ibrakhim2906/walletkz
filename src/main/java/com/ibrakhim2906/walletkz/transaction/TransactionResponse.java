package com.ibrakhim2906.walletkz.transaction;

import com.ibrakhim2906.walletkz.wallet.CurrencyEnum;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse (
        @NotNull Long id,
        @NotNull Long referenceId,
        @NotNull Long sourceWalletId,
        @NotNull Long targetWalletId,
        @NotNull TransactionType type,
        @NotNull BigDecimal amount,
        @NotNull CurrencyEnum currency,
        @NotNull TransactionStatus status,
        @NotNull String description,
        @NotNull LocalDateTime createdAt
        ) { }
