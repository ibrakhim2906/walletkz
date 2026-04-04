package com.ibrakhim2906.walletkz.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionResponse>> transactionHistory() {
        return ResponseEntity.ok().body(service.transactionHistory());
    }
}
