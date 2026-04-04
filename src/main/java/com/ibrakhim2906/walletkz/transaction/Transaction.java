package com.ibrakhim2906.walletkz.transaction;

import com.ibrakhim2906.walletkz.wallet.CurrencyEnum;
import com.ibrakhim2906.walletkz.wallet.Wallet;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String referenceId;

    @ManyToOne
    @JoinColumn(name = "source_wallet_id")
    private Wallet sourceWallet;

    @ManyToOne
    @JoinColumn(name = "target_wallet_id")
    private Wallet targetWallet;

    @Column(name = "source_wallet_id", insertable = false, updatable = false)
    private Long sourceWalletId;

    @Column(name = "target_wallet_id", insertable = false, updatable = false)
    private Long targetWalletId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal sourceAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal targetAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyEnum sourceCurrency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyEnum targetCurrency;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal exchangeRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static Transaction create(String referenceId, Wallet wallet, BigDecimal amount,
                                     CurrencyEnum currency, TransactionType type, String description,
                                     TransactionStatus status) {
        Transaction transaction = new Transaction();

        transaction.setSourceWallet(wallet);
        transaction.setSourceAmount(amount);
        transaction.setType(type);
        transaction.setDescription(description);
        transaction.setStatus(status);
        transaction.setReferenceId(referenceId);
        transaction.setSourceCurrency(currency);

        return transaction;
    }

    public static Transaction create(String referenceId, Wallet sourceWallet, Wallet targetWallet, BigDecimal sourceAmount,
                                     BigDecimal targetAmount, CurrencyEnum sourceCurrency, CurrencyEnum targetCurrency
                                     ,TransactionType type, String description,
                                     TransactionStatus status) {

        Transaction transaction = new Transaction();

        transaction.setSourceWallet(sourceWallet);
        transaction.setTargetWallet(targetWallet);
        transaction.setSourceAmount(sourceAmount);
        transaction.setTargetAmount(targetAmount);
        transaction.setSourceCurrency(sourceCurrency);
        transaction.setTargetCurrency(targetCurrency);
        transaction.setType(type);
        transaction.setDescription(description);
        transaction.setStatus(status);
        transaction.setReferenceId(referenceId);

        return transaction;
    }


}
