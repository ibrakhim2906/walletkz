package com.ibrakhim2906.walletkz.auth;

import com.ibrakhim2906.walletkz.common.util.JwtUtil;
import com.ibrakhim2906.walletkz.common.util.PhoneValidator;
import com.ibrakhim2906.walletkz.user.User;
import com.ibrakhim2906.walletkz.user.UserRepository;
import com.ibrakhim2906.walletkz.wallet.CurrencyEnum;
import com.ibrakhim2906.walletkz.wallet.Wallet;
import com.ibrakhim2906.walletkz.wallet.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final JwtUtil jwtService;
    private final PasswordEncoder encoder;
    private final WalletRepository walletRepo;

    public AuthService(UserRepository userRepo, PasswordEncoder encoder, WalletRepository walletRepo, JwtUtil jwtService) {
        this.walletRepo = walletRepo;
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public boolean register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email is already taken");
        }
        if (userRepo.existsByPhone(req.phone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This phone number is already taken");
        }
        if (!PhoneValidator.isValid(req.phone())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect phone number format");
        }

        User user = User.create(req.email(), req.phone(), encoder.encode(req.password()));
        Wallet wallet = Wallet.create(user, CurrencyEnum.KZT);

        userRepo.saveAndFlush(user);
        System.out.println("USER SAVED, ID = " + user.getId());
        walletRepo.saveAndFlush(wallet);
        System.out.println("USER SAVED, ID = " + user.getId());



        return true;
    }

    public AuthResponse login(LoginRequest req) {

        User user = userRepo.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        if (!encoder.matches(req.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is incorrect");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
}
