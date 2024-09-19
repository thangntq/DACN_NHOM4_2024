package com.ManShirtShop.dto.client.product;

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

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReponseGetAllClient {

    private Integer id;
    private String code;
    private Integer status;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String updateBy;

    private String createBy;

    private String name;
    private double price;
    private String description;
    private double weight;

    private CategoryResponse category;
    private DesignResponse design;
    private FormResponse form;
    private ResponseMaterial material;
    private SleeveResponse sleeve;
    private CollarResponse collar;
    private Integer total;
    private List<ProductImageResponse> productImage;
    private List<ProductDiscountResponese2> productDiscount;
}