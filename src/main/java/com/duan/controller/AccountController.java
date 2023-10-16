package com.duan.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
 // GET /accounts
    @GetMapping
    public List<AccountEntity> getAllAccounts() {
        return accountRepository.findAll();
    }
 // GET /accounts/{phone}
    @GetMapping("/{phone}")
    public ResponseEntity<AccountEntity> getAccountByPhone(@PathVariable String phone) {
        Optional<AccountEntity> optionalAccount = accountRepository.findById(phone);
        if (optionalAccount.isPresent()) {
            return ResponseEntity.ok(optionalAccount.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AccountEntity account) {
    	if (accountRepository.existsByPhone(account.getAccountPhone())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone already exists");
        }
        // Insert new user into the database
    	try {
            accountRepository.save(account);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user");
        }
    }
 // PUT /accounts/{phone}
    @PutMapping("/{phone}")
    public ResponseEntity<String> updateAccount(@PathVariable String phone, @RequestBody AccountEntity account) {
        Optional<AccountEntity> optionalAccount = accountRepository.findById(phone);
        if (optionalAccount.isPresent()) {
            AccountEntity existingAccount = optionalAccount.get();
            
            // Update properties of the existing account
            existingAccount.setAccountName(account.getAccountName());
            existingAccount.setAccountPassword(account.getAccountPassword());
            existingAccount.setAccountAddress(account.getAccountAddress());
            existingAccount.setAccountEmail(account.getAccountEmail());
            existingAccount.setAccountRole(account.getAccountRole());
            existingAccount.setAccountActive(account.isAccountActive());
            
            accountRepository.save(existingAccount);
            return ResponseEntity.ok("Account updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
 // DELETE /accounts/{phone}
    @DeleteMapping("/{phone}")
    public ResponseEntity<String> deleteAccount(@PathVariable String phone) {
        if (accountRepository.existsById(phone)) {
            accountRepository.deleteById(phone);
            return ResponseEntity.ok("Account deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AccountEntity account) {
        AccountEntity foundAccount = accountRepository.findByEmailAndPassword(account.getAccountEmail(), account.getAccountPassword());

        if (foundAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        return ResponseEntity.ok("Login successful");
    }
}
