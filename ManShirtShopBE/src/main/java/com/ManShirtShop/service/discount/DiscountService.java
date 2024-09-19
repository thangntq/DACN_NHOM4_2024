package com.ManShirtShop.service.discount;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ManShirtShop.dto.discount.DiscountRequest;
import com.ManShirtShop.dto.discount.DiscountResponse;

public interface DiscountService {
    List<DiscountResponse> getAll();

    ResponseEntity<?> create(DiscountRequest request);

    ResponseEntity<?> update(DiscountRequest request);

    ResponseEntity<?> delete(Integer id);

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> findByName(String name);

}
