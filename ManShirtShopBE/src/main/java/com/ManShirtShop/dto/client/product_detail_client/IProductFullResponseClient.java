package com.ManShirtShop.dto.client.product_detail_client;

import java.time.LocalDateTime;

public interface IProductFullResponseClient {
    Integer getId();

    String getCode();

    String getName();
    Double getPrice();

    String getDescription();

    double getWeight();

    Integer getLaster();

    Integer getSale();

    Double getRating();

    Integer getDiscount();

    Integer getSoluongdaban();
    LocalDateTime getEnd_date();
    String getCategory();
    String getCollar();
    String getDesign();
    String getForm();
    String getMaterial();
    String getSleeve();
}
