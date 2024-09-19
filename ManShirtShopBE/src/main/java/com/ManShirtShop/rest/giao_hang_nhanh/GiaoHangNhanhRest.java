package com.ManShirtShop.rest.giao_hang_nhanh;

import java.net.URI;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.transaction.Transactional;

import org.checkerframework.checker.units.qual.h;
import org.checkerframework.checker.units.qual.m;
import org.checkerframework.checker.units.qual.s;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.ManShirtShop.common.contans.OrderContant;
import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.contact.ContactResponse;
import com.ManShirtShop.dto.ghn.Items;
import com.ManShirtShop.dto.order_the_store.OrderResponeAdmin;
import com.ManShirtShop.entities.Order;
import com.ManShirtShop.entities.OrderDetail;
import com.ManShirtShop.entities.ProductDetail;
import com.ManShirtShop.repository.ContactRepository;
import com.ManShirtShop.repository.OderRepository;
import com.ManShirtShop.repository.ProductDetailRepository;
import com.ManShirtShop.repository.ProductRepository;
import com.ManShirtShop.service.email.SendEmailService;
import com.ManShirtShop.service.oder.OrderServiceAdmin;
import com.google.gson.JsonObject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "ghn/api")
@Tag(name = "Giao Hàng Nhanh api")
public class GiaoHangNhanhRest {
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
    OderRepository oderRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    OrderServiceAdmin orderServiceAdmin;

    @Autowired
    GhnService ghnService;

    @Operation(summary = "API Lấy Tỉnh Thành")
    @RequestMapping(value = "/get-province", method = RequestMethod.GET)
    public ResponseEntity<Object> getProvince() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity<Object> httpEntity = new HttpEntity<>("71e076df-0353-11ee-92f3-0e596e5953f1", headers);
        return restTemplate.exchange("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province",
                HttpMethod.GET, httpEntity, Object.class);
    }

    @Operation(summary = "API Lấy mã Quận/Huyện")
    @RequestMapping(value = "/get-district", method = RequestMethod.GET)
    public ResponseEntity<String> getDistrict(@RequestParam Integer provinceId) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district";
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("province_id", provinceId);
        URI finalUrl = builder.build().toUri();
        return restTemplate.exchange(finalUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    @Operation(summary = "API Lấy mã Phường/Xã")
    @RequestMapping(value = "/get-ward", method = RequestMethod.GET)
    public ResponseEntity<String> getWard(@RequestParam Integer wardId) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?";
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("district_id", wardId);
        URI finalUrl = builder.build().toUri();
        return restTemplate.exchange(finalUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    @Operation(summary = "API Lấy gói dịch vụ")
    @RequestMapping(value = "/get-available-services", method = RequestMethod.GET)
    public ResponseEntity<String> getAvailableServices(@RequestParam Integer toDistrict) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services";
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("shop_id", shopid)
                .queryParam("from_district", district)// Mã Quận/Huyện người gửi hàng
                .queryParam("to_district", toDistrict);// Mã Quận/Huyện người nhận hàng.
        URI finalUrl = builder.build().toUri();
        return restTemplate.exchange(finalUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    @Operation(summary = "API Tính phí dịch vụ")
    @RequestMapping(value = "/get-service-fee", method = RequestMethod.GET)
    public ResponseEntity<String> getServiceFee(@RequestParam Integer toDistrict, @RequestParam String toWard) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.set("ShopId", shopid);
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("service_id", service)// id dịch vụ
                .queryParam("to_ward_code", toWard)// Mã Phường/Xã người nhận hàng.
                .queryParam("to_district_id", toDistrict)// Mã Quận/Huyện người nhận hàng.
                // .queryParam("height", 50)// Chiều cao của đơn hàng (cm).
                // .queryParam("length", 20)// Chiều dài của đơn hàng (cm).
                .queryParam("weight", 200)// Khối lượng của đơn hàng (gram).
                // .queryParam("width", 20)// Chiều rộng của đơn hàng (cm).
                .queryParam("cod_failed_amount", 0)// Giá trị giao thất bại thu tiền .
                .queryParam("insurance_value", 0);// Giá trị của đơn hàng ( Trường hợp mất hàng, bể hàng sẽ đền
                                                  // theo
        // giá trị của đơn hàng).Tối đa 5.000.000
        URI finalUrl = builder.build().toUri();
        return restTemplate.exchange(finalUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    @Operation(summary = "API Tính thời gian dự kiến giao")
    @RequestMapping(value = "/get-leadtime", method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> getleadtime(@RequestParam Integer toDistrict,
            @RequestParam String toWard) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/leadtime";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", "71e076df-0353-11ee-92f3-0e596e5953f1");
        headers.set("ShopId", "124507");
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("from_district_id", district)// Quận/Huyện của người gửi hàng.
                .queryParam("from_ward_code", ward)// Phường/Xã của người gửi hàng.
                .queryParam("to_district_id", toDistrict)// Quận/Huyện của người nhận hàng.
                .queryParam("to_ward_code", toWard).// Phường/Xã của người nhận hàng
                queryParam("service_id", service);
        URI finalUrl = builder.build().toUri();
        ResponseEntity<Map> json =  restTemplate.exchange(finalUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        Map<Object, Object> responseCopde = json.getBody();
        Map<Object, Object> code = (Map<Object, Object>) responseCopde.get("data");
        String time = code.get("leadtime").toString();
        Date date = new Date(Long.parseLong(time) * 1000);
        Format format = new SimpleDateFormat("yyyy/MM/dd");
        Map<String,Object> map = new HashMap<>();
        map.put("code",200);
        map.put("message","OK");
        map.put("data",format.format(date));
        return ResponseEntity.ok(map);
    }

    @Operation(summary = "API in vận đơn")
    @RequestMapping(value = "/in-vdan-don", method = RequestMethod.GET)
    public ResponseEntity<String> getleadtime1(@RequestParam String code) {
        return ResponseEntity.ok(ghnService.getprintA5(code));
    }
}
