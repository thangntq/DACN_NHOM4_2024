package com.ManShirtShop.rest.giao_hang_nhanh;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ManShirtShop.Authentication.dto.user.EmailUser;
import com.ManShirtShop.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.ManShirtShop.common.contans.OrderContant;
import com.ManShirtShop.entities.Order;
import com.ManShirtShop.entities.OrderDetail;
import com.ManShirtShop.repository.OderRepository;
import com.ManShirtShop.repository.ProductDetailRepository;
import com.ManShirtShop.service.email.SendEmailService;
import com.ManShirtShop.service.oder.OrderServiceAdmin;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GhnServiceImpl implements GhnService {
    @Value("${ghn.token}")
    private String token;

    @Value("${ghn.shopid}")
    private String shopid;

    @Value("${ghn.shop.district}")
    private String district;

    @Value("${ghn.shop.ward}")
    private String ward;

    @Value("${ghn.ship.service}")
    private Integer service;

    @Autowired
    SendEmailService sendEmail;

    @Autowired
    OderRepository oderRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public String ghncreate(Integer lstId) {
        List<String> lstEmail = new ArrayList<>();
        // String[] arr = new String[100];
        Optional<Order> od = oderRepository.findById(lstId);
        if (od == null || od.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Order");
        }

        // System.out.println("--------------------"+od.get().getId());
        // for (Order order : lstOrder) {
        od.get().setStatus(OrderContant.STATUS_DANG_GIAO);
        od.get().setUpdateTime(Timestamp.from(Instant.now()));
        String fullName = employeeRepository.getFullNameByEmail(EmailUser.getEmailUser());
        od.get().setUpdateBy(fullName);
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ShopId", shopid);
        headers.set("Token", token);
        if (!(od.get().getCustomer().getEmail() == null) || !od.get().getCustomer().getEmail().isBlank()) {
            lstEmail.add(od.get().getCustomer().getEmail());
        }
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("payment_type_id", 1);
        requestBody.put("note", od.get().getNote());
        requestBody.put("required_note", "CHOXEMHANGKHONGTHU");
        requestBody.put("client_order_code", "");
        requestBody.put("to_name", od.get().getShipName());
        requestBody.put("to_phone", od.get().getShipPhone());
        requestBody.put("to_address", od.get().getAddress());
        requestBody.put("to_ward_code", od.get().getIdWard());
        requestBody.put("to_district_id", od.get().getIdDistrict());
        if (od.get().getStatusPay() == OrderContant.CHUA_THANH_TOAN) {
            requestBody.put("cod_amount", (int) od.get().getTotal());
        } else {
            requestBody.put("cod_amount", 0);
        }
        if (!od.get().getNote().isBlank() || !(od.get().getNote() == null) || !od.get().getNote().equals("")) {
            requestBody.put("content", od.get().getNote());
        } else {
            requestBody.put("content", "Khách hàng không chú thích");
        }
        requestBody.put("content", od.get().getNote());
        requestBody.put("weight", 200);
        requestBody.put("length", 1);
        requestBody.put("width", 19);
        requestBody.put("height", 10);
        requestBody.put("cod_failed_amount", 0);
        requestBody.put("deliver_station_id", null);
        requestBody.put("insurance_value", 0);
        requestBody.put("service_id", service);
        List<Map<String, Object>> items = new ArrayList<>();
        for (OrderDetail x : od.get().getOrderDetail()) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", x.getProductDetail().getProduct().getName()
                    + " "
                    + x.getProductDetail().getColor().getName()
                    + " "
                    + x.getProductDetail().getSize().getCode());
            item.put("code", x.getProductDetail().getProduct().getCategory().getName());
            item.put("quantity", x.getQuantity());
            item.put("price", (int) x.getUnitprice());
            items.add(item);
        }
        requestBody.put("items", items);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> json = restTemplate.postForEntity(url, requestEntity, Map.class);
        String[] orderCode = new String[] { get(json, "order_code") };
        System.out.println(orderCode[0]);
        if (lstEmail.isEmpty() || lstEmail.size() > 0) {
            sendEmail.start(lstEmail.get(0),"Đơn hàng của bạn đã được giao đi.Hãy chú ý điện thoại trong những ngày tới nhé!");
        }
        try {
            Order oder = od.get();
            oder.setCodeGhn(get(json, "order_code"));
            oderRepository.save(oder);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dữ liệu không hợp lệ");
        }

        ResponseEntity<Map> res = getTokenOrderCode(orderCode);
        String token = get(res, "token");
        System.out.println(token);
        return "https://dev-online-gateway.ghn.vn/a5/public-api/printA5?token=" + token;
    }

    public String get(ResponseEntity<Map> map, String res) {
        Map<Object, Object> responseCopde = map.getBody();
        Map<Object, Object> code = (Map<Object, Object>) responseCopde.get("data");
        return code.get(res).toString();
    }

    public ResponseEntity<Map> getTokenOrderCode(String[] orderCode) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/a5/gen-token";
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("order_codes", orderCode);
        URI finalUrl = builder.build().toUri();
        return restTemplate.exchange(finalUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
    }

    @Override
    public String getprintA5(String code) {
        String[] orderCode = new String[] { code };
        System.out.println(orderCode[0]);
        ResponseEntity<Map> res = getTokenOrderCode(orderCode);
        String token = get(res, "token");
        String url = "https://dev-online-gateway.ghn.vn/a5/public-api/printA5?token=" + token;
        return url;
    }
}
