package com.ibrakhim2906.walletkz.qr;

import com.ibrakhim2906.walletkz.common.util.QrTokenGenerator;
import com.ibrakhim2906.walletkz.transaction.transfer.TransferRequest;
import com.ibrakhim2906.walletkz.transaction.transfer.TransferResponse;
import com.ibrakhim2906.walletkz.transaction.transfer.TransferService;
import com.ibrakhim2906.walletkz.user.CurrentUserService;
import com.ibrakhim2906.walletkz.user.User;
import com.ibrakhim2906.walletkz.wallet.Wallet;
import com.ibrakhim2906.walletkz.wallet.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class QrService {

    private final QrPaymentRepository qrRepo;
    private final WalletRepository walletRepo;
    private final CurrentUserService userService;
    private final TransferService transferService;

    public QrService(QrPaymentRepository qrRepo, WalletRepository walletRepo,
                     CurrentUserService userService, TransferService transferService) {
        this.qrRepo = qrRepo;
        this.walletRepo = walletRepo;
        this.userService = userService;
        this.transferService = transferService;
    }

    @Transactional
    public QrPaymentResponse createRequest(CreateQrPaymentRequest req) {

        User currentUser = userService.getCurrentUser();

        Wallet wallet = walletRepo.findByUser_IdAndId(currentUser.getId(),
                req.walletId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wallet not found"));

        if (req.amount().compareTo(BigDecimal.ZERO)<=0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount cannot be less or equal zero");
        }

        if (!req.expiresAt().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "expiring time should be in future");
        }

        String qrToken = QrTokenGenerator.generateToken();

        QrPayment qrPayment = QrPayment.create(qrToken, wallet,
                req.amount(), wallet.getCurrency(),
                req.note(), QrRequestStatus.ACTIVE,
                req.expiresAt());

        qrRepo.save(qrPayment);

        return QrPaymentResponse.toResponse(qrPayment);
    }

    public QrPaymentResponse findByToken(String token) {

        User currentUser = userService.getCurrentUser();

        QrPayment qrPayment = qrRepo.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "qr payment is not found"));

        if (qrPayment.getStatus()==QrRequestStatus.ACTIVE && isExpired(qrPayment)) {
            qrPayment.setStatus(QrRequestStatus.EXPIRED);
            qrPayment = qrRepo.save(qrPayment);
        }

        return QrPaymentResponse.toResponse(qrPayment);
    }

    @Transactional
    public TransferResponse payQr(PayQrPaymentRequest req) {

        User currentUser = userService.getCurrentUser();

        QrPayment qrPayment = qrRepo.findByTokenForUpdate(req.token())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "qr payment is not found"));

        if (qrPayment.getStatus()!=QrRequestStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "qr payment is not active");
        }

        if (isExpired(qrPayment)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "qr payment is expired");
        }

        Wallet payerWallet = walletRepo.findByUser_IdAndId(
                        currentUser.getId(),
                        req.sourceWalletId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "source wallet is not found"));

        Wallet requesterWallet = qrPayment.getRequesterWallet();

        if (payerWallet.getId()==requesterWallet.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot be from the same wallet");

        }

        TransferRequest transferRequest = new TransferRequest(
                payerWallet.getId(),
                requesterWallet.getId(),
                qrPayment.getAmount(),
                qrPayment.getNote()
        );

        TransferResponse transferResponse = transferService.transfer(transferRequest);

        qrPayment.setStatus(QrRequestStatus.PAID);
        qrPayment.setPaidAt(LocalDateTime.now());

        qrRepo.save(qrPayment);

        return transferResponse;
    }


    private boolean isExpired(QrPayment qrPayment) {
        return qrPayment.getExpiresAt().isBefore(LocalDateTime.now());
    }
}
