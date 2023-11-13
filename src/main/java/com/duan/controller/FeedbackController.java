package com.duan.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.duan.entity.FeedbackEntity;
import com.duan.entity.FeedbackId;
import com.duan.entity.ProductEntity;
import com.duan.entity.AccountEntity;
import com.duan.entity.DetailOrderEntity;
import com.duan.repository.AccountRepository;
import com.duan.repository.FeedbackRepository;
import com.duan.repository.ProductRepository;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private FeedbackRepository feedbackRepository;

	@Autowired
	private AccountRepository accountRepository;

	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllFeedback() {
		Map<String, Object> res = new HashMap<>();
		try {
			res.put("status", true);
			res.put("data", feedbackRepository.findAll());
			return ResponseEntity.ok(res);
		} catch (Exception e) {
			res.put("status", false);
			res.put("message", "Đã có lỗi xảy ra");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}

	@GetMapping("/{feedbackId}")
	public ResponseEntity<Map<String, Object>> getFeedbackById(@RequestBody FeedbackEntity feedback) {
		Map<String, Object> res = new HashMap<>();
		Optional<FeedbackEntity> feedbacks = feedbackRepository.findById(feedback.getFeedbackId());
		if (feedbacks.isPresent()) {
			res.put("status", true);
			res.put("data", feedbacks.get());
			return ResponseEntity.ok(res);
		} else {
			res.put("status", false);
			res.put("message", "Thư phản hồi không tồn tại");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
		}
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<Map<String, Object>> getFeedbackByProduct(@PathVariable int productId) {
		Map<String, Object> res = new HashMap<>();
		Optional<ProductEntity> product = productRepository.findById(productId);
		if (product.isPresent()) {
			List<FeedbackEntity> feedback = feedbackRepository.findByProduct(product.get());
			if (!feedback.isEmpty()) {
				res.put("status", true);
				res.put("data", feedback);
				return ResponseEntity.ok(res);
			} else {
				res.put("status", false);
				res.put("message", "Không tìm thấy thư phản hồi trong sản phẩm đã chọn");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
			}
		} else {
			res.put("status", false);
			res.put("message", "Sản phẩm không tồn tại");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
		}
	}

	@GetMapping("/account/{accountPhone}")
	public ResponseEntity<Map<String, Object>> getFeedbackByAccount(@PathVariable String accountPhone) {
		Map<String, Object> res = new HashMap<>();
		Optional<AccountEntity> account = accountRepository.findById(accountPhone);
		if (account.isPresent()) {
			List<FeedbackEntity> feedback = feedbackRepository.findByAccount(account.get());
			if (!feedback.isEmpty()) {
				res.put("status", true);
				res.put("data", feedback);
				return ResponseEntity.ok(res);
			} else {
				res.put("status", false);
				res.put("message", "Không tìm thấy thư phản hồi trong sản phẩm đã chọn");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
			}
		} else {
			res.put("status", false);
			res.put("message", "Tài khoản không tồn tại");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
		}
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> createFeedback(@RequestBody FeedbackEntity newFeedback)
			throws InterruptedException {
		Map<String, Object> res = new HashMap<>();
		try {

			// Kiểm tra xem đã tồn tại chi tiết đơn hàng với orderId và productSizeId tương
			// ứng chưa
			Optional<FeedbackEntity> existingFeedback = feedbackRepository.findById(newFeedback.getFeedbackId());

			if (existingFeedback.isPresent()) {
				int feedbackRate = newFeedback.getFeedbackRate();
				if (feedbackRate < 1 || feedbackRate > 5) {
					res.put("status", false);
					res.put("message", "FeedbackRate phải nằm trong khoảng từ 1 đến 5.");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
				}
				// Kiểm tra xem Account_Phone đã tồn tại
				String accountPhone = newFeedback.getFeedbackId().getAccountPhone();
				boolean accountExists = accountExists(accountPhone);

				if (!accountExists) {
					res.put("status", false);
					res.put("message", "Account_Phone không tồn tại trong hệ thống");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
				}

				feedbackRepository.insertNewFeedback(newFeedback.getFeedbackRate(), newFeedback.getFeedbackComment(),
						newFeedback.getFeedbackId().getProductId(), newFeedback.getFeedbackId().getAccountPhone());
				res.put("status", true);
				res.put("data", existingFeedback);
				return ResponseEntity.ok(res);
			}

			// Lưu chi tiết đơn hàng mới vào cơ sở dữ liệu
			feedbackRepository.insertNewFeedback(newFeedback.getFeedbackRate(), newFeedback.getFeedbackComment(),
					newFeedback.getFeedbackId().getProductId(), newFeedback.getFeedbackId().getAccountPhone());

			FeedbackEntity newFetchFeedback = feedbackRepository.findById(newFeedback.getFeedbackId()).get();

			res.put("status", true);
			res.put("data", newFetchFeedback);
			return ResponseEntity.ok(res);
		} catch (Exception e) {
			res.put("status", false);
			res.put("message", "Đã có lỗi xảy ra trong quá trình tạo thư phản hồi");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}

	@PutMapping
	public ResponseEntity<Map<String, Object>> updateFeedback(@RequestBody FeedbackEntity updatedFeedback) {
		Map<String, Object> res = new HashMap<>();
		try {

			Optional<FeedbackEntity> existingFeedback = feedbackRepository.findById(updatedFeedback.getFeedbackId());

			if (existingFeedback.isPresent()) {
				String accountPhone = updatedFeedback.getFeedbackId().getAccountPhone();
				boolean accountExists = accountExists(accountPhone);

				if (!accountExists) {
					res.put("status", false);
					res.put("message", "Số điện thoại không tồn tại trong hệ thống");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
				}

				int feedbackRate = updatedFeedback.getFeedbackRate();
				if (feedbackRate < 1 || feedbackRate > 5) {
					res.put("status", false);
					res.put("message", "FeedbackRate phải nằm trong khoảng từ 1 đến 5.");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
				}
				FeedbackEntity feedbackToUpdate = existingFeedback.get();

				feedbackToUpdate.setFeedbackRate(updatedFeedback.getFeedbackRate());
				feedbackToUpdate.setFeedbackComment(updatedFeedback.getFeedbackComment());
				feedbackToUpdate.setFeedbackActive(updatedFeedback.isFeedbackActive());

				// Lưu chi tiết đơn hàng đã được cập nhật vào cơ sở dữ liệu
				feedbackRepository.updateFeedback(updatedFeedback.getFeedbackRate(),
						updatedFeedback.getFeedbackComment(), updatedFeedback.isFeedbackActive());

				FeedbackEntity newFetchFeedback = feedbackRepository.findById(updatedFeedback.getFeedbackId()).get();

				res.put("status", true);
				res.put("data", newFetchFeedback);
				return ResponseEntity.ok(res);
			} else {
				res.put("status", false);
				res.put("message", "Thư phản hồi không tồn tại");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
			}
		} catch (Exception e) {
			res.put("status", false);
			res.put("message", "Đã có lỗi xảy ra trong quá trình cập nhật thư phản hồi");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}

	public boolean accountExists(String accountPhone) {
		Optional<AccountEntity> account = accountRepository.findById(accountPhone);
		return account.isPresent();
	}

	@DeleteMapping
	public ResponseEntity<Map<String, Object>> deleteFeedback(@RequestBody FeedbackId feedbackId) {
		Map<String, Object> res = new HashMap<>();

		try {
			Optional<FeedbackEntity> existingFeedback = feedbackRepository.findById(feedbackId);

			if (!existingFeedback.isPresent()) {
				res.put("status", false);
				res.put("message", "Thư phản hồi không tồn tại");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
			}
			feedbackRepository.deleteById(existingFeedback.get().getFeedbackId());

			res.put("status", true);
			res.put("message", "Xoá thư phản hồi thành công");
			return ResponseEntity.ok(res);
		} catch (Exception e) {
			res.put("status", false);
			res.put("message", "Xoá thư phản hồi không thành công");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}
}
