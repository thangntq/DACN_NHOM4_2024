package com.ManShirtShop.service.client.adress.Impl;

import com.ManShirtShop.Authentication.dto.user.EmailUser;
import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.adress.AdressRequest;
import com.ManShirtShop.dto.adress.AdressResponse;
import com.ManShirtShop.dto.client.adress.AdressResponseClient;
import com.ManShirtShop.dto.customer.CustomerResponse;
import com.ManShirtShop.entities.Address;
import com.ManShirtShop.entities.Customer;
import com.ManShirtShop.repository.AdressRepository;
import com.ManShirtShop.repository.CustomerRepository;
import com.ManShirtShop.service.client.adress.AdressServiceClient;
import com.google.api.Http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdressServiceClientImpl implements AdressServiceClient {

    @Autowired
    AdressRepository adressRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public List<AdressResponseClient> getAllAdressClient() {
        List<Address> addresses;
        Integer id = customerRepository.findIdByEmail(EmailUser.getEmailUser());
        addresses = adressRepository.findAllByCustomerId(id);

        List<AdressResponseClient> adressResponses = new ArrayList<>();
        for (Address address : addresses) {
            AdressResponseClient adressResponse = ObjectMapperUtils.map(address, AdressResponseClient.class);
            adressResponse.setCustomerResponse(ObjectMapperUtils.map(address.getCustomer(), CustomerResponse.class));
            adressResponses.add(adressResponse);
        }
        return adressResponses;
    }

    @Override
    public AdressResponseClient create(AdressRequest adressRequest) {
        Address adress = ObjectMapperUtils.map(adressRequest, Address.class);
        Integer id = customerRepository.findIdByEmail(EmailUser.getEmailUser());
        boolean hasDefaultAddress = adressRepository.existsByCustomerIdAndIsDefaultTrue(id);
        if (adressRequest.isDefault()) {
            if (hasDefaultAddress) {
                Address defaultAddress = adressRepository.findByCustomerIdAndIsDefaultTrue(1);
                defaultAddress.setIsDefault(false);
                adressRepository.save(defaultAddress);
                adress.setIsDefault(true);
            } else {
                adress.setIsDefault(true);
            }
        } else {
            adress.setIsDefault(false);
        }
        adress.setCreateTime(Timestamp.from(Instant.now()));
        adress.setCreateBy("System");
        Customer c = new Customer();
        c.setId(id);
        adress.setCustomer(c);
        adress = adressRepository.save(adress);
        // Bước 4: Tạo và trả về đối tượng AdressResponse
        AdressResponseClient adressResponse = ObjectMapperUtils.map(adress, AdressResponseClient.class);
        return adressResponse;
    }

    @Override
    public AdressResponseClient delete(Integer id) {
        try {
            if (id <= 0 || id == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dữ liệu không hợp lệ");
            }
            Optional<Address> a = adressRepository.findById(id);
            if (a.get().getIsDefault()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không được xoá địa chỉ mặc định");
            }
            if (a == null || a.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "dữ liệu không hợp lệ");
            }
            adressRepository.delete(a.get());
            return ObjectMapperUtils.map(a.get(), AdressResponseClient.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dữ liệu không hợp lệ");
        }
    }
}
