package com.duan.controller;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.duan.entity.FeedbackEntity;
import com.duan.entity.FeedbackId;
import com.duan.repository.AccountRepository;
import com.duan.repository.FeedbackRepository;
import com.duan.repository.OrderingRepository;
import com.duan.repository.ProductRepository;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

	@Autowired
	private FeedbackRepository feedbackRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private ProductRepository productRepository; 

  @Autowired
  private OrderingRepository orderingRepository;

	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllFeedback() {
		Map<String, Object> res = new HashMap<>();
    try{
      res.put("status", true);
      res.put("data", feedbackRepository.findAll());
      return ResponseEntity.ok(res);
    }catch(Exception e){
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
	}

  @GetMapping("/account/{accountPhone}")
	public ResponseEntity<Map<String, Object>> getAllFeedbackByAccountPhone(@PathVariable String accountPhone) {
		Map<String, Object> res = new HashMap<>();
    try{
      res.put("status", true);
      res.put("data", feedbackRepository.findAllByFeedbackIdAccountPhone(accountPhone));
      return ResponseEntity.ok(res);
    }catch(Exception e){
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
	}

  @GetMapping("/product/{productId}")
	public ResponseEntity<Map<String, Object>> getAllFeedbackByProduct(@PathVariable Integer productId) {
		Map<String, Object> res = new HashMap<>();
    try{
      res.put("status", true);
      res.put("data", feedbackRepository.findAllByFeedbackIdProductId(productId));
      return ResponseEntity.ok(res);
    }catch(Exception e){
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
	}

	@GetMapping("/productAndAccount/{productId}/{accountPhone}")
	public ResponseEntity<Map<String, Object>> getFeedbackByProductAndAccount(@PathVariable Integer productId, @PathVariable String accountPhone) {
    Map<String, Object> res = new HashMap<>();
    try{
      res.put("status", true);
      res.put("data", feedbackRepository.findAllByFeedbackIdProductIdAndFeedbackIdAccountPhone(productId, accountPhone));
      return ResponseEntity.ok(res);
    }catch(Exception e){
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
	}

  @GetMapping("/isAllowedToCreateFeedback/{productId}/{accountPhone}")
  public ResponseEntity<Map<String,Object>> getIsAllowedToCreateFeedback(
    @PathVariable Integer productId,
    @PathVariable String accountPhone
  ){
    Map<String, Object> res = new HashMap<>();
    try{
      int orderingQuantity = orderingRepository.countOrderingQuantityFromAnAccountAndProduct(productId, accountPhone);
      if(orderingQuantity > 0){
        res.put("status", true);
        res.put("data", true);
        return ResponseEntity.ok(res);
      }else{
        res.put("status", true);
        res.put("data", false);
        return ResponseEntity.ok(res);
      }
    }catch(Exception e){
      System.out.println(e);
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }

	@PostMapping
	public ResponseEntity<Map<String, Object>> createFeedback(@RequestBody FeedbackEntity feedbackEntity) {
    Map<String, Object> res = new HashMap<String, Object>();
    try{
      if(!accountRepository.existsByAccountPhone(feedbackEntity.getFeedbackId().getAccountPhone())){ 
        res.put("status", false);
        res.put("message", "Số điện thoại không tồn tại trong hệ thống");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      if(!productRepository.existsByProductId(feedbackEntity.getFeedbackId().getProductId())){ 
        res.put("status", false);
        res.put("message", "Mã sản phẩm không tồn tại");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      if(!(feedbackEntity.getFeedbackRate() >= 1 && feedbackEntity.getFeedbackRate() <= 5)){
        res.put("status", false);
        res.put("message", "Số sao đánh giá phải từ 1 tới 5 sao");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      if(feedbackRepository.existsById(feedbackEntity.getFeedbackId())){
        res.put("status", false);
        res.put("message", "Đánh giá đã tồn tại trong hệ thống");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      feedbackRepository.insertNewFeedback(
        feedbackEntity.getFeedbackId().getProductId(), 
        feedbackEntity.getFeedbackId().getAccountPhone(), 
        feedbackEntity.getFeedbackRate(), 
        feedbackEntity.getFeedbackComment()
      );

      res.put("status", true);
      res.put("data", feedbackRepository.findById(feedbackEntity.getFeedbackId()).get());
      return ResponseEntity.ok(res);
    }catch(Exception e){
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình tạo đánh giá");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
	}

	@PutMapping
	public ResponseEntity<Map<String, Object>> updateSupport(@RequestBody FeedbackEntity feedbackEntity) {
    Map<String, Object> res = new HashMap<String, Object>();
    try{
      if(!accountRepository.existsByAccountPhone(feedbackEntity.getFeedbackId().getAccountPhone())){ 
        res.put("status", false);
        res.put("message", "Số điện thoại không tồn tại trong hệ thống");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      if(!productRepository.existsByProductId(feedbackEntity.getFeedbackId().getProductId())){ 
        res.put("status", false);
        res.put("message", "Mã sản phẩm không tồn tại");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      if(!(feedbackEntity.getFeedbackRate() >= 1 && feedbackEntity.getFeedbackRate() <= 5)){
        res.put("status", false);
        res.put("message", "Số sao đánh giá phản hồi phải từ 1 tới 5 sao");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      if(!feedbackRepository.existsById(feedbackEntity.getFeedbackId())){
        res.put("status", false);
        res.put("message", "Đánh giá không tồn tại trong hệ thống");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      feedbackRepository.updateNewFeedback(
        feedbackEntity.getFeedbackRate(), 
        feedbackEntity.getFeedbackComment(),
        feedbackEntity.isFeedbackActive(),
        feedbackEntity.getFeedbackId().getProductId(), 
        feedbackEntity.getFeedbackId().getAccountPhone() 
      );

      res.put("status", true);
      res.put("data", feedbackRepository.findById(feedbackEntity.getFeedbackId()).get());
      return ResponseEntity.ok(res);
    }catch(Exception e){
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình cập nhật đánh giá");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }

  @DeleteMapping("/{productId}/{accountPhone}")
  public ResponseEntity<Map<String, Object>> deleteFeedback(@RequestParam Integer productId, @RequestParam String accountPhone){
    Map<String, Object> res = new HashMap<String, Object>();
    try{
      FeedbackId feedbackId = new FeedbackId();
      feedbackId.setProductId(productId);
      feedbackId.setAccountPhone(accountPhone);
      if(!feedbackRepository.existsById(feedbackId)){
        res.put("status", false);
        res.put("message", "Đánh giá không tồn tại trong hệ thống");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      res.put("status", true);
      res.put("data", true);
      return ResponseEntity.ok(res);
    }catch(Exception e){
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình xóa đánh giá");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }

}
