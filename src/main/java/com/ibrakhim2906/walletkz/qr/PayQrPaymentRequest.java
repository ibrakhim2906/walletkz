package com.ibrakhim2906.walletkz.qr;

import jakarta.validation.constraints.NotNull;

public record PayQrPaymentRequest(
        @NotNull String token,
        @NotNull Long sourceWalletId
) { }
