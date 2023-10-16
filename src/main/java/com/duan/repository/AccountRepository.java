package com.duan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duan.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, String>{
	AccountEntity findByEmailAndPassword(String accountEmail, String accountPassword);
	boolean existsByPhone(String accountPhone);
}
