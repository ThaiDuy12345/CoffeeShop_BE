package com.duan.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duan.model.AccountModel;

@Repository
public interface AccountService extends JpaRepository<AccountModel, Integer> {


}
