package com.ibrakhim2906.walletkz.transaction;

import com.ibrakhim2906.walletkz.user.CurrentUserService;
import com.ibrakhim2906.walletkz.wallet.WalletRepository;
import org.springframework.stereotype.Service;

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



//    public
}
