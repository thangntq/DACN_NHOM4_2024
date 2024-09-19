package com.ManShirtShop.controller;

import com.ManShirtShop.dto.discount.DiscountRequest;
import com.ManShirtShop.dto.discount.DiscountResponse;
import com.ManShirtShop.dto.voucher.VoucherRequest;
import com.ManShirtShop.dto.voucher.VoucherResponse;
import com.ManShirtShop.service.discount.DiscountService;
import com.ManShirtShop.service.voucher.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/voucher")
@Tag(name = "Voucher api")
public class VoucherController {
    @Autowired
    VoucherService voucherService;

    @Operation(summary = "Lấy ra danh sách Voucher có status = 0")
    @GetMapping(value = "findAll")
    public ResponseEntity<List<VoucherResponse>> getALl() {
        return ResponseEntity.ok().body(voucherService.getAll());
    }

    @Operation(summary = "Lấy ra Voucher theo Id")
    @GetMapping(value = "findById")
    public ResponseEntity<VoucherResponse> findById(@RequestParam Integer id) {
        return ResponseEntity.ok().body(voucherService.findById(id));
    }

    @Operation(summary = "Thêm Voucher")
    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<VoucherResponse> create(@RequestBody VoucherRequest voucherRequest) {
        System.out.println("1-----" + voucherRequest);
        return ResponseEntity.ok().body(voucherService.create(voucherRequest));
    }

    @Operation(summary = "Xoá discount")
    @DeleteMapping(value = "delete")
    public ResponseEntity<VoucherResponse> delete(@RequestParam Integer id) {
        return ResponseEntity.ok().body(voucherService.delete(id));
    }

    @Operation(summary = "Lấy ra Voucher theo Id kèm danh sách các Order liên quan")
    @GetMapping(value = "findByIdWithOrder")
    public ResponseEntity<VoucherResponse> findByIdWithOrder(@RequestParam Integer voucherId) {
        return ResponseEntity.ok().body(voucherService.findByIdWithOrder(voucherId));
    }

    @Operation(summary = "Lấy danh sách các Voucher đang hoạt động")
    @GetMapping(value = "getActiveVouchers")
    public ResponseEntity<List<VoucherResponse>> getActiveVouchers() {
        List<VoucherResponse> activeVouchers = voucherService.getActiveVouchers();
        return ResponseEntity.ok().body(activeVouchers);
    }

    @Operation(summary = "Lấy danh sách các Voucher sắp được hoạt động")
    @GetMapping(value = "getComingVouchers")
    public ResponseEntity<List<VoucherResponse>> getUpcomingVouchers() {
        List<VoucherResponse> activeVouchers = voucherService.getUpcomingVouchers();
        return ResponseEntity.ok().body(activeVouchers);
    }

    @GetMapping(value = "findByCode")
    public ResponseEntity<VoucherResponse> getByCode(@RequestParam String code) {
        VoucherResponse activeVouchers = voucherService.findByCode(code);
        return ResponseEntity.ok().body(activeVouchers);
    }

}
