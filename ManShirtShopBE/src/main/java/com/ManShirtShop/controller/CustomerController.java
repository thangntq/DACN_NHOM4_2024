package com.ManShirtShop.controller;

import com.ManShirtShop.dto.contact.ContactRequest;
import com.ManShirtShop.dto.contact.ContactResponse;
import com.ManShirtShop.dto.customer.CustomerChangePass;
import com.ManShirtShop.dto.customer.CustomerDki;
import com.ManShirtShop.dto.customer.CustomerForgotPass;
import com.ManShirtShop.dto.customer.CustomerRequest;
import com.ManShirtShop.dto.customer.CustomerRequestCounter;
import com.ManShirtShop.dto.customer.CustomerResponse;
import com.ManShirtShop.service.contact.ContactService;
import com.ManShirtShop.service.customer.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/customer")
@Tag(name = "Customer api")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Operation(summary = "Lấy ra danh sách Account Khách hàng đang hoạt động")
    @GetMapping(value =  "findAllActive")
    public ResponseEntity<List<CustomerResponse>> getAllActive(){
        return ResponseEntity.ok(customerService.getAllActive());
    }
    @Operation(summary = "Lấy ra danh sách Account Khách hàng ngừng hoạt động")
    @GetMapping(value =  "findAllDisActive")
    public ResponseEntity<List<CustomerResponse>> getAllDisActive(){
        return ResponseEntity.ok(customerService.getAllDisActive());
    }
    @Operation(summary = "Thêm khách hàng")
    @PostMapping(value =  "create",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest customerRequest){
        return ResponseEntity.ok().body(customerService.create(customerRequest));
    }

    @Operation(summary = "Vô hiệu hoá Account khách hàng")
    @DeleteMapping(value = "delete")
    public ResponseEntity<CustomerResponse> delete(@RequestParam Integer id){
        return ResponseEntity.ok().body(customerService.delete(id));
    }
    @Operation(summary = "Sửa thông tin khách hàng")
    @PostMapping(value =  "update",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CustomerResponse> update(@RequestBody CustomerRequest customerRequest){
        return ResponseEntity.ok().body(customerService.update(customerRequest));
    }

    @Operation(summary = "Thêm nhanh Khách hàng tại quầy")
    @PostMapping(value =  "createAtTheCounter",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CustomerResponse> createAtTheCounter(@RequestBody CustomerRequestCounter customerRequest) {
        return ResponseEntity.ok().body(customerService.createAtTheCounter(customerRequest));
    }
    @Operation(summary = "Đăng kí khách hàng")
    @PostMapping(value =  "register",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String Register(@RequestBody CustomerDki dki) throws JsonProcessingException{

        return customerService.register(dki);

    }
    @Operation(summary = "Đổi mk cho khách")
    @PutMapping(value="changePass" ,consumes ={MediaType.APPLICATION_JSON_VALUE})
    public String ChangePass(@RequestBody CustomerChangePass dki, HttpServletRequest request) {
        return customerService.changePass(dki, request);

    }
    @Operation(summary = "Gửi mã xác minh đến email để lấy lại mật khẩu")
    @PostMapping("/send-code")
    public String sendVerificationCode(@RequestParam String email)throws JsonProcessingException {
        return customerService.sendVerificationCode(email);
    }
    @Operation(summary = "quên mật khẩu")
    @PostMapping(value="forgot-password" ,consumes ={MediaType.APPLICATION_JSON_VALUE})
    public String forgotPass(@RequestBody CustomerForgotPass customerForgotPass) throws JsonProcessingException{
        return customerService.forgotPassword(customerForgotPass);
    }
    @Operation(summary = "xác minh xem trùng code hay không để lấy lại mật khẩu")
    @PostMapping("/confirm-code")
    public String confirmCode(@RequestParam String code)throws JsonProcessingException {
        return customerService.verifyCode(code);
    }
    @Operation(summary = "Gửi mã xác minh đến email để đăng kí")
    @PostMapping("/send-code-dki")
    public String sendVerificationCodeDki(@RequestParam String email)throws JsonProcessingException {
        return customerService.sendVerificationCodeDki(email);
    }
    // @Operation(summary = "xác minh xem trùng code đăng kí hay không")
    // @PostMapping("/confirm-code-dki")
    // public String confirmCodeDki(@RequestParam String code)throws JsonProcessingException {
    //     return customerService.verifyCodeDki(code);
    // }
}
