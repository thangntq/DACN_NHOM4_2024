package com.ManShirtShop.rest.vnpay.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.ManShirtShop.entities.Order;
import com.ManShirtShop.repository.OderRepository;
import com.ManShirtShop.rest.vnpay.config.ConfigVnpay;
import com.ManShirtShop.rest.vnpay.service.VnpayCreate;
import com.google.api.Http;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.ObjectInputFilter.Config;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping(value = "api/client/vnpay")
@Tag(name = "Api VNPAY")
public class VnPayController {

    // String ridirectSuccess = "http://localhost:4200/order/success";
    String ridirectFail = "https://newmenstore.com/order/fail";

    @Autowired
    VnpayCreate vnpayCreate;

    @CrossOrigin("https://sandbox.vnpayment.vn")
    @GetMapping("resp-vnpay")
    public String getResponsePayment(@RequestParam Map<Object, Object> params, HttpServletRequest request) {
        String url;
        String code = params.get("vnp_TxnRef").toString();
        try {
            url = vnpayCreate.generateUrlResp(params, request);
        } catch (Exception e) {
            e.printStackTrace();
            url = ridirectFail + "?bill_code=" + code;
        }
        return "redirect:" + url;
    }

    // @Autowired
    // private OderRepository oderRepository;

    // @GetMapping(value = "create")
    // public RedirectView create(@RequestParam Integer idOrder) throws
    // UnsupportedEncodingException {
    // Optional<Order> order = oderRepository.findById(idOrder);
    // if (order == null || order.isEmpty()) {
    // throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy
    // Order",null);
    // }

    // String vnp_Version = "2.1.0";
    // String vnp_Command = "pay";
    // // String orderType = req.getParameter("ordertype");
    // // long amount = Integer.parseInt(req.getParameter("amount")) * 100;
    // // String bankCode = req.getParameter("bankCode");
    // Integer total = (int) order.get().getTotal();
    // String vnp_TxnRef = order.get().getCode();
    // // String vnp_IpAddr = Config.getIpAddress(req);
    // String vnp_TmnCode = ConfigVnpay.vnp_TmnCode;
    // // long amount = 132101200;
    // Map<String, String> vnp_Params = new HashMap<>();
    // vnp_Params.put("vnp_Version", vnp_Version);
    // vnp_Params.put("vnp_Command", vnp_Command);
    // vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
    // vnp_Params.put("vnp_Amount", String.valueOf(total * 100));
    // vnp_Params.put("vnp_CurrCode", "VND");

    // // if (bankCode != null && !bankCode.isEmpty()) {
    // // vnp_Params.put("vnp_BankCode", bankCode);
    // // }
    // vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
    // vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
    // vnp_Params.put("vnp_OrderType", "other");

    // // String locate = req.getParameter("language");
    // // if (locate != null && !locate.isEmpty()) {
    // // vnp_Params.put("vnp_Locale", locate);
    // // } else {
    // vnp_Params.put("vnp_Locale", "vn");
    // // }
    // vnp_Params.put("vnp_ReturnUrl", ConfigVnpay.vnp_Returnurl);
    // vnp_Params.put("vnp_IpAddr", getUserIP());
    // System.out.println(getUserIP());
    // Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
    // SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    // String vnp_CreateDate = formatter.format(cld.getTime());
    // vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

    // cld.add(Calendar.MINUTE, 15);
    // String vnp_ExpireDate = formatter.format(cld.getTime());
    // vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

    // List fieldNames = new ArrayList(vnp_Params.keySet());
    // Collections.sort(fieldNames);
    // StringBuilder hashData = new StringBuilder();
    // StringBuilder query = new StringBuilder();
    // Iterator itr = fieldNames.iterator();
    // while (itr.hasNext()) {
    // String fieldName = (String) itr.next();
    // String fieldValue = (String) vnp_Params.get(fieldName);
    // if ((fieldValue != null) && (fieldValue.length() > 0)) {
    // // Build hash data
    // hashData.append(fieldName);
    // hashData.append('=');
    // hashData.append(URLEncoder.encode(fieldValue,
    // StandardCharsets.US_ASCII.toString()));
    // // Build query
    // query.append(URLEncoder.encode(fieldName,
    // StandardCharsets.US_ASCII.toString()));
    // query.append('=');
    // query.append(URLEncoder.encode(fieldValue,
    // StandardCharsets.US_ASCII.toString()));
    // if (itr.hasNext()) {
    // query.append('&');
    // hashData.append('&');
    // }
    // }
    // }
    // String queryUrl = query.toString();
    // String vnp_SecureHash = ConfigVnpay.hmacSHA512(ConfigVnpay.vnp_HashSecret,
    // hashData.toString());
    // queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
    // String paymentUrl = ConfigVnpay.vnp_PayUrl + "?" + queryUrl;
    // // com.google.gson.JsonObject job = new JsonObject();
    // // job.addProperty("code", "00");
    // // job.addProperty("message", "success");
    // // job.addProperty("data", paymentUrl);
    // // Gson gson = new Gson();
    // // resp.getWriter().write(gson.toJson(job));
    // return new RedirectView(paymentUrl);
    // }

    // public String getUserIP() {
    // String ipAddress = request.getHeader("X-Forwarded-For");
    // if (ipAddress == null || ipAddress.isEmpty() ||
    // "unknown".equalsIgnoreCase(ipAddress)) {
    // ipAddress = request.getRemoteAddr();
    // }
    // return ipAddress;
    // }
}
