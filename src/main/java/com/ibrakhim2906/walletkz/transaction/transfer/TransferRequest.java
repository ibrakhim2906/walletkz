package com.ibrakhim2906.walletkz.transaction.transfer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull Long sourceWalletId,
        @NotNull Long targetWalletId,
        @DecimalMin(value = "0.01") @NotNull BigDecimal amount,
        @Size(max=255) String description
) { }
