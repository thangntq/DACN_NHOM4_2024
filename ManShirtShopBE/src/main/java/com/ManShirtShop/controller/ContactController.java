package com.ManShirtShop.controller;

import com.ManShirtShop.dto.contact.ContactRequest;
import com.ManShirtShop.dto.contact.ContactResponse;
import com.ManShirtShop.dto.design.DesignRequest;
import com.ManShirtShop.dto.design.DesignResponse;
import com.ManShirtShop.dto.voucher.VoucherRequest;
import com.ManShirtShop.dto.voucher.VoucherResponse;
import com.ManShirtShop.service.contact.ContactService;
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
@RequestMapping(value = "api/contact")
@Tag(name = "Contact api")
public class ContactController {
    @Autowired
    ContactService contactService;

    @Operation(summary = "Lấy ra danh sách Contact có status = 0")
    @GetMapping(value =  "findAll")
    public ResponseEntity<List<ContactResponse>> getAll(){
        return ResponseEntity.ok(contactService.getAll());
    }

    @Operation(summary = "Thêm Contact")
    @PostMapping(value =  "create",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ContactResponse> create(@RequestBody ContactRequest contactRequest){
        return ResponseEntity.ok().body(contactService.create(contactRequest));
    }

    @Operation(summary = "Xoá Contact")
    @DeleteMapping(value = "delete")
    public ResponseEntity<ContactResponse> delete(@RequestParam Integer id){
        return ResponseEntity.ok().body(contactService.delete(id));
    }
    @Operation(summary = "Sửa Contact")
    @PostMapping(value =  "update",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ContactResponse> update(@RequestBody ContactRequest contactRequest){
        return ResponseEntity.ok().body(contactService.update(contactRequest));
    }
}
