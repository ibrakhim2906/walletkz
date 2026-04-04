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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CurrencyEnum currency;

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
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDescription(description);
        transaction.setStatus(status);
        transaction.setReferenceId(referenceId);
        transaction.setCurrency(currency);

        return transaction;
    }

    public static Transaction create(String referenceId, Wallet sourceWallet, Wallet targetWallet, BigDecimal amount,
                                     CurrencyEnum currency, TransactionType type, String description,
                                     TransactionStatus status) {
        Transaction transaction = new Transaction();

        transaction.setSourceWallet(sourceWallet);
        transaction.setTargetWallet(targetWallet);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDescription(description);
        transaction.setStatus(status);
        transaction.setReferenceId(referenceId);
        transaction.setCurrency(currency);

        return transaction;
    }


}
