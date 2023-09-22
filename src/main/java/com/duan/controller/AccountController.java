package com.duan.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.duan.model.AccountModel;
import com.duan.service.AccountService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/account")
public class AccountController {
	public static Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	AccountService accountService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<AccountModel>> listAllAccount(){
		List<AccountModel> listaccount= accountService.findAll();
		if(listaccount.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<AccountModel>>(listaccount, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public AccountModel findAccount(@PathVariable("Id") int accountId) {
		AccountModel account= accountService.getOne(accountId);
		if(account == null) {
			ResponseEntity.notFound().build();
		}
		return account;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public AccountModel saveContact(@Valid @RequestBody AccountModel account) {
		return accountService.save(account);
	}
	
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResponseEntity<AccountModel> updateContact(@PathVariable(value = "Id") int accountId, 
	                                       @Valid @RequestBody AccountModel accountForm) {
		AccountModel account = accountService.getOne(accountId);
	    if(account == null) {
	        return ResponseEntity.notFound().build();
	    }
	    account.setName(accountForm.getName());
	    account.setPassword(accountForm.getPassword());
	    account.setAddress(accountForm.getAddress());
	    account.setEmail(accountForm.getEmail());
	    account.setPhone(accountForm.getPhone());

	    AccountModel updatedAccount = accountService.save(account);
	    return ResponseEntity.ok(updatedAccount);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<AccountModel> deleteContact(@PathVariable(value = "Id") int accountId) {
		AccountModel account = accountService.getOne(accountId);
	    if(account == null) {
	        return ResponseEntity.notFound().build();
	    }

	    accountService.delete(account);
	    return ResponseEntity.ok().build();
	}
}
