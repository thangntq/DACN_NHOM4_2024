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

import com.ManShirtShop.dto.product_Discount.ProductDiscountResponse;
import com.ManShirtShop.dto.product_Discount.ProductDiscoutRequest;
import com.ManShirtShop.service.product_discount.ProductDiscountService;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/productDiscount")
@Tag(name = "Product Discount api")
public class ProductDiscountController {
    @Autowired
    ProductDiscountService productDiscountService;

    @GetMapping(value = "findAll")
    public ResponseEntity<List<ProductDiscountResponse>> getALl() {
        return ResponseEntity.ok(productDiscountService.getAll());
    }

    @GetMapping(value = "findAllByDiscountId")
    public ResponseEntity<?> getALlByDiscoundId(@RequestParam Integer discountId) {
        return productDiscountService.getAllByDiscountId(discountId);
    }


    @GetMapping(value = "findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return productDiscountService.findById(id);
    }

    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<?> create(@RequestBody ProductDiscoutRequest request) {
        return productDiscountService.create(request);
    }

    @PostMapping(value = "update", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<?> update(@RequestBody List<ProductDiscoutRequest> request) {
        return productDiscountService.update(request);
    }

    @DeleteMapping(value = "delete")
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<?> delete(@RequestParam List<Integer> id) {
        return productDiscountService.delete(id);
    }
}
