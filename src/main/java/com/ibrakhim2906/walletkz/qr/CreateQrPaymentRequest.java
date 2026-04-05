package com.ibrakhim2906.walletkz.qr;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateQrPaymentRequest (
        @NotNull Long walletId,
        @NotNull BigDecimal amount,
        String note,
        @NotNull LocalDateTime expiresAt
) { }
