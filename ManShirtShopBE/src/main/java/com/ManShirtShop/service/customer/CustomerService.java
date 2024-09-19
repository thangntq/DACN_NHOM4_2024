package com.ManShirtShop.service.customer;

import com.ManShirtShop.dto.contact.ContactRequest;
import com.ManShirtShop.dto.contact.ContactResponse;
import com.ManShirtShop.dto.customer.CustomerChangePass;
import com.ManShirtShop.dto.customer.CustomerDki;
import com.ManShirtShop.dto.customer.CustomerForgotPass;
import com.ManShirtShop.dto.customer.CustomerRequest;
import com.ManShirtShop.dto.customer.CustomerRequestCounter;
import com.ManShirtShop.dto.customer.CustomerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface CustomerService {

    List<CustomerResponse> getAllActive();
    List<CustomerResponse> getAllDisActive();
    CustomerResponse create(CustomerRequest request);

    CustomerResponse update(CustomerRequest request);

    CustomerResponse delete(Integer id);

    CustomerResponse findById(Integer id);

    CustomerResponse createAtTheCounter(CustomerRequestCounter request);

    String register(CustomerDki dki)throws JsonProcessingException;

    String changePass(CustomerChangePass dki, HttpServletRequest request);

    String sendVerificationCode(String email)throws JsonProcessingException;
    String sendVerificationCodeDki(String email)throws JsonProcessingException;

    String forgotPassword(CustomerForgotPass pas)throws JsonProcessingException;

    String verifyCode(String code)throws JsonProcessingException;
    String verifyCodeDki(String code)throws JsonProcessingException;
  
}
