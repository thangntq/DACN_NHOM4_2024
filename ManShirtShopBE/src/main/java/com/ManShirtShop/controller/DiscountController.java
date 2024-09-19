package com.ManShirtShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ManShirtShop.dto.discount.DiscountRequest;
import com.ManShirtShop.dto.discount.DiscountResponse;
import com.ManShirtShop.service.discount.DiscountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/discount")
@Tag(name = "Discount api")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    @Operation(summary = "Lấy ra danh sách discount có stt = 0")
    @GetMapping(value = "findAll")
    public ResponseEntity<List<DiscountResponse>> getALl() {
        return ResponseEntity.ok().body(discountService.getAll());
    }

    @Operation(summary = "Lấy ra discount theo id")
    @GetMapping(value = "findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return discountService.findById(id);
    }

    @Operation(summary = "Thêm discount")
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> create(@RequestBody DiscountRequest discountRequest) {
        return discountService.create(discountRequest);
    }

    @Operation(summary = "Sửa discount")
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    @PostMapping(value = "update", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> update(@RequestBody DiscountRequest discountRequest) {
        return discountService.update(discountRequest);
    }

    @Operation(summary = "Xoá discount")
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    @DeleteMapping(value = "delete")
    public ResponseEntity<?> delete(@RequestParam Integer id) {
        return discountService.delete(id);
    }
}
