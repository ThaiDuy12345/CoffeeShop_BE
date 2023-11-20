package com.duan.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

import com.duan.entity.SupportEntity;
import com.duan.repository.AccountRepository;
import com.duan.repository.SupportRepository;
import com.duan.services.MailService;
import com.duan.utils.EmailTemplate;

@RestController
@RequestMapping("/supports")
public class SupportController {

	@Autowired
	private SupportRepository supportRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private MailService mailService;

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
	public ResponseEntity<Map<String, Object>> createSupport(@RequestBody SupportEntity support) {
		Map<String, Object> res = new HashMap<>();
		try {
			if (!accountRepository.existsByAccountPhone(support.getAccountEntity().getAccountPhone())) {
				res.put("status", false);
				res.put("message", "Tài khoản không tồn tại");
				return ResponseEntity.ok(res);
			}
			support.setAccountEntity(accountRepository.findById(support.getAccountEntity().getAccountPhone()).get());
			supportRepository.save(support);
			res.put("status", true);
			res.put("data", support);
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
			existingSupport.setSupportStatus(support.isSupportStatus());
			supportRepository.save(existingSupport);
			sendMailToClient(existingSupport);
			res.put("status", true);
			res.put("data", existingSupport);
			return ResponseEntity.ok(res);
		} else {
			res.put("status", false);
			res.put("message", "Đơn hỗ trợ không tồn tại");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
		}
	}

	private void sendMailToClient(SupportEntity support) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mailService.sendHTMLEmail(
						support.getAccountEntity().getAccountEmail(),
						"Thông báo về thư hỗ trợ đến từ hệ thống CoffeeShop",
						EmailTemplate.getEmailTemplate(messageBuilder(support)));
			}
		}).start();

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> deleteSupport(@PathVariable int id) {
		Optional<SupportEntity> optionalSupport = supportRepository.findById(id);
		Map<String, Object> res = new HashMap<>();
		if (optionalSupport.isPresent()) {
			supportRepository.delete(optionalSupport.get());
			res.put("status", true);
			res.put("data", true);
			return ResponseEntity.ok(res);
		} else {
			res.put("status", false);
			res.put("message", "Đơn hỗ trợ không tồn tại");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
		}
	}

	private String messageBuilder(SupportEntity support) {
		return (
			"<p>Xin chào,  </p>\n" 
			+
			"<p>Cảm ơn bạn đã liên hệ với chúng tôi tại CoffeeShop và đã chia sẻ về vấn đề mà bạn gặp phải. Chúng tôi rất xin lỗi về sự bất tiện bạn đã phải trải qua.</p>\n"
			+
			"<p>Chúng tôi đã tiếp nhận thông tin của bạn và đã xử lý vấn đề một cách nhanh chóng. Dưới đây là phản hồi từ bạn:</p>\n"
			+
			"<b>" + support.getSupportContent() + "</b><br/><br/>" 
			+
			"<p>CoffeeShop xin gửi lời xin lỗi chân thành về những vấn đề không mong muốn và chúng tôi đã tiến hành xử lý nhanh chóng để khắc phục vấn đề. Chúng tôi luôn cố gắng nâng cao chất lượng dịch vụ để mang lại trải nghiệm tốt nhất cho khách hàng.</p>\n"
			+
			"<p>Nếu bạn cần thêm thông tin hoặc có bất kỳ câu hỏi nào khác, vui lòng liên hệ với chúng tôi. Chúng tôi luôn sẵn lòng hỗ trợ bạn.</p>\n"
			+
			"<p>Chân thành cảm ơn bạn đã ủng hộ CoffeeShop. Chúc bạn một ngày tốt lành!</p><br/>\n"
			+
			"<p>Trân trọng, đội ngũ Coffee Shop</p>\n"
		);
	}

}