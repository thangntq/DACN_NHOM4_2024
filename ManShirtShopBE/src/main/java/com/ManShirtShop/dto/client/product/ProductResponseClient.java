package com.ManShirtShop.dto.client.product;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.ManShirtShop.dto.Collar_Dto.CollarResponse;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;
import com.ManShirtShop.dto.category.CategoryResponse;
import com.ManShirtShop.dto.design.DesignResponse;
import com.ManShirtShop.dto.form.FormResponse;
import com.ManShirtShop.dto.material_Dto.ResponseMaterial;
import com.ManShirtShop.dto.product_Discount.ProductDiscountResponse;
import com.ManShirtShop.dto.product_Image_dto.ProductImageResponse;
import com.ManShirtShop.dto.sleeve.SleeveResponse;
import com.ManShirtShop.entities.Collar;
import com.ManShirtShop.entities.Design;
import com.ManShirtShop.entities.Material;
import com.ManShirtShop.entities.ProductDetail;
import com.ManShirtShop.entities.ProductDiscount;
import com.ManShirtShop.entities.ProductImage;
import com.ManShirtShop.entities.Rating;
import com.ManShirtShop.entities.Sleeve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseClient implements Serializable {

    private Integer id;
    private String code;
    private String name;
    private double price;
    private String description;
    private double weight;

    private boolean laster;
    private boolean sale;
    private Double rating;
    private Integer discount;
    private Integer soluongdaban;

    private List<ProductDetailResponseClient> productDetail;

    private List<ProductImageResponseClient> productImage;
}
