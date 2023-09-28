package com.duan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duan.Entity.AccountEntity;

public interface UserRepository extends JpaRepository<AccountEntity, Integer>{
	AccountEntity findByEmail(String email);

	AccountEntity findByToken(String token);
}
