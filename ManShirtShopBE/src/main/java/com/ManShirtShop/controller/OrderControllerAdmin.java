package com.ManShirtShop.controller;

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

import com.ManShirtShop.dto.client.oderDto.OrderRequestClient;
import com.ManShirtShop.dto.order_the_store.OrderRequestAdmin;
import com.ManShirtShop.service.client.oder.OderClientService;
import com.ManShirtShop.service.oder.OrderServiceAdmin;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/api/order")
@Tag(name = "Order Admin api")
public class OrderControllerAdmin {
    @Autowired
    OrderServiceAdmin oderService;

    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> create(@RequestBody OrderRequestAdmin requet) {
        return ResponseEntity.ok(oderService.create(requet));
    }

    @PostMapping(value = "update", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> update(@RequestBody OrderRequestAdmin requet) {
        return ResponseEntity.ok(oderService.updateOrder(requet));
    }

    // @PostMapping(value = "updateStatus", consumes = {
    // MediaType.APPLICATION_JSON_VALUE })
    // public ResponseEntity<?> updateStatus(@RequestBody List<Integer> lst,
    // @RequestParam Integer status) {
    // return ResponseEntity.ok(oderService.updateStatus(lst, status));
    // }

    @GetMapping(value = "findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return ResponseEntity.ok(oderService.findById(id));
    }

    @GetMapping(value = "findByStaus")
    public ResponseEntity<?> findByStatus(@RequestParam Integer status) {
        return ResponseEntity.ok(oderService.findByStatus(status));
    }

    @GetMapping(value = "findByCode")
    public ResponseEntity<?> findBycode(@RequestParam String Code) {
        return ResponseEntity.ok(oderService.findByCode(Code));
    }

    @GetMapping(value = "getAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(oderService.findALl());
    }

    @PostMapping(value = "updateStatusXacNhanDon", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updatStatusXacNhanDon(@RequestBody List<Integer> lst) {
        return ResponseEntity.ok(oderService.updateStatusChuanBiHang(lst));
    }

    @PostMapping(value = "updateStatusThanhCong", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updatStatusXacThanhCong(@RequestBody List<Integer> lst) {
        return ResponseEntity.ok(oderService.updateStatusThanhCong(lst));
    }

    @PostMapping(value = "updateStatusXacGiaoHang")
    public ResponseEntity<?> updatStatusGiaoHang(@RequestParam Integer lst) {
        return ResponseEntity.ok(oderService.updateStatusGiaoHang(lst));
    }

    @PostMapping(value = "updateStatusGiaoThatBai")
    public ResponseEntity<?> updatStatusGiaoThatBai(@RequestParam Integer id) {
        return ResponseEntity.ok(oderService.updateStatusGiaoThatBai(id));
    }

    @PostMapping(value = "updateStatusHuy")
    public ResponseEntity<?> updatStatusHuy(@RequestParam Integer id) {
        return ResponseEntity.ok(oderService.updateStatusHuy(id));
    }

    @GetMapping(value = "sumAllStatusOrder")
    public ResponseEntity<?> sumAllStatusOrder() {
        return ResponseEntity.ok(oderService.sumAllOrder());
    }

}
