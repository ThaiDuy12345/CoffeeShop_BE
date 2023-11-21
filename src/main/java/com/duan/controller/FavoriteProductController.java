package com.duan.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duan.entity.AccountEntity;
import com.duan.entity.FavoriteProductEntity;
import com.duan.entity.FavoriteProductId;
import com.duan.entity.ProductEntity;
import com.duan.repository.AccountRepository;
import com.duan.repository.FavoriteProductRepository;
import com.duan.repository.ProductRepository;

@RestController
@RequestMapping("/favoriteProducts")
public class FavoriteProductController {

  @Autowired
  FavoriteProductRepository favoriteProductRepository;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  ProductRepository productRepository;

  @GetMapping("/account/{accountPhone}")
  public ResponseEntity<Map<String, Object>> getAllFavoriteProductsByAccountPhone(@PathVariable String accountPhone){
    Map<String, Object> res = new HashMap<String, Object>();
    try{
      if(accountRepository.findById(accountPhone).isEmpty()){
        res.put("status", false);
        res.put("message", "Tài khoản không tồn tại");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }
      res.put("status", true);
      res.put("data", favoriteProductRepository.findAllByFavoriteProductIdAccountPhone(accountPhone));
      return ResponseEntity.ok(res);
    }catch(Exception e){
      System.out.println(e);
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }

  @GetMapping("/isFavorite/{productId}/{accountPhone}")
  public ResponseEntity<Map<String, Object>> isFavoriteByProductIdAndAccountPhone(
    @PathVariable Integer productId,
    @PathVariable String accountPhone
  ){
    Map<String, Object> res = new HashMap<String, Object>();
    try{
      if(accountRepository.findById(accountPhone).isEmpty()){
        res.put("status", false);
        res.put("message", "Tài khoản không tồn tại");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      if(productRepository.findById(productId).isEmpty()){
        res.put("status", false);
        res.put("message", "Sản phẩm không tồn tại");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }
      res.put("status", true);
      res.put("data", favoriteProductRepository.existsByFavoriteProductIdAccountPhoneAndFavoriteProductIdProductId(accountPhone, productId));
      return ResponseEntity.ok(res);
    }catch(Exception e){
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }

  @PostMapping("/{productId}/{accountPhone}")
  public ResponseEntity<Map<String, Object>> createFavoriteByProductIdAndAccountPhone(
    @PathVariable Integer productId,
    @PathVariable String accountPhone
  ){
    Map<String, Object> res = new HashMap<String, Object>();
    try{
      Optional<AccountEntity> accountEntity = accountRepository.findById(accountPhone);
      Optional<ProductEntity> productEntity = productRepository.findById(productId);     
      if(accountEntity.isEmpty()){
        res.put("status", false);
        res.put("message", "Tài khoản không tồn tại");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      if(productEntity.isEmpty()){
        res.put("status", false);
        res.put("message", "Sản phẩm không tồn tại");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      FavoriteProductEntity fpe = new FavoriteProductEntity();
      FavoriteProductId fpi = new FavoriteProductId(productId, accountPhone);
      
      if(favoriteProductRepository.existsById(fpi)){
        res.put("status", false);
        res.put("message", "Bạn đã yêu thích sản phẩm này rồi");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }
      
      fpe.setFavoriteProductId(fpi);
      fpe.setAccountEntity(accountEntity.get());
      fpe.setProductEntity(productEntity.get());
      
      favoriteProductRepository.save(fpe);
      res.put("status", true);
      res.put("data", fpe);
      return ResponseEntity.ok(res);
    }catch(Exception e){
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }

  @DeleteMapping("/{productId}/{accountPhone}")
  public ResponseEntity<Map<String, Object>> deleteFavoriteByProductIdAndAccountPhone(
    @PathVariable Integer productId,
    @PathVariable String accountPhone
  ){
    Map<String, Object> res = new HashMap<String, Object>();
    try{
      Optional<AccountEntity> accountEntity = accountRepository.findById(accountPhone);
      Optional<ProductEntity> productEntity = productRepository.findById(productId);     
      if(accountEntity.isEmpty()){
        res.put("status", false);
        res.put("message", "Tài khoản không tồn tại");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      if(productEntity.isEmpty()){
        res.put("status", false);
        res.put("message", "Sản phẩm không tồn tại");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      FavoriteProductId fpi = new FavoriteProductId(productId, accountPhone);
      
      if(!favoriteProductRepository.existsById(fpi)){
        res.put("status", false);
        res.put("message", "Bạn không thể xoá sản phẩm chưa từng yêu thích");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }
      
      favoriteProductRepository.deleteById(fpi);
      res.put("status", true);
      res.put("data", true);
      return ResponseEntity.ok(res);
    }catch(Exception e){
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }
}
