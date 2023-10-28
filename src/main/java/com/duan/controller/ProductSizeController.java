package com.duan.controller;

import com.duan.entity.ProductSizeEntity;
import com.duan.repository.ProductSizeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/productsizes")
public class ProductSizeController {

    private final ProductSizeRepository productSizeRepository;

    @Autowired
    public ProductSizeController(ProductSizeRepository productSizeRepository) {
        this.productSizeRepository = productSizeRepository;
    }

    // GET /productsizes/product/{productId}
    @GetMapping("/product/{productId}")
    public ResponseEntity<Map<String, Object>> getAllProductSizeByProductID(@PathVariable int productId) {
        Map<String, Object> res = new HashMap<>();
        List<ProductSizeEntity> productSizes = productSizeRepository.findByProduct_ProductId(productId);
        if (!productSizes.isEmpty()) {
            res.put("status", true);
            res.put("data", productSizes);
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Không tìm thấy kích thước sản phẩm cho ID sản phẩm đã cung cấp");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

    // PUT /productsizes/{productSizeId}
    @PutMapping("/{productSizeId}")
    public ResponseEntity<Map<String, Object>> updateProductSize(@PathVariable int productSizeId, @RequestBody ProductSizeEntity productSize) {
        Optional<ProductSizeEntity> optionalProductSize = productSizeRepository.findById(productSizeId);
        Map<String, Object> res = new HashMap<>();
        if (optionalProductSize.isPresent()) {
            ProductSizeEntity existingProductSize = optionalProductSize.get();
            existingProductSize.setProductSize(productSize.getProductSize());
            existingProductSize.setProductSizePrice(productSize.getProductSizePrice());
            existingProductSize = productSizeRepository.save(existingProductSize);
            res.put("status", true);
            res.put("data", existingProductSize);
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Kích thước sản phẩm không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

    // DELETE /productsizes/{productSizeId}
    @DeleteMapping("/productSize/{id}")
    public ResponseEntity<Map<String, Object>> deleteProductSize(@PathVariable int id) {
        Map<String, Object> res = new HashMap<>();
        try {
            productSizeRepository.deleteById(id);
            res.put("status", true);
            res.put("message", "Size sản phẩm có ID " + id + " đã bị xóa");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", false);
            res.put("message", "Không xóa được size sản phẩm có ID " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
}