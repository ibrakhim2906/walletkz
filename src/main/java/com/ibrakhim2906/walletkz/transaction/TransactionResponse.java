package com.ibrakhim2906.walletkz.transaction;

import com.ibrakhim2906.walletkz.wallet.CurrencyEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponse (
        @NotNull Long id,
        @NotNull String referenceId,
        @NotNull Long sourceWalletId,
        Long targetWalletId,
        @NotNull TransactionType type,
        @NotNull BigDecimal sourceAmount,
        @NotNull BigDecimal targetAmount,
        @NotNull CurrencyEnum sourceCurrency,
        @NotNull CurrencyEnum targetCurrency,
        @NotNull BigDecimal exchangeRate,
        @NotNull TransactionStatus status,
        @NotNull String description,
        @NotNull LocalDateTime createdAt
        ) {

        public static TransactionResponse toResponse(Transaction transaction) {
                return TransactionResponse.builder()
                        .id(transaction.getId())
                        .referenceId(transaction.getReferenceId())
                        .sourceWalletId(transaction.getSourceWalletId())
                        .targetWalletId(transaction.getTargetWalletId() != null ?
                                transaction.getTargetWalletId() : null)
                        .type(transaction.getType())
                        .sourceAmount(transaction.getSourceAmount())
                        .sourceCurrency(transaction.getSourceCurrency())
                        .targetAmount(transaction.getTargetAmount()!=null ?
                                transaction.getTargetAmount() : null)
                        .targetCurrency(transaction.getTargetCurrency()!=null ?
                                transaction.getTargetCurrency() : null)
                        .exchangeRate(transaction.getExchangeRate()!=null ?
                                transaction.getExchangeRate() : null)
                        .status(transaction.getStatus())
                        .description(transaction.getDescription())
                        .createdAt(transaction.getCreatedAt())
                        .build();
        }
}
