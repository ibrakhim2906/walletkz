package com.ibrakhim2906.walletkz.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    public boolean existsByUserIdAndCurrency(Long id, CurrencyEnum currency);
}
