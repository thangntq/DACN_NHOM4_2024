package com.ManShirtShop.service.rating_image;

import java.io.IOException;
import java.io.InvalidClassException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.ManShirtShop.dto.product_Image_dto.ProductImageRequest;
import com.ManShirtShop.dto.product_Image_dto.ProductImageResponse;
import com.ManShirtShop.dto.rating_image_dto.RatingImageRequest;
import com.ManShirtShop.dto.rating_image_dto.RatingImageResponse;



public interface RatingImageService {
    List<RatingImageResponse> getAll();

    RatingImageResponse Create(RatingImageRequest request,MultipartFile file) throws IOException;

    RatingImageResponse update(RatingImageRequest request);

    RatingImageResponse delete(Integer id);

    RatingImageResponse  getById(Integer id);
}
