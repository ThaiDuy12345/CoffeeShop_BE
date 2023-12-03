package com.duan.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duan.repository.AccountRepository;
import com.duan.repository.FeedbackRepository;
import com.duan.repository.OrderingRepository;
import com.duan.repository.ProductRepository;
import com.duan.repository.SupportRepository;

import jakarta.websocket.server.PathParam;
import java.util.Calendar;

@RestController
@RequestMapping("/statistics")
public class StatisticController {

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  OrderingRepository orderingRepository;

  @Autowired
  SupportRepository supportRepository;

  @Autowired
  FeedbackRepository feedbackRepository;

  @GetMapping
  public ResponseEntity<Map<String, Object>> getIndexStatistics() {
    Map<String, Object> res = new HashMap<String, Object>();
    Map<String, Object> data = new HashMap<String, Object>();

    try {
      Integer accountStatistic = accountRepository.countByAccountActive(true);
      Integer productStatistic = productRepository.countByProductActive(true);
      Integer orderingStatistic = orderingRepository.countByOrderingStatus(1);
      Integer supportStatistic = supportRepository.countBySupportStatus(false);

      data.put("accountStatistic", accountStatistic);
      data.put("productStatistic", productStatistic);
      data.put("supportStatistic", supportStatistic);
      data.put("orderingStatistic", orderingStatistic);

      res.put("status", true);
      res.put("data", data);
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      res.put("status", false);
      res.put("message", "Đã xảy ra lỗi trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }

  @GetMapping("/productBySoldQuantity")
  public ResponseEntity<Map<String, Object>> getProductBySoldQuantity() {
    Map<String, Object> res = new HashMap<String, Object>();
    try {
      res.put("status", true);
      res.put("data", productRepository.getProductStatisticsBySoldQuantity());
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }

  @GetMapping("/productByFeedbackQuantity")
  public ResponseEntity<Map<String, Object>> getProductByFeedbackQuantity() {
    Map<String, Object> res = new HashMap<String, Object>();
    try {
      res.put("status", true);
      res.put("data", productRepository.getProductStatisticsByFeedbackQuantity());
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }

  @GetMapping("/statisticChart")
  public ResponseEntity<Map<String, Object>> getStatisticChart(
      @PathParam("type") String type,
      @PathParam("dateType") String dateType) {
    Map<String, Object> res = new HashMap<String, Object>();
    try {
      /*
      * type thống kê có thể là product, ordering, support và feedback
      * Product: sản phẩm bán ra trong 1 khoảng thời gian nào đó
      * Ordering: hóa đơn được đặt trong 1 khoảng thời gian nào đó
      * Support: thư hỗ trợ trong 1 khoảng thời gian nhất định
      * 
      * dateType thống kê có thể là week, month, halfYear, year
      * week: thống kê theo tuần
      * month: thống kê theo tháng
      * halfYear: thống kê theo nửa năm
      * year: thống kê theo năm
      */

      // validate coi thử type và dateType có hợp lệ không
      if (!type.equals("product") && !type.equals("ordering") && !type.equals("support") && !type.equals("feedback")) {
        res.put("status", false);
        res.put("message", "Không tồn tại loại thống kê này");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      if (!dateType.equals("week") && !dateType.equals("month") && !dateType.equals("halfYear")
          && !dateType.equals("year")) {
        res.put("status", false);
        res.put("message", "Không tồn tại loại thời gian thống kê này");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
      }

      res.put("status", true);
      res.put("data", getStatisticData(type, dateType));
      return ResponseEntity.ok(res);

    } catch (Exception e) {
      res.put("status", false);
      res.put("message", "Đã có lỗi xảy ra trong quá trình lấy dữ liệu");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
  }

  private Object getStatisticData(String type, String dateType){
    Date startDate = new Date();
    Date endDate = new Date();

    switch (dateType) {
      case "week":
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, -7);
        startDate = calendar.getTime();
        break;
      case "month":
        calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, -1);
        startDate = calendar.getTime();
        break;
      case "halfYear":
        calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, -6);
        startDate = calendar.getTime();
        break;
      case "year":
        calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, -1);
        startDate = calendar.getTime();
        break;
      default:
        break;
    }

      switch (type) {
        case "product":
          return productRepository.getProductStatisticsBySoldQuantityAndDate(startDate, endDate);
        case "ordering":
          return orderingRepository.getOrderingStatisticsByDate(startDate, endDate);
        case "support":
          return supportRepository.getSupportStatisticsByDate(startDate, endDate);
        case "feedback":
          return feedbackRepository.getFeedbackStatisticsByDate(startDate, endDate);
        default:
          return null;
      }
    }
}
