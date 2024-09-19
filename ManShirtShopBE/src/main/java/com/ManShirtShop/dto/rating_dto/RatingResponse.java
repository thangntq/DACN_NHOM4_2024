package com.ManShirtShop.dto.rating_dto;

import java.sql.Timestamp;
import java.util.List;

import com.ManShirtShop.dto.customer.CustomerResponse;
import com.ManShirtShop.dto.order_the_store.OrderResponeAdmin;
import com.ManShirtShop.dto.product.ProductReponse;
import com.ManShirtShop.dto.rating_image_dto.RatingImageResponse;
import com.ManShirtShop.entities.Customer;
import com.ManShirtShop.entities.Order;
import com.ManShirtShop.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingResponse {
    private Integer id;
    private double rating;
    private String content;
    private String customerName;
    private Integer orderId;
    private Integer productId;
    private String productName;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String updateBy;
    private String createBy;
    private Integer status;
    private List<RatingImageResponse> ratingImage;
    List<ProductDetailRating> productDetail;

}
