package com.duan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duan.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, String>{
	AccountEntity findByAccountEmailAndAccountPassword(String accountEmail, String accountPassword);
	Optional<AccountEntity> findByAccountEmail(String accountEmail);
	boolean existsByAccountPhone(String accountPhone);
	boolean existsByAccountEmail(String accountEmail);
}
