package com.ibrakhim2906.walletkz.qr;

import com.ibrakhim2906.walletkz.common.util.QrImageGenerator;
import com.ibrakhim2906.walletkz.wallet.CurrencyEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record QrPaymentResponse(
        @NotNull String token,
        @NotNull Long walletId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        @NotNull CurrencyEnum currency,
        @NotNull QrRequestStatus status,
        @NotNull LocalDateTime expiresAt,
        @NotNull String qrContent,
        @NotNull String qrImage
        ) {

    public static QrPaymentResponse toResponse(QrPayment qrPayment) {

        String qrContent = "walletkz://pay?token=" + qrPayment.getToken();

        String qrImage = QrImageGenerator.generateBase64Qr(qrContent);


        return new QrPaymentResponse(
                qrPayment.getToken(),
                qrPayment.getRequesterWalletId(),
                qrPayment.getAmount(),
                qrPayment.getCurrency(),
                qrPayment.getStatus(),
                qrPayment.getExpiresAt(),
                qrContent,
                qrImage
        );
    }

}
