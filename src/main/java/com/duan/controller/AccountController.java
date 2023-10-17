package com.duan.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.duan.entity.AccountEntity;
import com.duan.repository.AccountRepository;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	private final AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<AccountEntity>> listAllAccount(){
		List<AccountEntity> listaccount= accountRepository.findAll();
		if(listaccount.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<AccountEntity>>(listaccount, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{phone}", method = RequestMethod.GET)
	public AccountEntity findAccount(@PathVariable("phone") int accountPhone) {
		AccountEntity account= accountRepository.getOne(accountPhone);
		if(account == null) {
			ResponseEntity.notFound().build();
		}
		return account;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public AccountEntity saveContact(@Valid @RequestBody AccountEntity account) {
		return accountRepository.save(account);
	}
	
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResponseEntity<AccountEntity> updateContact(@PathVariable(value = "phone") int accountPhone, 
	                                       @Valid @RequestBody AccountEntity accountForm) {
		AccountEntity account = accountRepository.getOne(accountPhone);
	    if(account == null) {
	        return ResponseEntity.notFound().build();
	    }
	    account.setAccountName(accountForm.getAccountName());
	    account.setAccountPassword(accountForm.getAccountPassword());
	    account.setAccountAddress(accountForm.getAccountAddress());
	    account.setAccountEmail(accountForm.getAccountEmail());
	    account.setAccountRole(accountForm.getAccountRole());
	    account.setAccountActive(true);

	    AccountEntity updatedAccount = accountRepository.save(account);
	    return ResponseEntity.ok(updatedAccount);
	}
	
	@RequestMapping(value = "/{phone}", method = RequestMethod.DELETE)
	public ResponseEntity<AccountEntity> deleteContact(@PathVariable(value = "phone") int accountPhone) {
		AccountEntity account = accountRepository.getOne(accountPhone);
	    if(account == null) {
	        return ResponseEntity.notFound().build();
	    }

	    accountRepository.delete(account);
	    return ResponseEntity.ok().build();
	}
	@RequestMapping(value = "/{login}", method = RequestMethod.POST)
	public ResponseEntity<String> login(@RequestBody AccountEntity account) {
        AccountEntity foundAccount = accountRepository.findByEmailAndPassword(account.getAccountEmail(), account.getAccountPassword());

        if (foundAccount == null) {
        	return ResponseEntity.notFound().build();
        }
        accountRepository.save(account);
	    return ResponseEntity.ok().build();
    }
}
