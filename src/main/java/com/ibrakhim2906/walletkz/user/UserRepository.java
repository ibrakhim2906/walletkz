package com.ibrakhim2906.walletkz.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public boolean existsByEmail(String email);

    public boolean existsByPhone(String phone);

    public Optional<User> findByEmail(String email);

    public Optional<User> findByUserId(Long id);
}
