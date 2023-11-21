package com.duan.controller;

import java.util.HashMap;
import java.util.List;
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

import com.duan.entity.CategoryEntity;
import com.duan.entity.ProductEntity;
import com.duan.repository.CategoryRepository;
import com.duan.repository.ProductRepository;
import com.duan.repository.ProductSizeRepository;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
    private ProductRepository productRepository;
	
	@Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProduct() {
    	Map<String, Object> res = new HashMap<>();
        res.put("status", true);
        res.put("data", productRepository.findAll());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/withPrice")
    public ResponseEntity<Map<String, Object>> getAllProductWithPrice() {
    	Map<String, Object> res = new HashMap<>();
        res.put("status", true);
        res.put("data", productRepository.findAllProductWithMinPrice());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable int id) {
        Map<String, Object> res = new HashMap<>();
        Optional<ProductEntity> product = productRepository.findById(id);
        if (product.isPresent()) {
            res.put("status", true);
            res.put("data", product.get());
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Sản phẩm không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

    @GetMapping("/soldQuantity/{productId}")
    public ResponseEntity<Map<String, Object>> getProductSoldQuantityById(@PathVariable int productId) {
        Map<String, Object> res = new HashMap<>();
        Optional<ProductEntity> product = productRepository.findById(productId);
        if (product.isPresent()) {
            res.put("status", true);
            res.put("data", productRepository.getSoldQuantityByProductId(product.get().getProductId()));
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Sản phẩm không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Map<String, Object>> getProductByCategory(@PathVariable int categoryId) {
        Map<String, Object> res = new HashMap<>();
        Optional<CategoryEntity> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            List<ProductEntity> products = productRepository.findByCategoryEntity(category.get());
            if (!products.isEmpty()) {
                res.put("status", true);
                res.put("data", products);
                return ResponseEntity.ok(res);
            } else {
                res.put("status", false);
                res.put("message", "Không tìm thấy sản phẩm trong danh mục đã cho");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
            }
        } else {
            res.put("status", false);
            res.put("message", "Danh mục không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody ProductEntity product) {
        Map<String, Object> res = new HashMap<>();
        try {

            if(product.isProductIsPopular() == true && product.isProductActive() == false){
                res.put("status", false);
                res.put("message", "Sản phẩm phổ biến không thể bị ẩn");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            if(product.isProductActive() == true && (product.getProductImageUrl() == null || product.getProductImageUrl() == "")){
                res.put("status", false);
                res.put("message", "Sản phẩm không thể được kích hoạt nếu chưa có ảnh hoặc các kích cỡ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }


            ProductEntity createdProduct = productRepository.save(product);
            res.put("status", true);
            res.put("data", createdProduct);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", false);
            res.put("message", "Đã có lỗi xảy ra trong quá trình tạo sản phẩm");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable int id, @RequestBody ProductEntity product) {
        Map<String, Object> res = new HashMap<>();
        Optional<ProductEntity> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            ProductEntity updatedProduct = existingProduct.get();

            if(product.isProductIsPopular() == true && product.isProductActive() == false){
                res.put("status", false);
                res.put("message", "Sản phẩm phổ biến không thể bị ẩn");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            if(product.isProductActive() == true && (product.getProductImageUrl() == null || product.getProductImageUrl() == "")){
                res.put("status", false);
                res.put("message", "Sản phẩm không thể được kích hoạt nếu chưa có ảnh");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }
            if(product.isProductActive() == true && productSizeRepository.findByProductEntityProductId(id).isEmpty()){
                res.put("status", false);
                res.put("message", "Sản phẩm không thể được kích hoạt nếu chưa có các kích cỡ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            updatedProduct.setProductName(product.getProductName());
            updatedProduct.setProductDescription(product.getProductDescription());
            updatedProduct.setProductIsPopular(product.isProductIsPopular());
            updatedProduct.setProductActive(product.isProductActive());
            updatedProduct.setProductImageUrl(product.getProductImageUrl());
            updatedProduct.setCategoryEntity(categoryRepository.findById(product.getCategoryEntity().getCategoryId()).get());
            updatedProduct = productRepository.save(updatedProduct);
            res.put("status", true);
            res.put("data", updatedProduct);
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Sản phẩm không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

}
