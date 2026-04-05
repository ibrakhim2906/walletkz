package com.ibrakhim2906.walletkz.qr;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QrPaymentRepository extends JpaRepository<QrPayment, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT q FROM QrPayment q WHERE q.token = :token")
    public Optional<QrPayment> findByTokenForUpdate(@Param("token") String token);

    public Optional<QrPayment> findByToken(String token);
}
