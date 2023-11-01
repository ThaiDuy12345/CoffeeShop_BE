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

import com.duan.entity.OrderingEntity;
import com.duan.repository.AccountRepository;
import com.duan.repository.OrderRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    // GET /orders
    @GetMapping("orders")
    public ResponseEntity<Map<String, Object>> getAllOrder() {
        Map<String, Object> res = new HashMap<>();
        res.put("status", true);
        res.put("data", orderRepository.findAll());
        return ResponseEntity.ok(res);
    }

    // GET /orders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable int orderId) {
        Map<String, Object> res = new HashMap<>();
        Optional<OrderingEntity> order = orderRepository.findByOrderID(orderId);
        if (order.isPresent()) {
            res.put("status", true);
            res.put("data", order.get());
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Hóa đơn không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

 // PUT /orders/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateOrder(@PathVariable int id, @RequestBody OrderingEntity updatedOrder) {
        Optional<OrderingEntity> existingOrder = orderRepository.findByOrderID(id);
        Map<String, Object> res = new HashMap<>();

        if (existingOrder.isPresent()) {
        	OrderingEntity orderToUpdate = existingOrder.get();

            

            // Cập nhật thông tin hóa đơn với dữ liệu từ payload body
            orderToUpdate.setOrderingStatus(updatedOrder.getOrderingStatus());
            orderToUpdate.setOrderingCreationDate(updatedOrder.getOrderingCreationDate());
            orderToUpdate.setOrderingShippingFee(updatedOrder.getOrderingShippingFee());
            orderToUpdate.setOrderingPrice(updatedOrder.getOrderingPrice());
            orderToUpdate.setOrderingTotalPrice(updatedOrder.getOrderingTotalPrice());
            orderToUpdate.setOrderingNote(updatedOrder.getOrderingNote());
            orderToUpdate.setDiscount(updatedOrder.getDiscount());
            

            // Lưu hóa đơn đã được cập nhật vào cơ sở dữ liệu
            try {
                orderToUpdate = orderRepository.save(orderToUpdate);
                res.put("status", true);
                res.put("data", orderToUpdate);
                return ResponseEntity.ok(res);
            } catch (Exception e) {
                res.put("status", false);
                res.put("message", "Đã có lỗi xảy ra trong quá trình cập nhật hóa đơn");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
            }
        } else {
            res.put("status", false);
            res.put("message", "Hóa đơn không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }


    // POST /orders
    @PostMapping("/{accountPhone}")
    public ResponseEntity<Map<String, Object>> createOrder(@PathVariable String accountPhone, @RequestBody OrderingEntity newOrder) {
        Map<String, Object> res = new HashMap<>();
        try {
            if(!accountRepository.existsByAccountPhone(accountPhone)){
                res.put("status", false);
                res.put("message", "Số điện thoại tài khoản không tồn tại");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
            }
            // Lưu hóa đơn mới vào cơ sở dữ liệu
        	OrderingEntity createdOrder = orderRepository.save(newOrder);
            createdOrder.setAccount(accountRepository.findById(accountPhone).get());
            OrderingEntity order = orderRepository.save(createdOrder);

            res.put("status", true);
            res.put("data", order);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", false);
            res.put("message", "Đã có lỗi xảy ra trong quá trình tạo hóa đơn");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
}