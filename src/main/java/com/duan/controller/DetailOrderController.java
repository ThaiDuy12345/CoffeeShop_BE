package com.duan.controller;

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

import com.duan.repository.DetailOrderRepository;

import com.duan.entity.DetailOrderEntity;
import com.duan.entity.DetailOrderId;

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
    @GetMapping("/ordering/{id}")
    public ResponseEntity<Map<String, Object>> getDetailOrdersByOrderId(@PathVariable int id) {
        Map<String, Object> res = new HashMap<>();
        List<DetailOrderEntity> detailOrders = detailOrderRepository.findAllByDetailOrderIdOrderingId(id);
        res.put("status", true);
        res.put("data", detailOrders);
        return ResponseEntity.ok(res);
    }
    
    
    // POST /detail-orders
    @PostMapping
    public ResponseEntity<Map<String, Object>> createDetailOrder(@RequestBody DetailOrderEntity newDetailOrder)  throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        try {
            // Kiểm tra xem đã tồn tại chi tiết đơn hàng với orderId và productSizeId tương ứng chưa
            Optional<DetailOrderEntity> existingDetailOrder = detailOrderRepository.findById(
                newDetailOrder.getDetailOrderId()
            );

            if (existingDetailOrder.isPresent()) {
                if(
                    (detailOrderRepository
                        .getSumProductInTheOrdering(existingDetailOrder.get().getDetailOrderId().getOrderingId())
                        + newDetailOrder.getDetailOrderProductQuantity()) > 30
                ){
                    res.put("status", false);
                    res.put("message", "Số lượng sản phẩm trong giỏ hàng chỉ có thể chứa tối đa 30 sản phẩm");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
                }

                detailOrderRepository.updateDetailOrder(
                    existingDetailOrder.get().getDetailOrderProductQuantity() + newDetailOrder.getDetailOrderProductQuantity(),
                    newDetailOrder.getDetailOrderId().getOrderingId(),
                    newDetailOrder.getDetailOrderId().getProductSizeId()
                );
                res.put("status", true);
                res.put("data", existingDetailOrder);
                return ResponseEntity.ok(res);
            }

            if(
                (detailOrderRepository
                        .getSumProductInTheOrdering(newDetailOrder.getDetailOrderId().getOrderingId())
                        + newDetailOrder.getDetailOrderProductQuantity()) > 30
            ){
                res.put("status", false);
                res.put("message", "Số lượng sản phẩm trong giỏ hàng chỉ có thể chứa tối đa 30 sản phẩm");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }
            
            // Lưu chi tiết đơn hàng mới vào cơ sở dữ liệu
             detailOrderRepository.insertNewDetailOrder(
                newDetailOrder.getDetailOrderProductQuantity(),
                newDetailOrder.getDetailOrderId().getOrderingId(),
                newDetailOrder.getDetailOrderId().getProductSizeId()
            );

            DetailOrderEntity newFetchDetailOrder = detailOrderRepository.findById(
                newDetailOrder.getDetailOrderId()
            ).get();
            
            res.put("status", true);
            res.put("data", newFetchDetailOrder);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            System.out.println(e);
            res.put("status", false);
            res.put("message", "Đã có lỗi xảy ra trong quá trình tạo hoá đơn chi tiết");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
    
    // PUT /detail-orders
    @PutMapping
    public ResponseEntity<Map<String, Object>> updateDetailOrder(@RequestBody DetailOrderEntity updatedDetailOrder) throws InterruptedException {
        Map<String, Object> res = new HashMap<>();
        // Kiểm tra xem đã tồn tại chi tiết đơn hàng với orderId và productSizeId tương ứng chưa
        Optional<DetailOrderEntity> existingDetailOrder = detailOrderRepository.findById(
            updatedDetailOrder.getDetailOrderId()
        );

        if (existingDetailOrder.isPresent()) {
            DetailOrderEntity detailOrderToUpdate = existingDetailOrder.get();
            
            int currentQuantity = detailOrderRepository
                .getSumProductInTheOrdering(existingDetailOrder.get().getDetailOrderId().getOrderingId())
                - detailOrderToUpdate.getDetailOrderProductQuantity();
            if(
                currentQuantity + updatedDetailOrder.getDetailOrderProductQuantity() > 30
            ){
                res.put("status", false);
                res.put("message", "Số lượng sản phẩm trong giỏ hàng chỉ có thể chứa tối đa 30 sản phẩm");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            // Cập nhật thông tin chi tiết đơn hàng với dữ liệu từ payload body
            detailOrderToUpdate.setDetailOrderProductQuantity(updatedDetailOrder.getDetailOrderProductQuantity());
            // Thêm các trường cần cập nhật tương ứng

            // Lưu chi tiết đơn hàng đã được cập nhật vào cơ sở dữ liệu
            detailOrderRepository.updateDetailOrder(
                updatedDetailOrder.getDetailOrderProductQuantity(),
                updatedDetailOrder.getDetailOrderId().getOrderingId(),
                updatedDetailOrder.getDetailOrderId().getProductSizeId()
            );

            DetailOrderEntity newFetchDetailOrder = detailOrderRepository.findById(
                updatedDetailOrder.getDetailOrderId()
            ).get();
            
            res.put("status", true);
            res.put("data",newFetchDetailOrder);
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Hoá đơn chi tiết không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }
    
 // DELETE /detail-orders/{id}
    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteDetailOrder(@RequestBody DetailOrderId detailOrderId) {
        Map<String, Object> res = new HashMap<>();

        try {
            Optional<DetailOrderEntity> existingDetailOrder = detailOrderRepository.findById(detailOrderId);

            // Xoá chi tiết đơn hàng theo ID
             if (!existingDetailOrder.isPresent()) {
                res.put("status", false);
                res.put("message", "Hoá đơn chi tiết không tồn tại");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
            }
            detailOrderRepository.deleteById(existingDetailOrder.get().getDetailOrderId());

            res.put("status", true);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", false);
            res.put("message", "Xoá chi tiết đơn hàng không thành công");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
}
