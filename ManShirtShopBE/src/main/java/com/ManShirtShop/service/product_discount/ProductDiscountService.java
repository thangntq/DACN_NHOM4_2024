package com.ManShirtShop.service.product_discount;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ManShirtShop.dto.product_Discount.ProductDiscountResponse;
import com.ManShirtShop.dto.product_Discount.ProductDiscoutRequest;

public interface ProductDiscountService {
    List<ProductDiscountResponse> getAll();

    ResponseEntity<?>getAllByDiscountId(Integer discountId);

    ResponseEntity<?> create(ProductDiscoutRequest request);

    ResponseEntity<?> update(List<ProductDiscoutRequest> request);

    ResponseEntity<?> delete(List<Integer> id);

    ResponseEntity<?> findById(Integer id);

    List<ProductDiscountResponse> getAllByDiscount();
}
