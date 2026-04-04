package com.ibrakhim2906.walletkz.wallet;

import com.ibrakhim2906.walletkz.common.util.ReferenceIdGenerator;
import com.ibrakhim2906.walletkz.transaction.*;
import com.ibrakhim2906.walletkz.user.CurrentUserService;
import com.ibrakhim2906.walletkz.user.User;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class WalletService {

    private final WalletRepository repo;
    private final CurrentUserService userService;
    private final TransactionRepository transactionRepo;

    public WalletService(WalletRepository repo, CurrentUserService userService, TransactionRepository transactionRepo) {
        this.repo = repo;
        this.userService = userService;
        this.transactionRepo = transactionRepo;
    }

    public WalletResponse createWallet(CreateWalletRequest req) {

        User currentUser = userService.getCurrentUser();

        if (repo.existsByUserIdAndCurrency(currentUser.getId(), req.currency())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Such wallet already exists");
        }

        Wallet wallet = Wallet.create(currentUser, req.currency());

        repo.save(wallet);

        return new WalletResponse(wallet.getId(), wallet.getCurrency(), wallet.getBalance());
    }

    public List<Wallet> showAllMyWallets() {

        User currentUser = userService.getCurrentUser();

        List<Wallet> myWallets = repo.findByUserId(currentUser.getId());

        if (myWallets==null) {
            return new ArrayList<Wallet>();
        }

        return myWallets;
    }

    @Transactional
    public void deposit(Long walletId, DepositRequest req) {

        User currentUser = userService.getCurrentUser();

        Wallet wallet = repo.findByUser_IdAndId(currentUser.getId(), walletId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Such user or wallet do not exist"));

        if (req.amount().doubleValue()<=0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot deposit negative or zero amount");
        }

        wallet.setBalance(wallet.getBalance().add(req.amount()));

        String referenceId = ReferenceIdGenerator.generateReference();

        Transaction transaction = Transaction.create(referenceId, wallet, req.amount(), wallet.getCurrency(), TransactionType.DEPOSIT,
                req.description(), TransactionStatus.COMPLETED);

        transactionRepo.save(transaction);
    }
}
