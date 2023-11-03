package com.duan.controller;

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

import com.duan.repository.DetailOrderRepository;
import com.duan.entity.DetailOrderEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/detail-orders")
public class DetailOrderController {

    @Autowired
    private DetailOrderRepository detailOrderRepository;

    // GET /detail-orders/by-order/{id}
    @GetMapping("/by-order/{id}")
    public ResponseEntity<Map<String, Object>> getDetailOrdersByOrderId(@PathVariable int id) {
        Map<String, Object> res = new HashMap<>();
        List<DetailOrderEntity> detailOrders = detailOrderRepository.findByOrderId(id);
        if (!detailOrders.isEmpty()) {
            res.put("status", true);
            res.put("data", detailOrders);
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Không có hoá đơn chi tiết nào cho ID hoá đơn đã cho");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }
    
    // POST /detail-orders
    @PostMapping
    public ResponseEntity<Map<String, Object>> createDetailOrder(@RequestBody DetailOrderEntity newDetailOrder) {
        Map<String, Object> res = new HashMap<>();
        try {
            // Kiểm tra xem đã tồn tại chi tiết đơn hàng với orderId và productSizeId tương ứng chưa
            Optional<DetailOrderEntity> existingDetailOrder = detailOrderRepository.findByOrderIdAndProductSizeId(
                    newDetailOrder.getOrderId(), newDetailOrder.getProductSizeId());

            if (existingDetailOrder.isPresent()) {
                res.put("status", false);
                res.put("message", "Hoá đơn chi tiết đã tồn tại ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            // Lưu chi tiết đơn hàng mới vào cơ sở dữ liệu
            DetailOrderEntity createdDetailOrder = detailOrderRepository.save(newDetailOrder);
            res.put("status", true);
            res.put("data", createdDetailOrder);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", false);
            res.put("message", "Đã có lỗi xảy ra trong quá trình tạo hoá đơn chi tiết");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
    
    // PUT /detail-orders/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDetailOrder(@PathVariable int id, @RequestBody DetailOrderEntity updatedDetailOrder) {
        Map<String, Object> res = new HashMap<>();
        Optional<DetailOrderEntity> existingDetailOrder = detailOrderRepository.findById(id);

        if (existingDetailOrder.isPresent()) {
            DetailOrderEntity detailOrderToUpdate = existingDetailOrder.get();

            // Cập nhật thông tin chi tiết đơn hàng với dữ liệu từ payload body
            detailOrderToUpdate.setDetailOrderProductQuantity(updatedDetailOrder.getDetailOrderProductQuantity());
            detailOrderToUpdate.setDetailOrderSubTotal(updatedDetailOrder.getDetailOrderSubTotal());
            // Thêm các trường cần cập nhật tương ứng

            // Lưu chi tiết đơn hàng đã được cập nhật vào cơ sở dữ liệu
            detailOrderToUpdate = detailOrderRepository.save(detailOrderToUpdate);

            res.put("status", true);
            res.put("data", detailOrderToUpdate);
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Hoá đơn chi tiết không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }
    
 // DELETE /detail-orders/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDetailOrder(@PathVariable int id) {
        Map<String, Object> res = new HashMap<>();

        try {
            // Xoá chi tiết đơn hàng theo ID
            detailOrderRepository.deleteById(id);

            res.put("status", true);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", false);
            res.put("message", "Xoá chi tiết đơn hàng không thành công");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
}
