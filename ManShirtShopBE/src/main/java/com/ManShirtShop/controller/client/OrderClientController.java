package com.ManShirtShop.controller.client;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ManShirtShop.dto.client.oderDto.OrderRequestClient;
import com.ManShirtShop.service.client.oder.OderClientService;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "client/api/order")
@Tag(name = "Order Client api")
public class OrderClientController {

    @Autowired
    OderClientService oderService;

    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @Transactional
    public ResponseEntity<?> create(@RequestBody OrderRequestClient requet) {
        return ResponseEntity.ok(oderService.create(requet));
    }

    @PostMapping(value = "update", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @Transactional
    public ResponseEntity<?> update(@RequestBody OrderRequestClient requet) {
        return ResponseEntity.ok(oderService.updateOrderClient(requet));
    }

    @GetMapping(value = "findByIdOrder")
    public ResponseEntity<?> findByIdOrder(@RequestParam Integer id) {
        return ResponseEntity.ok(oderService.findByIdOrder(id));
    }

    @GetMapping(value = "findByIdCustomer")
    public ResponseEntity<?> findByIdCustomer() {
        return ResponseEntity.ok(oderService.findByIdClient());
    }

    @GetMapping(value = "findByCode")
    public ResponseEntity<?> findByCode(@RequestParam String code) {
        return ResponseEntity.ok(oderService.findByCode(code));
    }

    @GetMapping(value = "findByCodeInSucces")
    public ResponseEntity<?> findByCodeInSucces(@RequestParam String code) {
        return ResponseEntity.ok(oderService.findByCodeSucces(code));
    }

}
