package com.ibrakhim2906.walletkz.transaction.transfer;

import com.ibrakhim2906.walletkz.transaction.TransactionStatus;
import com.ibrakhim2906.walletkz.wallet.CurrencyEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferResponse (
        @NotNull String referenceId,
        @DecimalMin(value = "0.01") @NotNull BigDecimal amount,
        @NotNull CurrencyEnum currency,
        @NotNull TransactionStatus status
        ) { }
