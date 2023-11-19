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
						messageBuilder(support));
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
		return "<!doctype html>\n" + //
				"<html>\n" + //
				"  <head>\n" + //
				"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" + //
				"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" + //
				"    <title>Simple Transactional Email</title>\n" + //
				"    <style>\n" + //
				"      /* -------------------------------------\n" + //
				"          GLOBAL RESETS\n" + //
				"      ------------------------------------- */\n" + //
				"      \n" + //
				"      /*All the styling goes here*/\n" + //
				"      \n" + //
				"      img {\n" + //
				"        border: none;\n" + //
				"        -ms-interpolation-mode: bicubic;\n" + //
				"        max-width: 100%; \n" + //
				"      }\n" + //
				"\n" + //
				"      body {\n" + //
				"        background-color: #f6f6f6;\n" + //
				"        font-family: sans-serif;\n" + //
				"        -webkit-font-smoothing: antialiased;\n" + //
				"        font-size: 14px;\n" + //
				"        line-height: 1.4;\n" + //
				"        margin: 0;\n" + //
				"        padding: 0;\n" + //
				"        -ms-text-size-adjust: 100%;\n" + //
				"        -webkit-text-size-adjust: 100%; \n" + //
				"      }\n" + //
				"\n" + //
				"      table {\n" + //
				"        border-collapse: separate;\n" + //
				"        mso-table-lspace: 0pt;\n" + //
				"        mso-table-rspace: 0pt;\n" + //
				"        width: 100%; }\n" + //
				"        table td {\n" + //
				"          font-family: sans-serif;\n" + //
				"          font-size: 14px;\n" + //
				"          vertical-align: top; \n" + //
				"      }\n" + //
				"\n" + //
				"      /* -------------------------------------\n" + //
				"          BODY & CONTAINER\n" + //
				"      ------------------------------------- */\n" + //
				"\n" + //
				"      .body {\n" + //
				"        background-color: #f6f6f6;\n" + //
				"        width: 100%; \n" + //
				"      }\n" + //
				"\n" + //
				"      /* Set a max-width, and make it display as block so it will automatically stretch to that width, but will also shrink down on a phone or something */\n"
				+ //
				"      .container {\n" + //
				"        display: block;\n" + //
				"        margin: 0 auto !important;\n" + //
				"        /* makes it centered */\n" + //
				"        max-width: 580px;\n" + //
				"        padding: 10px;\n" + //
				"        width: 580px; \n" + //
				"      }\n" + //
				"\n" + //
				"      /* This should also be a block element, so that it will fill 100% of the .container */\n" + //
				"      .content {\n" + //
				"        box-sizing: border-box;\n" + //
				"        display: block;\n" + //
				"        margin: 0 auto;\n" + //
				"        max-width: 580px;\n" + //
				"        padding: 10px; \n" + //
				"      }\n" + //
				"\n" + //
				"      /* -------------------------------------\n" + //
				"          HEADER, FOOTER, MAIN\n" + //
				"      ------------------------------------- */\n" + //
				"      .main {\n" + //
				"        background: #ffffff;\n" + //
				"        border-radius: 3px;\n" + //
				"        width: 100%; \n" + //
				"      }\n" + //
				"\n" + //
				"      .wrapper {\n" + //
				"        box-sizing: border-box;\n" + //
				"        padding: 20px; \n" + //
				"      }\n" + //
				"\n" + //
				"      .content-block {\n" + //
				"        padding-bottom: 10px;\n" + //
				"        padding-top: 10px;\n" + //
				"      }\n" + //
				"\n" + //
				"      .footer {\n" + //
				"        clear: both;\n" + //
				"        margin-top: 10px;\n" + //
				"        text-align: center;\n" + //
				"        width: 100%; \n" + //
				"      }\n" + //
				"        .footer td,\n" + //
				"        .footer p,\n" + //
				"        .footer span,\n" + //
				"        .footer a {\n" + //
				"          color: #999999;\n" + //
				"          font-size: 12px;\n" + //
				"          text-align: center; \n" + //
				"      }\n" + //
				"\n" + //
				"      /* -------------------------------------\n" + //
				"          TYPOGRAPHY\n" + //
				"      ------------------------------------- */\n" + //
				"      h1,\n" + //
				"      h2,\n" + //
				"      h3,\n" + //
				"      h4 {\n" + //
				"        color: #000000;\n" + //
				"        font-family: sans-serif;\n" + //
				"        font-weight: 400;\n" + //
				"        line-height: 1.4;\n" + //
				"        margin: 0;\n" + //
				"        margin-bottom: 30px; \n" + //
				"      }\n" + //
				"\n" + //
				"      h1 {\n" + //
				"        font-size: 35px;\n" + //
				"        font-weight: 300;\n" + //
				"        text-align: center;\n" + //
				"        text-transform: capitalize; \n" + //
				"      }\n" + //
				"\n" + //
				"      p,\n" + //
				"      ul,\n" + //
				"      ol {\n" + //
				"        font-family: sans-serif;\n" + //
				"        font-size: 14px;\n" + //
				"        font-weight: normal;\n" + //
				"        margin: 0;\n" + //
				"        margin-bottom: 15px; \n" + //
				"      }\n" + //
				"        p li,\n" + //
				"        ul li,\n" + //
				"        ol li {\n" + //
				"          list-style-position: inside;\n" + //
				"          margin-left: 5px; \n" + //
				"      }\n" + //
				"\n" + //
				"      a {\n" + //
				"        color: #3498db;\n" + //
				"        text-decoration: underline; \n" + //
				"      }\n" + //
				"\n" + //
				"      /* -------------------------------------\n" + //
				"          BUTTONS\n" + //
				"      ------------------------------------- */\n" + //
				"      .btn {\n" + //
				"        box-sizing: border-box;\n" + //
				"        width: 100%; }\n" + //
				"        .btn > tbody > tr > td {\n" + //
				"          padding-bottom: 15px; }\n" + //
				"        .btn table {\n" + //
				"          width: auto; \n" + //
				"      }\n" + //
				"        .btn table td {\n" + //
				"          background-color: #ffffff;\n" + //
				"          border-radius: 5px;\n" + //
				"          text-align: center; \n" + //
				"      }\n" + //
				"        .btn a {\n" + //
				"          background-color: #ffffff;\n" + //
				"          border: solid 1px #3498db;\n" + //
				"          border-radius: 5px;\n" + //
				"          box-sizing: border-box;\n" + //
				"          color: #3498db;\n" + //
				"          cursor: pointer;\n" + //
				"          display: inline-block;\n" + //
				"          font-size: 14px;\n" + //
				"          font-weight: bold;\n" + //
				"          margin: 0;\n" + //
				"          padding: 12px 25px;\n" + //
				"          text-decoration: none;\n" + //
				"          text-transform: capitalize; \n" + //
				"      }\n" + //
				"\n" + //
				"      .btn-primary table td {\n" + //
				"        background-color: #3498db; \n" + //
				"      }\n" + //
				"\n" + //
				"      .btn-primary a {\n" + //
				"        background-color: #3498db;\n" + //
				"        border-color: #3498db;\n" + //
				"        color: #ffffff; \n" + //
				"      }\n" + //
				"\n" + //
				"      /* -------------------------------------\n" + //
				"          OTHER STYLES THAT MIGHT BE USEFUL\n" + //
				"      ------------------------------------- */\n" + //
				"      .last {\n" + //
				"        margin-bottom: 0; \n" + //
				"      }\n" + //
				"\n" + //
				"      .first {\n" + //
				"        margin-top: 0; \n" + //
				"      }\n" + //
				"\n" + //
				"      .align-center {\n" + //
				"        text-align: center; \n" + //
				"      }\n" + //
				"\n" + //
				"      .align-right {\n" + //
				"        text-align: right; \n" + //
				"      }\n" + //
				"\n" + //
				"      .align-left {\n" + //
				"        text-align: left; \n" + //
				"      }\n" + //
				"\n" + //
				"      .clear {\n" + //
				"        clear: both; \n" + //
				"      }\n" + //
				"\n" + //
				"      .mt0 {\n" + //
				"        margin-top: 0; \n" + //
				"      }\n" + //
				"\n" + //
				"      .mb0 {\n" + //
				"        margin-bottom: 0; \n" + //
				"      }\n" + //
				"\n" + //
				"      .preheader {\n" + //
				"        color: transparent;\n" + //
				"        display: none;\n" + //
				"        height: 0;\n" + //
				"        max-height: 0;\n" + //
				"        max-width: 0;\n" + //
				"        opacity: 0;\n" + //
				"        overflow: hidden;\n" + //
				"        mso-hide: all;\n" + //
				"        visibility: hidden;\n" + //
				"        width: 0; \n" + //
				"      }\n" + //
				"\n" + //
				"      .powered-by a {\n" + //
				"        text-decoration: none; \n" + //
				"      }\n" + //
				"\n" + //
				"      hr {\n" + //
				"        border: 0;\n" + //
				"        border-bottom: 1px solid #f6f6f6;\n" + //
				"        margin: 20px 0; \n" + //
				"      }\n" + //
				"\n" + //
				"      /* -------------------------------------\n" + //
				"          RESPONSIVE AND MOBILE FRIENDLY STYLES\n" + //
				"      ------------------------------------- */\n" + //
				"      @media only screen and (max-width: 620px) {\n" + //
				"        table.body h1 {\n" + //
				"          font-size: 28px !important;\n" + //
				"          margin-bottom: 10px !important; \n" + //
				"        }\n" + //
				"        table.body p,\n" + //
				"        table.body ul,\n" + //
				"        table.body ol,\n" + //
				"        table.body td,\n" + //
				"        table.body span,\n" + //
				"        table.body a {\n" + //
				"          font-size: 16px !important; \n" + //
				"        }\n" + //
				"        table.body .wrapper,\n" + //
				"        table.body .article {\n" + //
				"          padding: 10px !important; \n" + //
				"        }\n" + //
				"        table.body .content {\n" + //
				"          padding: 0 !important; \n" + //
				"        }\n" + //
				"        table.body .container {\n" + //
				"          padding: 0 !important;\n" + //
				"          width: 100% !important; \n" + //
				"        }\n" + //
				"        table.body .main {\n" + //
				"          border-left-width: 0 !important;\n" + //
				"          border-radius: 0 !important;\n" + //
				"          border-right-width: 0 !important; \n" + //
				"        }\n" + //
				"        table.body .btn table {\n" + //
				"          width: 100% !important; \n" + //
				"        }\n" + //
				"        table.body .btn a {\n" + //
				"          width: 100% !important; \n" + //
				"        }\n" + //
				"        table.body .img-responsive {\n" + //
				"          height: auto !important;\n" + //
				"          max-width: 100% !important;\n" + //
				"          width: auto !important; \n" + //
				"        }\n" + //
				"      }\n" + //
				"\n" + //
				"      /* -------------------------------------\n" + //
				"          PRESERVE THESE STYLES IN THE HEAD\n" + //
				"      ------------------------------------- */\n" + //
				"      @media all {\n" + //
				"        .ExternalClass {\n" + //
				"          width: 100%; \n" + //
				"        }\n" + //
				"        .ExternalClass,\n" + //
				"        .ExternalClass p,\n" + //
				"        .ExternalClass span,\n" + //
				"        .ExternalClass font,\n" + //
				"        .ExternalClass td,\n" + //
				"        .ExternalClass div {\n" + //
				"          line-height: 100%; \n" + //
				"        }\n" + //
				"        .apple-link a {\n" + //
				"          color: inherit !important;\n" + //
				"          font-family: inherit !important;\n" + //
				"          font-size: inherit !important;\n" + //
				"          font-weight: inherit !important;\n" + //
				"          line-height: inherit !important;\n" + //
				"          text-decoration: none !important; \n" + //
				"        }\n" + //
				"        #MessageViewBody a {\n" + //
				"          color: inherit;\n" + //
				"          text-decoration: none;\n" + //
				"          font-size: inherit;\n" + //
				"          font-family: inherit;\n" + //
				"          font-weight: inherit;\n" + //
				"          line-height: inherit;\n" + //
				"        }\n" + //
				"        .btn-primary table td:hover {\n" + //
				"          background-color: #34495e !important; \n" + //
				"        }\n" + //
				"        .btn-primary a:hover {\n" + //
				"          background-color: #34495e !important;\n" + //
				"          border-color: #34495e !important; \n" + //
				"        } \n" + //
				"      }\n" + //
				"\n" + //
				"    </style>\n" + //
				"  </head>\n" + //
				"  <body>\n" + //
				"    <span class=\"preheader\">Phản hồi đến từ hệ thống của COFFEE SHOP.</span>\n" + //
				"    <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"body\">\n" + //
				"      <tr>\n" + //
				"        <td>&nbsp;</td>\n" + //
				"        <td class=\"container\">\n" + //
				"          <div class=\"content\">\n" + //
				"\n" + //
				"            <!-- START CENTERED WHITE CONTAINER -->\n" + //
				"            <table role=\"presentation\" class=\"main\">\n" + //
				"\n" + //
				"              <!-- START MAIN CONTENT AREA -->\n" + //
				"              <tr>\n" + //
				"                <td class=\"wrapper\">\n" + //
				"                  <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" + //
				"                    <tr>\n" + //
				"                      <td>\n" + //
				"                        <p>Xin chào,  </p>\n" + //
				"                        <p>Cảm ơn bạn đã liên hệ với chúng tôi tại CoffeeShop và đã chia sẻ về vấn đề mà bạn gặp phải. Chúng tôi rất xin lỗi về sự bất tiện bạn đã phải trải qua.</p>\n"
				+ //
				"                        <p>Chúng tôi đã tiếp nhận thông tin của bạn và đã xử lý vấn đề một cách nhanh chóng. Dưới đây là phản hồi từ bạn:</p>\n"
				+
				"													<b>" + support.getSupportContent() + "</b><br/><br/>" +
				"                        \n" + //
				"\t\t\t\t\t\t\t\t        <p>CoffeeShop xin gửi lời xin lỗi chân thành về những vấn đề không mong muốn và chúng tôi đã tiến hành xử lý nhanh chóng để khắc phục vấn đề. Chúng tôi luôn cố gắng nâng cao chất lượng dịch vụ để mang lại trải nghiệm tốt nhất cho khách hàng.</p>\n"
				+ //
				"                        <p>Nếu bạn cần thêm thông tin hoặc có bất kỳ câu hỏi nào khác, vui lòng liên hệ với chúng tôi. Chúng tôi luôn sẵn lòng hỗ trợ bạn.</p>\n"
				+ //
				"                        <p>Chân thành cảm ơn bạn đã ủng hộ CoffeeShop. Chúc bạn một ngày tốt lành!</p><br/>\n"
				+ //
				"                        <p>Trân trọng, đội ngũ Coffee Shop</p>\n" + //
				"                      </td>\n" + //
				"                    </tr>\n" + //
				"                  </table>\n" + //
				"                </td>\n" + //
				"              </tr>\n" + //
				"\n" + //
				"            <!-- END MAIN CONTENT AREA -->\n" + //
				"            </table>\n" + //
				"            <!-- END CENTERED WHITE CONTAINER -->\n" + //
				"\n" + //
				"            <!-- START FOOTER -->\n" + //
				"            <div class=\"footer\">\n" + //
				"              <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" + //
				"                <tr>\n" + //
				"                  <td class=\"content-block powered-by\">\n" + //
				"                    Tài trợ bởi COFFEE SHOP</a>.\n" + //
				"                  </td>\n" + //
				"                </tr>\n" + //
				"              </table>\n" + //
				"            </div>\n" + //
				"            <!-- END FOOTER -->\n" + //
				"\n" + //
				"          </div>\n" + //
				"        </td>\n" + //
				"        <td>&nbsp;</td>\n" + //
				"      </tr>\n" + //
				"    </table>\n" + //
				"  </body>\n" + //
				"</html>\n" + //
				"";
	}

}