package com.ibrakhim2906.walletkz.qr;

import com.ibrakhim2906.walletkz.wallet.CurrencyEnum;
import com.ibrakhim2906.walletkz.wallet.Wallet;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "qr_payment_requests")
public class QrPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "requester_wallet_id")
    private Wallet requesterWallet;

    @Column(name = "requester_wallet_id", insertable = false, updatable = false)
    private Long requesterWalletId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyEnum currency;

    @Column
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QrRequestStatus status;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column
    private LocalDateTime paidAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static QrPayment create(String token, Wallet requesterWallet, BigDecimal amount, CurrencyEnum currency,
                                   String note, QrRequestStatus status, LocalDateTime expiresAt) {

        QrPayment qrPayment = new QrPayment();

        qrPayment.setToken(token);
        qrPayment.setRequesterWallet(requesterWallet);
        qrPayment.setAmount(amount);
        qrPayment.setCurrency(currency);
        qrPayment.setNote(note.isEmpty() ? null : note);
        qrPayment.setStatus(status);
        qrPayment.setExpiresAt(expiresAt);

        return qrPayment;
    }





}
