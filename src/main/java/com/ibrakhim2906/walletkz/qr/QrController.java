package com.ibrakhim2906.walletkz.qr;

import com.ibrakhim2906.walletkz.transaction.transfer.TransferResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr")
public class QrController {

    private final QrService service;

    public QrController(QrService service) {
        this.service = service;
    }

    @GetMapping("/{token}")
    public ResponseEntity<QrPaymentResponse> findByToken(@PathVariable String token){
        return ResponseEntity.ok().body(service.findByToken(token));
    }

    @PostMapping("/create")
    public ResponseEntity<QrPaymentResponse> createRequest(@Valid @RequestBody CreateQrPaymentRequest request) {
        return ResponseEntity.ok().body(service.createRequest(request));
    }

    @PostMapping("/pay")
    public ResponseEntity<TransferResponse> payQr(@Valid @RequestBody PayQrPaymentRequest request) {
        return ResponseEntity.ok().body(service.payQr(request));
    }
}
