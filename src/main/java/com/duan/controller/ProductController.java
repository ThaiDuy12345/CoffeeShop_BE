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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duan.entity.CategoryEntity;
import com.duan.entity.ProductEntity;
import com.duan.repository.CategoryRepositoty;
import com.duan.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
    private ProductRepository productRepository;
	
	@Autowired
    private CategoryRepositoty categoryRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProduct() {
    	Map<String, Object> res = new HashMap<>();
        res.put("status", true);
        res.put("data", productRepository.findAll());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCategoryById(@PathVariable int id) {
        Map<String, Object> res = new HashMap<>();
        Optional<ProductEntity> product = productRepository.findById(id);
        if (product.isPresent()) {
            res.put("status", true);
            res.put("data", product.get());
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Danh mục không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Map<String, Object>> getProductByCategory(@PathVariable int categoryId) {
        Map<String, Object> res = new HashMap<>();
        Optional<CategoryEntity> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            List<ProductEntity> products = productRepository.findByCategory(category.get());
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
}
