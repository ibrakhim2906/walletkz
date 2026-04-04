package com.ibrakhim2906.walletkz.transaction;

import com.ibrakhim2906.walletkz.user.CurrentUserService;
import com.ibrakhim2906.walletkz.user.User;
import com.ibrakhim2906.walletkz.wallet.Wallet;
import com.ibrakhim2906.walletkz.wallet.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepo;
    private final WalletRepository walletRepo;
    private final CurrentUserService userService;

    public TransactionService(
            TransactionRepository transactionRepo,
            WalletRepository walletRepo,
            CurrentUserService userService
    ) {
        this.transactionRepo = transactionRepo;
        this.walletRepo = walletRepo;
        this.userService = userService;
    }

    public List<TransactionResponse> transactionHistory() {
        User user = userService.getCurrentUser();

        List<Long> walletIds = walletRepo.findByUserId(user.getId())
                .stream().map(Wallet::getId).toList();

        List<Transaction> transactions = transactionRepo.findByWalletIds(walletIds);

        return transactions.stream().
                map(TransactionResponse::toResponse)
                .toList();
    }
}
