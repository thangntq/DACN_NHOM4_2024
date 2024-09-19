package com.ManShirtShop.controller.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ManShirtShop.dto.client.oderDto.ProductDetailOderRequet;
import com.ManShirtShop.dto.voucher.VoucherResponse;
import com.ManShirtShop.service.client.voucher.VoucherClientService;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "client/api/voucher")
@Tag(name = "Voucher Client api")
public class VoucherClientController {

    @Autowired
    VoucherClientService voucherClientService;

    @PostMapping(value = "getAllVoucher", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<VoucherResponse>> delete(@RequestBody List<ProductDetailOderRequet> lstId) {
        return ResponseEntity.ok(voucherClientService.getAllVoucherIsOrderDetail(lstId));
    }
}
