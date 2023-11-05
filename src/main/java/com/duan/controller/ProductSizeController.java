package com.duan.controller;

import com.duan.entity.ProductEntity;
import com.duan.entity.ProductSizeEntity;
import com.duan.repository.ProductRepository;
import com.duan.repository.ProductSizeRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/product_sizes")
public class ProductSizeController {

    private final ProductSizeRepository productSizeRepository;
    private final ProductRepository productRepository;

    public ProductSizeController(ProductSizeRepository productSizeRepository, ProductRepository productRepository) {
        this.productSizeRepository = productSizeRepository;
        this.productRepository = productRepository;
    }

    // GET /product_sizes/product/{productId}
    @GetMapping("/product/{productId}")
    public ResponseEntity<Map<String, Object>> getAllProductSizeByProductID(@PathVariable int productId) {
        Map<String, Object> res = new HashMap<>();
        List<ProductSizeEntity> productSizes = productSizeRepository.findByProductEntity_ProductId(productId);
        res.put("status", true);
        res.put("data", productSizes);
        return ResponseEntity.ok(res);
    }

    // PUT /product_sizes/product/{productId}
    @PostMapping("/product/{productId}")
    public ResponseEntity<Map<String, Object>> createProductSize(@PathVariable int productId, @RequestBody ProductSizeEntity productSize) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(productId);
        Map<String, Object> res = new HashMap<>();
        if (optionalProduct.isPresent()) {
            ProductEntity existingProduct = optionalProduct.get();

            if(productSizeRepository.countByProductSizeAndProductId(productSize.getProductSize(), existingProduct.getProductId()) > 0){
                res.put("status", false);
                res.put("message", "Kích cỡ sản phẩm đã tồn tại");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            if(productSize.getProductSizePrice().compareTo(new BigDecimal(1000000)) > 0){
                res.put("status", false);
                res.put("message", "Gía thành kích cỡ không được vượt quá 1.000.000VNĐ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            ProductSizeEntity productSizeEntity = new ProductSizeEntity();
            
            productSizeEntity.setProductSize(productSize.getProductSize());
            productSizeEntity.setProductSizePrice(productSize.getProductSizePrice());
            productSizeEntity.setProductEntity(existingProduct);

            productSizeEntity = productSizeRepository.save(productSizeEntity);
            res.put("status", true);
            res.put("data", productSizeEntity);
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Sản phẩm không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

    // POST /product_sizes/{productSizeId}
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

    // DELETE /product_sizes/{productSizeId}
    @DeleteMapping("/{productSizeId}")
    public ResponseEntity<Map<String, Object>> deleteProductSize(@PathVariable int productSizeId) {
        Map<String, Object> res = new HashMap<>();
        try {
            productSizeRepository.deleteById(productSizeId);
            res.put("status", true);
            res.put("message", "Size sản phẩm có ID " + productSizeId + " đã bị xóa");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", false);
            res.put("message", "Không xóa được size sản phẩm có ID " + productSizeId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
}