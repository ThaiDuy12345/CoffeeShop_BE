package com.duan.controller;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;



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

import com.duan.entity.OrderingEntity;
import com.duan.entity.SupportEntity;
import com.duan.repository.AccountRepository;
import com.duan.repository.DiscountRepository;
import com.duan.repository.OrderingRepository;
import com.duan.services.MailService;
import com.duan.utils.EmailTemplate;

@RestController
@RequestMapping("/orderings")
public class OrderingController {

    @Autowired
    private OrderingRepository orderingRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
	private MailService mailService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    // GET /orders
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrder() {
        Map<String, Object> res = new HashMap<>();
        res.put("status", true);
        res.put("data", orderingRepository.findAll());
        return ResponseEntity.ok(res);
    }

    // GET /orders
    @GetMapping("/account/{accountPhone}")
    public ResponseEntity<Map<String, Object>> getByAccountPhoneAndStatus(@PathVariable String accountPhone) {
        Map<String, Object> res = new HashMap<>();
        try{
            List<OrderingEntity> orders = orderingRepository.findAllByOrderingStatusAndAccountEntity_AccountPhone(0, accountPhone);
            if(orders.isEmpty()){
                OrderingEntity newOrder = new OrderingEntity();
                newOrder.setAccountEntity(accountRepository.findById(accountPhone).get());
                newOrder = orderingRepository.save(newOrder);
                res.put("status", true);
                res.put("data", newOrder);
                return ResponseEntity.ok(res);
            }else{
                res.put("status", true);
                res.put("data", orders.get(0));
                return ResponseEntity.ok(res);
            }
        }catch(Exception e){
            res.put("status", false);
            res.put("message", "Đã có lỗi xảy ra trong quá trình lấy thông tin giỏ hàng");
            return ResponseEntity.ok(res);
        }
    }

    // GET /orders
    @GetMapping("/getAllByAccount/{accountPhone}")
    public ResponseEntity<Map<String, Object>> getAllByAccountPhone(@PathVariable String accountPhone) {
        Map<String, Object> res = new HashMap<>();
        try{
            List<OrderingEntity> orders = orderingRepository.findAllByAccountEntity_AccountPhone(accountPhone);
            res.put("status", true);
            res.put("data", orders);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            res.put("status", false);
            res.put("message", "Đã có lỗi xảy ra trong quá trình lấy thông tin lịch sử đơn hàng");
            return ResponseEntity.ok(res);
        }
    }

    // GET /orders/{id}
    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable int orderId) {
        Map<String, Object> res = new HashMap<>();
        Optional<OrderingEntity> order = orderingRepository.findById(orderId);
        if (order.isPresent()) {
            res.put("status", true);
            res.put("data", order.get());
            return ResponseEntity.ok(res);
        } else {
            res.put("status", false);
            res.put("message", "Hóa đơn không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

 // PUT /orders/{id}
    @PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateOrder(@PathVariable int id, @RequestBody OrderingEntity updatedOrder) {
        Optional<OrderingEntity> existingOrder = orderingRepository.findById(id);
        Map<String, Object> res = new HashMap<>();

        if (existingOrder.isPresent()) {
        	OrderingEntity orderToUpdate = existingOrder.get();
            
            // Kiểm tra coi thử nhân viên duyệt đơn có đúng với nhân duyệt đơn trước đó đã duyệt hoặc là admin
            if (
                orderToUpdate.getUpdatedByAccountEntity() != null
            ) {
                if(updatedOrder.getUpdatedByAccountEntity() == null){
                    res.put("status", false);
                    res.put("message", "Không tìm thấy nhân viên duyệt đơn hàng");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
                }

                if(
                    !(  
                        (
                            orderToUpdate.getOrderingStatus() != 0 && 
                            orderToUpdate.getOrderingStatus() != 1 &&
                            orderToUpdate.getUpdatedByAccountEntity().getAccountPhone().equals(
                                updatedOrder.getUpdatedByAccountEntity().getAccountPhone()
                            )
                        ) ||
                        accountRepository.findById(updatedOrder.getUpdatedByAccountEntity().getAccountPhone()).get().getAccountRole() == 0
                    )
                ) {
                    res.put("status", false);
                    res.put("message", "Bạn không quyền thao tác đơn hàng, chỉ có admin hoặc nhân viên duyệt đơn mới có thể thao tác");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
                }
            }
            // Cập nhật thông tin hóa đơn với dữ liệu từ payload body
            if(updatedOrder.getOrderingStatus() == 1 && orderToUpdate.getOrderingStatus() == 0){
                orderToUpdate.setOrderingCreationDate(new Date());
            }
            orderToUpdate.setOrderingStatus(updatedOrder.getOrderingStatus());
            orderToUpdate.setOrderingPaymentStatus(updatedOrder.getOrderingPaymentStatus());
            orderToUpdate.setOrderingShippingFee(updatedOrder.getOrderingShippingFee());
            orderToUpdate.setOrderingNote(
                updatedOrder.getOrderingNote() != null ? 
                    updatedOrder.getOrderingNote() 
                        : 
                    orderToUpdate.getOrderingNote()
            );

            if(updatedOrder.getUpdatedByAccountEntity() != null){
                orderToUpdate.setUpdatedByAccountEntity(
                    accountRepository.findById(
                        updatedOrder.getUpdatedByAccountEntity().getAccountPhone()
                    ).get()
                );
            }

            if(updatedOrder.getDiscountEntity() != null){
                orderToUpdate.setDiscountEntity(
                    discountRepository.findById(
                        updatedOrder.getDiscountEntity().getDiscountId()
                    ).get()
                );
            }
            
            orderToUpdate.setOrderingCancelDescription(
                updatedOrder.getOrderingCancelDescription() != null ? 
                    updatedOrder.getOrderingCancelDescription() 
                        : 
                    orderToUpdate.getOrderingCancelDescription()
            );
            orderToUpdate.setOrderingApproveDescription(
                updatedOrder.getOrderingApproveDescription() != null ? 
                    updatedOrder.getOrderingApproveDescription() 
                        : 
                    orderToUpdate.getOrderingApproveDescription()
            );

            try {
                orderToUpdate = orderingRepository.saveAndFlush(orderToUpdate);
                switch (orderToUpdate.getOrderingStatus()) {
                    case 1:
                        sendSuccessfullyOrderingEmail(orderToUpdate);
                        break;
                    case 2:
                        sendSuccessfullyApproveEmail(orderToUpdate);
                        break;
                    case 3:
                        sendSuccessfullyHandOverToShipperEmail(orderToUpdate);
                        break;
                    case 4:
                        sendSuccessfullyEmail(orderToUpdate);
                        break;
                    case -1:
                        sendCancelOrder(orderToUpdate);
                        break;
                    default:
                        break;
                }
                res.put("status", true);
                res.put("data", orderToUpdate);
                return ResponseEntity.ok(res);
            } catch (Exception e) {
                res.put("status", false);
                res.put("message", "Đã có lỗi xảy ra trong quá trình cập nhật hóa đơn");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
            }
        } else {
            res.put("status", false);
            res.put("message", "Hóa đơn không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }


    // POST /orders
    @PostMapping("/{accountPhone}")
    public ResponseEntity<Map<String, Object>> createOrder(@PathVariable String accountPhone, @RequestBody OrderingEntity newOrder) {
        Map<String, Object> res = new HashMap<>();
        try {
            if(!accountRepository.existsByAccountPhone(accountPhone)){
                res.put("status", false);
                res.put("message", "Số điện thoại tài khoản không tồn tại");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
            }
            // Lưu hóa đơn mới vào cơ sở dữ liệu
            newOrder.setAccountEntity(accountRepository.findById(accountPhone).get());
            OrderingEntity order = orderingRepository.save(newOrder);

            res.put("status", true);
            res.put("data", orderingRepository.findById(order.getOrderingID()));
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", false);
            res.put("message", "Đã có lỗi xảy ra trong quá trình tạo hóa đơn");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    private void sendSuccessfullyOrderingEmail(OrderingEntity ordering){
        String content = "<p>Xin chào,  </p>\n" 
        +
        "<p>Đơn hàng của bạn đã được đặt thành công<p>\n"
        +
        "<p>Xin vui lòng chờ tầm 15 phút để chúng tôi chuẩn bị đơn hàng của bạn</p>\n"
        +
        "<b>Thông tin đơn hàng:</b><br/><br/>" 
        +
        "<p>" 
        +
        "Mã đơn hàng: #" + ordering.getOrderingID() + "<br/>"
        +
        "Ngày đặt: " + dateFormat.format(ordering.getOrderingCreationDate()) + "<br/>"
        +
        "Tổng tiền đơn hàng: " + (String.format("%.0f", ordering.getOrderingPrice()) + "đ") + "<br/>"
        +
        "Tiền ship: " + (String.format("%.0f", ordering.getOrderingShippingFee())+ "đ") + "<br/>"
        +
        "Giảm giá: " + ((ordering.getDiscountEntity() != null && ordering.getDiscountEntity().getDiscountCode().length() > 0) ? (String.format("%.0f", ordering.getDiscountEntity().getDiscountAmount()) + "đ") : "0đ") + "<br/>"
        +
        "Tổng thanh toán: " + (String.format("%.0f", ordering.getOrderingTotalPrice()) + "đ")
        +
        "</p>\n"
        +
        "<p>Nếu bạn cần thêm thông tin hoặc có bất kỳ câu hỏi nào khác, vui lòng liên hệ với chúng tôi. Chúng tôi luôn sẵn lòng hỗ trợ bạn.</p>\n"
        +
        "<p>Chân thành cảm ơn bạn đã ủng hộ CoffeeShop. Chúc bạn một ngày tốt lành!</p><br/>\n"
        +
        "<p>Trân trọng, đội ngũ Coffee Shop</p>\n";
        sendMailToClient(ordering, content);
    }

    private void sendSuccessfullyApproveEmail(OrderingEntity ordering){
        String content = "<p>Xin chào,  </p>\n" 
        +
        "<p>Đơn hàng của bạn đã được duyệt và đang được chuẩn bị<p>\n"
        +
        "<b>Thông tin đơn hàng:</b><br/><br/>" 
        +
        "<p>" 
        +
        "Mã đơn hàng: #" + ordering.getOrderingID() + "<br/>"
        +
        "Ngày đặt: " + dateFormat.format(ordering.getOrderingCreationDate()) + "<br/>"
        +
        "Tổng tiền đơn hàng: " + (String.format("%.0f", ordering.getOrderingPrice()) + "đ") + "<br/>"
        +
        "Tiền ship: " + (String.format("%.0f", ordering.getOrderingShippingFee())+ "đ") + "<br/>"
        +
        "Giảm giá: " + ((ordering.getDiscountEntity() != null && ordering.getDiscountEntity().getDiscountCode().length() > 0) ? (String.format("%.0f", ordering.getDiscountEntity().getDiscountAmount()) + "đ") : "0đ") + "<br/>"
        +
        "Tổng thanh toán: " + (String.format("%.0f", ordering.getOrderingTotalPrice()) + "đ")
        +
        "</p>\n"
        +
        "<p>Nếu bạn cần thêm thông tin hoặc có bất kỳ câu hỏi nào khác, vui lòng liên hệ với chúng tôi. Chúng tôi luôn sẵn lòng hỗ trợ bạn.</p>\n"
        +
        "<p>Chân thành cảm ơn bạn đã ủng hộ CoffeeShop. Chúc bạn một ngày tốt lành!</p><br/>\n"
        +
        "<p>Trân trọng, đội ngũ Coffee Shop</p>\n";

        sendMailToClient(ordering, content);
    }

    private void sendSuccessfullyHandOverToShipperEmail(OrderingEntity ordering){
        String content = "<p>Xin chào,  </p>\n" 
        +
        "<p>Đơn hàng của bạn đã được bàn giao cho đội vận chuyển, xin hãy chú ý điện thoại để shipper liên lạc.<p>\n"
        +
        "<b>Thông tin đơn hàng:</b><br/><br/>" 
        +
        "<p>" 
        +
        "Mã đơn hàng: #" + ordering.getOrderingID() + "<br/>"
        +
        "Ngày đặt: " + dateFormat.format(ordering.getOrderingCreationDate()) + "<br/>"
        +
        "Tổng tiền đơn hàng: " + (String.format("%.0f", ordering.getOrderingPrice()) + "đ") + "<br/>"
        +
        "Tiền ship: " + (String.format("%.0f", ordering.getOrderingShippingFee())+ "đ") + "<br/>"
        +
        "Giảm giá: " + ((ordering.getDiscountEntity() != null && ordering.getDiscountEntity().getDiscountCode().length() > 0) ? (String.format("%.0f", ordering.getDiscountEntity().getDiscountAmount()) + "đ") : "0đ") + "<br/>"
        +
        "Tổng thanh toán: " + (String.format("%.0f", ordering.getOrderingTotalPrice()) + "đ")
        +
        "</p>\n"
        +
        "<p>Nếu bạn cần thêm thông tin hoặc có bất kỳ câu hỏi nào khác, vui lòng liên hệ với chúng tôi. Chúng tôi luôn sẵn lòng hỗ trợ bạn.</p>\n"
        +
        "<p>Chân thành cảm ơn bạn đã ủng hộ CoffeeShop. Chúc bạn một ngày tốt lành!</p><br/>\n"
        +
        "<p>Trân trọng, đội ngũ Coffee Shop</p>\n";

        sendMailToClient(ordering, content);
    }

    private void sendSuccessfullyEmail(OrderingEntity ordering){
        String content = "<p>Xin chào,  </p>\n" 
        +
        "<p>Đơn hàng của bạn đã được giao thành công.<p>\n"
        +
        "<b>Thông tin đơn hàng:</b><br/><br/>" 
        +
        "<p>" 
        +
        "Mã đơn hàng: #" + ordering.getOrderingID() + "<br/>"
        +
        "Ngày đặt: " + dateFormat.format(ordering.getOrderingCreationDate()) + "<br/>"
        +
        "Tổng tiền đơn hàng: " + (String.format("%.0f", ordering.getOrderingPrice()) + "đ") + "<br/>"
        +
        "Tiền ship: " + (String.format("%.0f", ordering.getOrderingShippingFee())+ "đ") + "<br/>"
        +
        "Giảm giá: " + ((ordering.getDiscountEntity() != null && ordering.getDiscountEntity().getDiscountCode().length() > 0) ? (String.format("%.0f", ordering.getDiscountEntity().getDiscountAmount()) + "đ") : "0đ") + "<br/>"
        +
        "Tổng thanh toán: " + (String.format("%.0f", ordering.getOrderingTotalPrice()) + "đ")
        +
        "</p>\n"
        +
        "<p>Nếu bạn cần thêm thông tin hoặc có bất kỳ câu hỏi nào khác, vui lòng liên hệ với chúng tôi. Chúng tôi luôn sẵn lòng hỗ trợ bạn.</p>\n"
        +
        "<p>Chân thành cảm ơn bạn đã ủng hộ CoffeeShop. Chúc bạn một ngày tốt lành!</p><br/>\n"
        +
        "<p>Trân trọng, đội ngũ Coffee Shop</p>\n";

        sendMailToClient(ordering, content);
    }

    private void sendCancelOrder(OrderingEntity ordering){
        String content = "<p>Xin chào,  </p>\n" 
        +
        "<p>Đơn hàng của bạn đã bị hủy<p>\n"
        +
        "<b>Thông tin đơn hàng:</b><br/><br/>"  
        +
        "<p>" 
        +
        "Mã đơn hàng: #" + ordering.getOrderingID() + "<br/>"
        +
        "Ngày đặt: " + dateFormat.format(ordering.getOrderingCreationDate()) + "<br/>"
        +
        "Tổng tiền đơn hàng: " + (String.format("%.0f", ordering.getOrderingPrice()) + "đ") + "<br/>"
        +
        "Tiền ship: " + (String.format("%.0f", ordering.getOrderingShippingFee())+ "đ") + "<br/>"
        +
        "Giảm giá: " + ((ordering.getDiscountEntity() != null && ordering.getDiscountEntity().getDiscountCode().length() > 0) ? (String.format("%.0f", ordering.getDiscountEntity().getDiscountAmount()) + "đ") : "0đ") + "<br/>"
        +
        "Tổng thanh toán: " + (String.format("%.0f", ordering.getOrderingTotalPrice()) + "đ")
        +
        "</p>\n"
        +
        "<p>Nếu bạn cần thêm thông tin hoặc có bất kỳ câu hỏi nào khác, vui lòng liên hệ với chúng tôi. Chúng tôi luôn sẵn lòng hỗ trợ bạn.</p>\n"
        +
        "<p>Chân thành cảm ơn bạn đã ủng hộ CoffeeShop. Chúc bạn một ngày tốt lành!</p><br/>\n"
        +
        "<p>Trân trọng, đội ngũ Coffee Shop</p>\n";

        sendMailToClient(ordering, content);
    }

    private void sendMailToClient(OrderingEntity ordering, String content) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mailService.sendHTMLEmail(
                    ordering.getAccountEntity().getAccountEmail(),
                    "Thông báo về thư hỗ trợ đến từ hệ thống CoffeeShop",
                    EmailTemplate.getEmailTemplate(content)
                );
			}
		}).start();

	}
}