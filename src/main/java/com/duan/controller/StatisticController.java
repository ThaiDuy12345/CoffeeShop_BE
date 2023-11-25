package com.duan.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duan.repository.AccountRepository;
import com.duan.repository.FeedbackRepository;
import com.duan.repository.OrderingRepository;
import com.duan.repository.ProductRepository;
import com.duan.repository.SupportRepository;

@RestController
@RequestMapping("/statistics")
public class StatisticController {

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  OrderingRepository orderingRepository;

  @Autowired
  SupportRepository supportRepository;

  @GetMapping
  public ResponseEntity<Map<String, Object>> getIndexStatistics() {
    Map<String, Object> res = new HashMap<String, Object>();
    Map<String, Object> data = new HashMap<String, Object>();

    try{
      Integer accountStatistic = accountRepository.countByAccountActive(true);
      Integer productStatistic = productRepository.countByProductActive(true);
      Integer orderingStatistic = orderingRepository.countByOrderingStatus(1);
      Integer supportStatistic = supportRepository.countBySupportStatus(false);

      data.put("accountStatistic", accountStatistic);
      data.put("productStatistic", productStatistic);
      data.put("supportStatistic", supportStatistic);
      data.put("orderingStatistic", orderingStatistic);

      res.put("status", true);
      res.put("data", data);
      return ResponseEntity.ok(res);
    }catch(Exception e){
      res.put("status", false);
      res.put("message", "Đã xảy ra lỗi trong quá trình lấy dữ liệu");  
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
    
  }
}
