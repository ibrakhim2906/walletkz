package com.ibrakhim2906.walletkz.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    public boolean existsByUserIdAndCurrency(Long userId, CurrencyEnum currency);

    public ArrayList<Wallet> findByUserId(Long userId);

    public Optional<Wallet> findByUser_IdAndId(Long UserId, Long Id);


}