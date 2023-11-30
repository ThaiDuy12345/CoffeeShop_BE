package com.duan.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duan.entity.AccountEntity;
import com.duan.repository.AccountRepository;
import com.duan.services.MailService;
import com.duan.utils.EmailTemplate;

@RestController
@RequestMapping("/forgotPasswords")
public class ForgotPasswordController {
  @Autowired
  AccountRepository accountRepository;

  @Autowired
  MailService mailService;

  @PostMapping("/{accountEmail}")
  public ResponseEntity<Map<String, Object>> sendCodeThroughEmail(@PathVariable String accountEmail){
    Map<String, Object> res = new HashMap<String, Object>();
    try{
      Optional<AccountEntity> accountEntity = accountRepository.findByAccountEmail(accountEmail);
      if(accountEntity.isEmpty()){
        res.put("status", false);
        res.put("message", "Tài khoản email không tồn tại");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
      }

      AccountEntity account = accountEntity.get();

      if (!account.isAccountActive()){
        res.put("status", false);
        res.put("message", "Tài khoản email không tồn tại");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
      }

      String randomNumber = getRandomNumber();
      sendEmail(randomNumber, account);

      res.put("status", true);
      res.put("data", encode(randomNumber.toString()));
      return ResponseEntity.ok(res);
    }catch(Exception e){
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }

  private void sendEmail(String number, AccountEntity account){
    new Thread(new Runnable() {
      @Override
      public void run(){
        mailService.sendHTMLEmail(
          account.getAccountEmail(),
          "Yêu cầu khôi phục mật khẩu từ hệ thống COFFEE SHOP",
          EmailTemplate.getEmailTemplate(getContent(number))
        );
      }
    }).start();
  }
  private static String encode(String input) throws NoSuchAlgorithmException {
    return Base64.getEncoder().encodeToString(input.getBytes());
  }

  private String getRandomNumber() {
    Random random = new Random();

    // Tạo một số ngẫu nhiên từ 0 đến 999999
    int randomNumber = random.nextInt(1000000);

    // Cắt số ngẫu nhiên thành 6 chữ số
    String number = String.format("%06d", randomNumber);
    return number;

  }

  private String getContent(String number) {
    return "<p>Xin chào,  </p>\n" 
      +
      "<p>Đã có một ai đó yêu cầu khôi phục mật khẩu, nhưng trước hết cần nhập mã xác nhận:<p>\n"
      +
      "<p style='font-weight: bold'>" + number + "<p>\n"
      +
      "<p>Xin vui lòng không chia sẻ mã này cho bất kỳ ai!!<p>\n";
  }
}
