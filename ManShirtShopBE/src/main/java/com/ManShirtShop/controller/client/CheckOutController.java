package com.ManShirtShop.controller.client;

import java.util.List;

import javax.transaction.Transactional;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ManShirtShop.dto.client.check_out.ProductDetailCheckOut;
import com.ManShirtShop.dto.client.oderDto.OrderRequestClient;
import com.ManShirtShop.service.client.check_out.CheckOutServiceClient;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "client/api/checkOut")
@Tag(name = "CheckOut Client API")
public class CheckOutController {

    @Autowired
    CheckOutServiceClient checkOutServiceClient;

    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> create(@RequestBody List<ProductDetailCheckOut> lstProductDetail) {
        return ResponseEntity.ok(checkOutServiceClient.create(lstProductDetail));
    }

    @GetMapping(value = "get")
    public ResponseEntity<?> get(@RequestParam String code) {
        return ResponseEntity.ok(checkOutServiceClient.get(code));
    }

    @PostMapping(value = "checkCart", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> checkCart(@RequestBody List<ProductDetailCheckOut> lstProductDetail) {
        return ResponseEntity.ok(checkOutServiceClient.checkCart(lstProductDetail));
    }
}
