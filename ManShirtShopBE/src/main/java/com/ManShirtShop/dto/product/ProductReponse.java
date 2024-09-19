package com.ManShirtShop.dto.product;

import java.sql.Timestamp;
import java.util.List;

import com.ManShirtShop.dto.Collar_Dto.CollarResponse;
import com.ManShirtShop.dto.category.CategoryResponse;
import com.ManShirtShop.dto.design.DesignResponse;
import com.ManShirtShop.dto.discount.DiscountResponse;
import com.ManShirtShop.dto.form.FormResponse;
import com.ManShirtShop.dto.material_Dto.ResponseMaterial;
import com.ManShirtShop.dto.product_Discount.ProductDiscountResponse;
import com.ManShirtShop.dto.product_Image_dto.ProductImageResponse;
import com.ManShirtShop.dto.sleeve.SleeveResponse;
import com.ManShirtShop.repository.ProductDiscountRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReponse {

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
    private List<ProductDetailResponse> productDetail;
    private List<ProductImageResponse> productImage;
    private List<ProductDiscountResponese2> productDiscount;
}
