package com.ibrakhim2906.walletkz.transaction.transfer;

import com.ibrakhim2906.walletkz.transaction.TransactionStatus;
import com.ibrakhim2906.walletkz.wallet.CurrencyEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferResponse (
        @NotNull String referenceId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal sourceAmount,
        @NotNull CurrencyEnum sourceCurrency,
        @NotNull BigDecimal targetAmount,
        @NotNull CurrencyEnum targetCurrency,
        @NotNull BigDecimal exchangeRate,
        @NotNull TransactionStatus status
        ) { }
