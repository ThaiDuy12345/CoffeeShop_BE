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

import com.duan.entity.SupportEntity;
import com.duan.repository.SupportRepository;

@RestController
@RequestMapping("/supports")
public class SupportController {

	@Autowired
	private SupportRepository supportRepository;

	// GET /support
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllSupport() {
		Map<String, Object> res = new HashMap<>();
		res.put("status", true);
		res.put("data", supportRepository.findAll());
		return ResponseEntity.ok(res);
	}

	// GET /support/{id}
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> getSupportById(@PathVariable Integer id) {
		Map<String, Object> res = new HashMap<>();
		Optional<SupportEntity> optionalSupport = supportRepository.findById(id);
		if (optionalSupport.isPresent()) {
			res.put("status", true);
			res.put("data", optionalSupport.get());
			return ResponseEntity.ok(res);
		} else {
			res.put("status", false);
			res.put("message", "Thư hỗ trợ không tồn tại");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
		}
	}

	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> createSupport(@RequestBody SupportEntity support) {
		Map<String, Object> res = new HashMap<>();
		try {
				SupportEntity createdSupport = supportRepository.save(support);
				res.put("status", true);
				res.put("data", createdSupport);
				return ResponseEntity.ok(res);
		} catch (Exception e) {
				res.put("status", false);
				res.put("message", "Đã có lỗi xảy ra trong quá trình tạo đơn hỗ trợ");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}

	// PUT /support/{id}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateSupport(@PathVariable int id, @RequestBody SupportEntity support) {
		Optional<SupportEntity> optionalSupport = supportRepository.findById(id);
		Map<String, Object> res = new HashMap<>();
		if (optionalSupport.isPresent()) {
			SupportEntity existingSupport = optionalSupport.get();
			// Update properties of the existing account
			existingSupport.setSupportContent(support.getSupportContent());
			existingSupport.setSupportReason(support.getSupportReason());
			existingSupport.setSupportTitle(support.getSupportTitle());
			res.put("status", true);
			res.put("data", existingSupport);
			return ResponseEntity.ok(res);
		} else {
			res.put("status", false);
			res.put("message", "Đơn hỗ trợ không tồn tại");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
		}
	}

}
