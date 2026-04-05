package com.ibrakhim2906.walletkz.transaction.transfer;

import com.ibrakhim2906.walletkz.transaction.TransactionStatus;
import com.ibrakhim2906.walletkz.wallet.CurrencyEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferResponse (
        String referenceId,
        BigDecimal sourceAmount,
        CurrencyEnum sourceCurrency,
        BigDecimal targetAmount,
        CurrencyEnum targetCurrency,
        BigDecimal exchangeRate,
        TransactionStatus status
        ) { }
