package com.ibrakhim2906.walletkz.transaction.transfer;

import com.ibrakhim2906.walletkz.common.util.ReferenceIdGenerator;
import com.ibrakhim2906.walletkz.transaction.Transaction;
import com.ibrakhim2906.walletkz.transaction.TransactionRepository;
import com.ibrakhim2906.walletkz.transaction.TransactionStatus;
import com.ibrakhim2906.walletkz.transaction.TransactionType;
import com.ibrakhim2906.walletkz.user.CurrentUserService;
import com.ibrakhim2906.walletkz.wallet.Wallet;
import com.ibrakhim2906.walletkz.wallet.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class TransferService{

    private final WalletRepository walletRepo;

    private final TransactionRepository transactionRepo;

    private final CurrentUserService userService;

    public TransferService(WalletRepository walletRepo,
                           TransactionRepository transactionRepo,
                           CurrentUserService userService) {
        this.walletRepo = walletRepo;
        this.transactionRepo = transactionRepo;
        this.userService = userService;
    }

    @Transactional
    public TransferResponse transfer(TransferRequest req) {

        // while using Pessimistic Lock on db data retrieval introduces consistency to the data given to the method
        // deadlock situation possibility occurs
        // such issue will be addressed by first retrieving smaller id wallet and then larger id wallet

        if (req.amount().compareTo(BigDecimal.ZERO)<=0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transferred amount cannot be less or equal zero");
        }

        Long minId = Math.min(req.sourceWallet().getId(), req.targetWallet().getId());
        Long maxId = Math.max(req.sourceWallet().getId(), req.targetWallet().getId());

        Wallet minIdWallet = walletRepo.findByIdForUpdate(minId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Source wallet not found"));

        Wallet maxIdWallet = walletRepo.findByIdForUpdate(maxId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target wallet not found"));

        Wallet sourceWallet = req.sourceWallet().getId().equals(minId) ? minIdWallet : maxIdWallet;
        Wallet targetWallet = req.sourceWallet().getId().equals(minId) ? maxIdWallet : minIdWallet;

        if (sourceWallet.getId().equals(targetWallet.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfers to the same wallet are not possible");
        }

        if (!sourceWallet.getCurrency().equals(targetWallet.getCurrency())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both wallets should be same currency");
        }

        if (sourceWallet.getBalance().compareTo(req.amount())<0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        sourceWallet.setBalance(sourceWallet.getBalance().subtract(req.amount()));
        targetWallet.setBalance(targetWallet.getBalance().add(req.amount()));

        String referenceId = ReferenceIdGenerator.generateReference();

        Transaction transaction = Transaction.create(
                referenceId,
                sourceWallet,
                targetWallet,
                req.amount(),
                req.sourceWallet().getCurrency(),
                TransactionType.TRANSFER,
                req.description(),
                TransactionStatus.COMPLETED
        );

        transactionRepo.save(transaction);


        return new TransferResponse(
                referenceId,
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getStatus()
        );
    }


}
