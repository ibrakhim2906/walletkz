package com.ibrakhim2906.walletkz.qr;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateQrPaymentRequest (
        @NotNull Long walletId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        String note,
        @NotNull LocalDateTime expiresAt
) { }
