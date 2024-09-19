package com.ManShirtShop.dto.client.product_detail_client;

import lombok.ToString;

public interface IProductDetailClientSearch {
    Integer getId();
    String getBar_code();
    Integer getQuantity();
    Integer getProduct_id();
    Integer getStatus();
    String getName();
    Integer getColorId();
    String getCode();
    Integer getSizeId();
}
