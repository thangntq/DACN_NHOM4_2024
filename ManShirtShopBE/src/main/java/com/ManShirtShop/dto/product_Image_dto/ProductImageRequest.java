package com.ManShirtShop.dto.product_Image_dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ManShirtShop.entities.Color;
import com.ManShirtShop.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageRequest {
    private Integer id;
    private Boolean mainImage;
    private String urlImage;
    private Integer colorId;
    private Integer productId;
    private Integer status;
}
