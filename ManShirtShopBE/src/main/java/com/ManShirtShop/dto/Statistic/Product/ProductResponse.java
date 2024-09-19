package com.ManShirtShop.dto.Statistic.Product;

import java.sql.Timestamp;
import java.util.List;

import com.ManShirtShop.dto.Collar_Dto.CollarResponse;
import com.ManShirtShop.dto.category.CategoryResponse;
import com.ManShirtShop.dto.design.DesignResponse;
import com.ManShirtShop.dto.form.FormResponse;
import com.ManShirtShop.dto.material_Dto.ResponseMaterial;
import com.ManShirtShop.dto.product.ProductDiscountResponese2;
import com.ManShirtShop.dto.product_Image_dto.ProductImageResponse;
import com.ManShirtShop.dto.sleeve.SleeveResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Integer id;
    private String code;
    private String name;
    private double price;
    private String category;
    private String design;
    private String form;
    private String material;
    private String sleeve;
    private String collar;
    private Integer total;
    private List<ProductImageResponse> productImage;
}
