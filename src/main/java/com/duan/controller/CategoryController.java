package com.duan.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.duan.entity.CategoryEntity;
import com.duan.repository.CategoryRepositoty;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	
    @Autowired
    private CategoryRepositoty categoryRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCategories() {
    	Map<String, Object> res = new HashMap<>();
        res.put("status", true);
        res.put("data", categoryRepository.findAll());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCategoryById(@PathVariable int id) {
        Map<String, Object> res = new HashMap<>();
        Optional<CategoryEntity> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            res.put("status", true);
            res.put("data", category.get());
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Danh mục không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable int id, @RequestBody CategoryEntity category) {
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(id);
        Map<String, Object> res = new HashMap<>();
        if (optionalCategory.isPresent()) {
            CategoryEntity existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            existingCategory = categoryRepository.save(existingCategory);
            res.put("status", true);
            res.put("data", existingCategory);
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Danh mục không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategory(@RequestBody CategoryEntity category) {
        Map<String, Object> res = new HashMap<>();
        try {
            CategoryEntity createdCategory = categoryRepository.save(category);
            res.put("status", true);
            res.put("data", createdCategory);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", false);
            res.put("message", "Đã có lỗi xảy ra trong quá trình tạo danh mục");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
}
