package com.ManShirtShop.service.product_Image;

import java.io.InvalidClassException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ManShirtShop.dto.product_Image_dto.ProductImageRequest;
import com.ManShirtShop.dto.product_Image_dto.ProductImageResponse;



public interface ProductImageService {
    List<ProductImageResponse> getAll();

    ProductImageResponse Create(ProductImageRequest request) throws InvalidClassException;

    ProductImageResponse update(ProductImageRequest request);

    ProductImageResponse delete(Integer id);

    ProductImageResponse  getById(Integer id);
}
