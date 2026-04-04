package com.ibrakhim2906.walletkz.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    public List<Transaction> findBySourceWallet_Id(Long sourceWalletId);

    public List<Transaction> findByTargetWallet_Id(Long targetWalletId);

}
