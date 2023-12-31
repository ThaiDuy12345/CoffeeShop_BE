package com.duan.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class IndexController {

  @GetMapping("/")
  public ResponseEntity<Map<String, Object>> getServerStatus(){
    Map<String, Object> response = new HashMap<>();
    response.put("status", 200);
    response.put("message", "SpringBoot Server is working!!");
    return ResponseEntity.ok(response);
  }
}

