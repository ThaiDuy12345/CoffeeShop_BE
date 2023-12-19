package com.duan.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    @Autowired
	private AccountRepository accountRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAccounts() {
        Map<String, Object> res = new HashMap<>();
        res.put("status", true);
        res.put("data", accountRepository.findAll());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{phone}")
    public ResponseEntity<Map<String, Object>> getAccountByPhone(@PathVariable String phone) {
        Map<String, Object> res = new HashMap<>();
        Optional<AccountEntity> optionalAccount = accountRepository.findById(phone);
        if (optionalAccount.isPresent()) {
            res.put("status", true);
            res.put("data", optionalAccount.get());
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Tài khoản không tồn tại"); 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> getAccountByEmail(@PathVariable String email) {
        Map<String, Object> res = new HashMap<>();
        Optional<AccountEntity> account = accountRepository.findByAccountEmail(email);
        if (account.isPresent()) {
            res.put("status", true);
            res.put("data", account.get());
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Tài khoản không tồn tại");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody AccountEntity account) {
        Map<String, Object> res = new HashMap<>();
    	if (accountRepository.existsByAccountPhone(account.getAccountPhone()) || accountRepository.existsByAccountEmail(account.getAccountEmail())) {
            res.put("status", false);
            res.put("message", "Số điện thoại hoặc email đã tồn tại ở tài khoản khác!!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        // Insert new user into the database
    	try {
            AccountEntity accountEntity = accountRepository.save(account);
            res.put("status", true);
            res.put("data", accountEntity);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", false);
            res.put("message", "Đã có lỗi xảy ra trong quá trình đăng ký!!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

 // PUT /accounts/{phone}
    @PutMapping("/{phone}")
    public ResponseEntity<Map<String, Object>> updateAccount(@PathVariable String phone, @RequestBody AccountEntity account) {
        Optional<AccountEntity> optionalAccount = accountRepository.findById(phone);
        Map<String, Object> res = new HashMap<>();
        if (optionalAccount.isPresent()) {
            if (
                !(account.getAccountEmail().equalsIgnoreCase(optionalAccount.get().getAccountEmail())) &&
                accountRepository.existsByAccountEmail(account.getAccountEmail())
            ){
                res.put("status", false);
                res.put("message", "Email đã tồn tại ở tài khoản khác!!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res); 
            }
            
            AccountEntity existingAccount = optionalAccount.get();
            
            // Update properties of the existing account
            existingAccount.setAccountEmail(account.getAccountEmail());
            existingAccount.setAccountName(account.getAccountName());
            existingAccount.setAccountPassword(account.getAccountPassword());
            existingAccount.setAccountAddress(account.getAccountAddress());
            existingAccount.setAccountRole(account.getAccountRole());
            existingAccount.setAccountActive(account.isAccountActive());
            
            existingAccount = accountRepository.save(existingAccount);
            res.put("status", true);
            res.put("data", existingAccount);
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Tài khoản không tồn tại");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
    }
 
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AccountEntity account) {
        Map<String, Object> res = new HashMap<>();
        AccountEntity foundAccount = accountRepository.findByAccountEmailAndAccountPassword(account.getAccountEmail(), account.getAccountPassword());
       
        if (foundAccount == null) {
            res.put("status", false);
            res.put("message", "Tài khoản hoặc mật khẩu không tồn tại");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        } else {
            res.put("status", true);
            res.put("data", foundAccount);
            return ResponseEntity.ok(res);
        }
    }
}
