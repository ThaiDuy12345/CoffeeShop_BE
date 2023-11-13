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

import com.duan.entity.AccountEntity;
import com.duan.entity.DetailOrderEntity;
import com.duan.entity.FeedbackEntity;
import com.duan.entity.SupportEntity;
import com.duan.repository.AccountRepository;
import com.duan.repository.SupportRepository;

@RestController
@RequestMapping("/supports")
public class SupportController {

	@Autowired
	private SupportRepository supportRepository;

	@Autowired
	private AccountRepository accountRepository;

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

	@PostMapping
	public ResponseEntity<Map<String, Object>> createSupport(@RequestBody SupportEntity newSupport) {
		Map<String, Object> res = new HashMap<>();

		try {
			// Kiểm tra xem đã tồn tại chi tiết đơn hàng với supportID tương ứng chưa
			Optional<SupportEntity> existingSupport = supportRepository.findById(newSupport.getSupportID());

			if (existingSupport.isPresent()) {
				// Lấy số điện thoại từ đối tượng newSupport
				String accountPhone = newSupport.getAccountEntity().getAccountPhone();

				// Kiểm tra xem Account_Phone đã tồn tại
				boolean accountExists = accountExists(accountPhone);

				if (!accountExists) {
					res.put("status", false);
					res.put("message", "Số điện thoại không tồn tại trong hệ thống");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
				}

				// Lưu chi tiết đơn hàng mới vào cơ sở dữ liệu
				supportRepository.save(newSupport);

				res.put("status", true);
				res.put("data", newSupport);
				return ResponseEntity.ok(res);
			} else {
				res.put("status", false);
				res.put("message", "Support không tồn tại");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
			}
		} catch (Exception e) {
			res.put("status", false);
			res.put("message", "Đã có lỗi xảy ra trong quá trình tạo thư hỗ trợ");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}

	public boolean accountExists(String accountPhone) {
		Optional<AccountEntity> account = accountRepository.findById(accountPhone);
		return account.isPresent();
	}
	// PUT /support/{id}

	@PutMapping
	public ResponseEntity<Map<String, Object>> updateSupport(@RequestBody SupportEntity updatedSupport)
			throws InterruptedException {
		Map<String, Object> res = new HashMap<>();

		Optional<SupportEntity> existingSupport = supportRepository.findById(updatedSupport.getSupportID());

		if (existingSupport.isPresent()) {
			SupportEntity supportToUpdate = existingSupport.get();

			supportToUpdate.setSupportReason(updatedSupport.getSupportReason());
			supportToUpdate.setSupportTitle(updatedSupport.getSupportTitle());
			supportToUpdate.setSupportContent(updatedSupport.getSupportContent());
			
			// Lưu thư hỗ trợ đã được cập nhật vào cơ sở dữ liệu
			supportRepository.updateSupport(updatedSupport.getSupportReason(), updatedSupport.getSupportTitle(),
					updatedSupport.getSupportContent());

			SupportEntity newFetchSupport = supportRepository.findById(updatedSupport.getSupportID()).get();

			res.put("status", true);
			res.put("data", newFetchSupport);
			return ResponseEntity.ok(res);
		} else {
			res.put("status", false);
			res.put("message", "Thư hỗ trơ không tồn tại");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
		}
	}

}
