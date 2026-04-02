package com.ibrakhim2906.walletkz.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody String email,
                                           @RequestBody String phone,
                                           @PathVariable String password) {
        service.register(new RegisterRequest(email, phone, password));


    }
}
