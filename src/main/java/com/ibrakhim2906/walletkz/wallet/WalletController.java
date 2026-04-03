package com.ibrakhim2906.walletkz.wallet;

import com.ibrakhim2906.walletkz.transaction.DepositRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WalletController {

    private final WalletService service;

    public WalletController(WalletService service) {
        this.service = service;
    }

    @GetMapping("/showAllMyWallets")
    public ResponseEntity<List<Wallet>> showAllMyWallets() {
        List<Wallet> wallets = service.showAllMyWallets();

        return ResponseEntity.ok().body(wallets);
    }

    @PostMapping("/createWallet")
    public ResponseEntity<WalletResponse> createWallet(@Valid @RequestBody CreateWalletRequest req) {
        return ResponseEntity.ok().body(service.createWallet(req));
    }

    @PostMapping("/{walletId}/deposit")
    public void deposit(@PathVariable Long walletId, @Valid @RequestBody DepositRequest req) {
        service.deposit(walletId, req);
    }

}