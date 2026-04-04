package com.ibrakhim2906.walletkz.transaction.transfer;

import com.ibrakhim2906.walletkz.common.util.ReferenceIdGenerator;
import com.ibrakhim2906.walletkz.fx.FxService;
import com.ibrakhim2906.walletkz.transaction.Transaction;
import com.ibrakhim2906.walletkz.transaction.TransactionRepository;
import com.ibrakhim2906.walletkz.transaction.TransactionStatus;
import com.ibrakhim2906.walletkz.transaction.TransactionType;
import com.ibrakhim2906.walletkz.user.CurrentUserService;
import com.ibrakhim2906.walletkz.user.User;
import com.ibrakhim2906.walletkz.wallet.Wallet;
import com.ibrakhim2906.walletkz.wallet.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TransferService{

    private final WalletRepository walletRepo;

    private final TransactionRepository transactionRepo;

    private final CurrentUserService userService;

    private final FxService fxService;

    public TransferService(WalletRepository walletRepo,
                           TransactionRepository transactionRepo,
                           CurrentUserService userService,
                           FxService fxService) {
        this.walletRepo = walletRepo;
        this.transactionRepo = transactionRepo;
        this.userService = userService;
        this.fxService = fxService;
    }

    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        User currentUser = userService.getCurrentUser();

        Wallet sourceWallet = walletRepo.findByUser_IdAndId(currentUser.getId(),
                request.sourceWalletId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Source wallet not found"));

        Wallet targetWallet = walletRepo.findByIdForUpdate(request.targetWalletId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target wallet not found"));

        if (sourceWallet.getId().equals(targetWallet.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot transfer to same wallet");
        }

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive");
        }

        if (sourceWallet.getBalance().compareTo(request.amount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        BigDecimal rate = fxService.getRate(sourceWallet.getCurrency(), targetWallet.getCurrency());
        BigDecimal targetAmount = request.amount()
                .multiply(rate)
                .setScale(2, RoundingMode.HALF_UP);

        sourceWallet.setBalance(sourceWallet.getBalance().subtract(request.amount()));
        targetWallet.setBalance(targetWallet.getBalance().add(targetAmount));

        String referenceId = ReferenceIdGenerator.generateReference();

        Transaction transaction = new Transaction();
        transaction.setReferenceId(referenceId);
        transaction.setSourceWallet(sourceWallet);
        transaction.setTargetWallet(targetWallet);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setSourceAmount(request.amount());
        transaction.setTargetAmount(targetAmount);
        transaction.setSourceCurrency(sourceWallet.getCurrency());
        transaction.setTargetCurrency(targetWallet.getCurrency());
        transaction.setExchangeRate(rate);
        transaction.setDescription(request.description());

        transactionRepo.save(transaction);

        return new TransferResponse(
                referenceId,
                request.amount(),
                sourceWallet.getCurrency(),
                targetAmount,
                targetWallet.getCurrency(),
                rate,
                TransactionStatus.COMPLETED
        );
    }


}
