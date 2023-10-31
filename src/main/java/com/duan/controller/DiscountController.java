package com.duan.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
  import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duan.entity.DiscountEntity;
import com.duan.repository.DiscountRepository;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

  private final DiscountRepository discountRepository;

  public DiscountController(DiscountRepository discountRepository) {
      this.discountRepository = discountRepository;
  }

  //Get all discount
  @GetMapping
  public ResponseEntity<Map<String, Object>> getAllDiscounts() {
    Map<String, Object> res = new HashMap<>();
    try{
      res.put("status", true);
      res.put("data", discountRepository.findAll());
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }

  //Get discount by id
  @GetMapping("/{id}")
  public ResponseEntity<Map<String, Object>> getDiscount(@PathVariable int id) {
    Map<String, Object> res = new HashMap<>();
    Optional<DiscountEntity> discount = discountRepository.findById(id);

    if(discount.isPresent()){
      res.put("status", true);
      res.put("data", discount.get());
      return ResponseEntity.ok(res);
    }else{
      res.put("status", false);
      res.put("message", "Id giảm giá không tồn tại");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }
  }

  //Get discount by discount code
  @GetMapping("/code/{code}")
  public ResponseEntity<Map<String, Object>> getDiscountByDiscountCode(@PathVariable String code) {
    Map<String, Object> res = new HashMap<>();
    Optional<DiscountEntity> discount = discountRepository.findByDiscountCode(code);

    if(discount.isPresent()){
      res.put("status", true);
      res.put("data", discount.get());
      return ResponseEntity.ok(res);
    }else{
      res.put("status", false);
      res.put("message", "Mã giảm giá không tồn tại");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }
  }

  //Create a discount
  @PostMapping
  public ResponseEntity<Map<String, Object>> createDiscount(@RequestBody DiscountEntity discount) {
    Map<String, Object> res = new HashMap<>();
    if (discountRepository.existsByDiscountCode(discount.getDiscountCode())){
      res.put("status", false);
      res.put("message", "Mã code đã tồn tại trong hệ thống");
      return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    if(discount.getDiscountAmount().compareTo(new BigDecimal(200000)) == 0 
        || 
      discount.getDiscountAmount().compareTo(new BigDecimal(200000)) > 0
    ){
      res.put("status", false);
      res.put("message", "Mức giá giảm không hợp lệ, vui lòng nhập mức giảm dưới 200.000vnđ");
      return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }
    try {
      DiscountEntity discountEntity = discountRepository.save(discount);
      res.put("status", true);
      res.put("data", discountEntity);
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      res.put("status", false);
      res.put("message", "Tạo mới thất bại, đã có lỗi xảy");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }

  //Update a discount
  @PutMapping("/{id}")
  public ResponseEntity<Map<String, Object>> updateDiscount(@PathVariable int id, @RequestBody DiscountEntity discount) {
    Map<String, Object> res = new HashMap<>();
    Optional<DiscountEntity> discountEntityOptional = discountRepository.findById(id);

    if (!discountEntityOptional.isPresent()) {
      res.put("status", false);
      res.put("message", "Id giảm giá không tồn tại");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    

    if(discount.getDiscountAmount().compareTo(new BigDecimal(200000)) == 0 
        || 
      discount.getDiscountAmount().compareTo(new BigDecimal(200000)) > 0
    ){
      res.put("status", false);
      res.put("message", "Mức giá giảm không hợp lệ, vui lòng nhập mức giảm dưới 200.000vnđ");
      return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    DiscountEntity discountEntity = discountEntityOptional.get();
    
    if (discountEntity.getDiscountCode().equals(discount.getDiscountCode())) {

      discountEntity.setDiscountExpiredDate(discount.getDiscountExpiredDate());
      discountEntity.setDiscountMinimumOrderPrice(discount.getDiscountMinimumOrderPrice());
      discountEntity.setDiscountAmount(discount.getDiscountAmount());
      

      try{
        res.put("status", true);
        res.put("data", discountRepository.save(discountEntity));
        return ResponseEntity.ok(res);
      }catch (Exception e){
        System.out.println(e);
        res.put("status", false);
        res.put("message", "Đã có lỗi khi cập nhật, xin vui lòng thử lại");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
      }
    } else {
      if (discountRepository.existsByDiscountCode(discount.getDiscountCode())) {
        res.put("status", false);
        res.put("message", "Mã code đã tồn tại trong hệ thống");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
      } else {

        discountEntity.setDiscountCode(discount.getDiscountCode());
        discountEntity.setDiscountExpiredDate(discount.getDiscountExpiredDate());
        discountEntity.setDiscountMinimumOrderPrice(discount.getDiscountMinimumOrderPrice());
        discountEntity.setDiscountAmount(discount.getDiscountAmount());
        

        try{
          res.put("status", true);
          res.put("data", discountRepository.save(discountEntity));
          return ResponseEntity.ok(res);
        }catch (Exception e){
          System.out.println(e);
          res.put("status", false);
          res.put("message", "Đã có lỗi khi cập nhật, xin vui lòng thử lại");
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
      }
    }
  }
}
