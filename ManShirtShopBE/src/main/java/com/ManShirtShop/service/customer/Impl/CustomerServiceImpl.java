package com.ManShirtShop.service.customer.Impl;

import com.ManShirtShop.Authentication.custom.TokenUtilities;
import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.customer.CustomerChangePass;
import com.ManShirtShop.dto.customer.CustomerDki;
import com.ManShirtShop.dto.customer.CustomerForgotPass;
import com.ManShirtShop.dto.customer.CustomerRequest;
import com.ManShirtShop.dto.customer.CustomerRequestCounter;
import com.ManShirtShop.dto.customer.CustomerResponse;
import com.ManShirtShop.dto.discount.DiscountResponse;
import com.ManShirtShop.entities.Customer;
import com.ManShirtShop.entities.Discount;
import com.ManShirtShop.entities.Role;
import com.ManShirtShop.repository.CustomerRepository;
import com.ManShirtShop.service.customer.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@Service
public class CustomerServiceImpl implements CustomerService {
    // 0 hoạt động
    // 1 đã ngưng hoạt động
    // 3 chưa kích hoạt
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    TokenUtilities tokenUtilities;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    EmailService emailService;

    private String currentVerificationCode = "";
    private String currentVerificationCodeDki = "";
    private String emailx = "";
    private String emailDki = "";
    private SecureRandom random = new SecureRandom();
    private String code1 = "";
    private String codeDki = "";

    @Override
    public List<CustomerResponse> getAllActive() {
        List<Customer> geListDb = customerRepository.getAllActiveByStatus();
        return ObjectMapperUtils.mapAll(geListDb, CustomerResponse.class);
    }

    @Override
    public List<CustomerResponse> getAllDisActive() {
        List<Customer> geListDb = customerRepository.getAllDisActiveByStatus();
        return ObjectMapperUtils.mapAll(geListDb, CustomerResponse.class);
    }

    @Override
    public CustomerResponse create(CustomerRequest request) {
        request.setId(-1);
        Customer entity = ObjectMapperUtils.map(request, Customer.class);
        entity.setCreateBy("admin");
        Role role = new Role();
        role.setId(3);
        entity.setRole(role);
        entity.setBirthDate(request.getBirthDate());
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        entity = customerRepository.save(entity);
        return ObjectMapperUtils.map(entity, CustomerResponse.class);
    }

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!customerRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public CustomerResponse update(CustomerRequest request) {
        if (!checkIb(request.getId())) {
            return null;
        }
        Customer entityDB = customerRepository.findById(request.getId()).get();
        Customer entity = ObjectMapperUtils.map(request, Customer.class);
        entity.setCreateBy(entityDB.getCreateBy());
        entity.setCreateTime(entityDB.getCreateTime());
        entity.setBirthDate(entityDB.getBirthDate());
        entity.setUpdateBy("admin");
        entity.setRole(entityDB.getRole());
        entity.setUpdateTime(Timestamp.from(Instant.now()));
        entity = customerRepository.save(entity);
        return ObjectMapperUtils.map(entity, CustomerResponse.class);
    }

    @Override
    public CustomerResponse delete(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        Customer e = customerRepository.findById(id).get();
        e.setStatus(1);
        e = customerRepository.save(e);
        return ObjectMapperUtils.map(e, CustomerResponse.class);
    }

    @Override
    public CustomerResponse findById(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        Customer customer = customerRepository.findById(id).get();
        return ObjectMapperUtils.map(customer, CustomerResponse.class);
    }

    @Override
    public CustomerResponse createAtTheCounter(CustomerRequestCounter request) {
        Role role = new Role();
        role.setId(3);
        Customer entity = new Customer();
        entity.setPhone(request.getPhone());
        entity.setFullname(request.getFullname());
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity.setRole(role);
        entity.setStatus(3);
        entity = customerRepository.save(entity);
        return ObjectMapperUtils.map(entity, CustomerResponse.class);
    }

    @Override
    public String register(CustomerDki dki) throws JsonProcessingException {
        Role role = new Role();
        role.setId(3);
        ObjectMapper objectMapper = new ObjectMapper();
        if (dki.getCode().contains(codeDki)) {
            try {
                if (!dki.getPassword().contains(dki.getConfirmPassword())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mật khẩu xác nhận không trùng khớp",
                            null);
                } else {
                    Customer customer = new Customer();
                    customer.setEmail(emailDki);
                    customer.setPassword(dki.getPassword());
                    customer.setFullname(dki.getFullname());
                    customer.setStatus(0);
                    customer.setPhone(dki.getPhone());
                    customer.setRole(role);
                    customerRepository.save(customer);
                }
                return objectMapper.writeValueAsString(new SuccessResponse("Đăng kí thành công "));
            } catch (Exception e) {
                return "Error register: " + e.getMessage();
            }
        }
        return objectMapper.writeValueAsString(new ErrorResponse("Code sai"));

    }

    @Override
    public String changePass(CustomerChangePass dki, HttpServletRequest request) {
        String userName = "";
        userName = tokenUtilities.getClaimsProperty(request).get("sub").toString();
        int tmp = 0;
        Customer customer = customerRepository.findCustomerByEmail(userName);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (!encoder.matches(dki.getOldPassword(), customer.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mật khẩu cũ không đúng",
                        null);

            } else {
                if (!dki.getConfirmPassword().contains(dki.getNewPassword())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mật khẩu xác nhận không trùng khớp",
                            null);
                } else {
                    customer.setPassword(dki.getNewPassword());
                    customerRepository.save(customer);
                }
            }
            return objectMapper.writeValueAsString(new SuccessResponse("Đổi mật khẩu thành công "));
        } catch (Exception e) {
            return "Error change password: " + e.getMessage();
        }

    }

    @Override
    public String sendVerificationCode(String email) {
      
        currentVerificationCode = generateRandomCode(6);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            emailx = email;
            Customer customer = customerRepository.findCustomerByEmail(email);
            if (customer != null) {
                code1 = currentVerificationCode;
                emailService.sendVerificationCode(email, currentVerificationCode);
                return objectMapper.writeValueAsString(new SuccessResponse("Gửi code thành công"));
            }
            return objectMapper.writeValueAsString(new SuccessResponse("Không có email này vui lòng nhập đúng email"));
        } catch (Exception e) {
            return "Error sending verification code: " + e.getMessage();
        }
    }

    @Override
    public String sendVerificationCodeDki(String email) throws JsonProcessingException {
       
        currentVerificationCodeDki = generateRandomCode(6);
        Customer entityDB = customerRepository.findCustomerByEmail(email);

        ObjectMapper objectMapper = new ObjectMapper();

        if (entityDB != null) {
            return objectMapper.writeValueAsString(new ErrorResponse("Đã có email hãy đăng kí email khác"));
        } else {
            try {
                emailDki = email;
                codeDki = currentVerificationCodeDki;
                emailService.sendVerificationCode(email, currentVerificationCodeDki);
                return objectMapper.writeValueAsString(new SuccessResponse("Gửi code thành công"));
            } catch (Exception e) {
                return objectMapper
                        .writeValueAsString(new ErrorResponse("Error sending verification codeDki: " + e.getMessage()));
            }
        }
    }

    class SuccessResponse {
        private String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

    }

    @Override
    public String verifyCode(String code) throws JsonProcessingException {
       
        ObjectMapper objectMapper = new ObjectMapper();

        boolean verificationResult = code.equals(currentVerificationCode);
        if (verificationResult == true) {
            return objectMapper.writeValueAsString(new SuccessResponse("Code hợp lệ"));
        }
        return objectMapper.writeValueAsString(new ErrorResponse("Code sai"));

    }

    @Override
    public String verifyCodeDki(String code) throws JsonProcessingException {
        
        ObjectMapper objectMapper = new ObjectMapper();
        boolean verificationResult = code.equals(currentVerificationCodeDki);
        if (verificationResult == true) {
            return objectMapper.writeValueAsString(new SuccessResponse("Code hợp lệ"));
        }
        return objectMapper.writeValueAsString(new ErrorResponse("Code sai"));
    }

    private String generateRandomCode(int length) {
        StringBuilder code = new StringBuilder();
        String characters = "0123456789";

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }

    @Override
    public String forgotPassword(CustomerForgotPass pas) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        if (pas.getCode().contains(code1)) {
            try {
                Customer customer = customerRepository.findCustomerByEmail(emailx);
                if (pas.getNewPassword().contains(pas.getConfirmPassword())) {
                    customer.setPassword(pas.getNewPassword());
                    customerRepository.save(customer);
                    return objectMapper.writeValueAsString(new SuccessResponse("Thay đổi mật khẩu thành công"));
                }
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mật khẩu xác nhận không trùng khớp",
                        null);
            } catch (Exception e) {
                return "Error change Pass: " + e.getMessage();
            }

        }
        return objectMapper.writeValueAsString(new ErrorResponse("Code sai"));
    }
}
