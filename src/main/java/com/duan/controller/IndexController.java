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
    response.put("status", "true");
    response.put("message", "Server is working at port 8080!!");
    return ResponseEntity.ok(response);
  }
}

