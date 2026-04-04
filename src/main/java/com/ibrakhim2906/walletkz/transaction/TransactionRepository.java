package com.ibrakhim2906.walletkz.transaction;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    public List<Transaction> findBySourceWallet_Id(Long sourceWalletId);
    public List<Transaction> findByTargetWallet_Id(Long targetWalletId);

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.sourceWallet.id IN :walletIds " +
            "OR t.targetWallet.id IN :walletIds " +
            "ORDER BY t.createdAt DESC")
    public List<Transaction> findByWalletIds(@Param("walletIds") List<Long> walletIds);
}
