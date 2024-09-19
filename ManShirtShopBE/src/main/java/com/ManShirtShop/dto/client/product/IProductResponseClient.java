package com.ManShirtShop.dto.client.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

//@Entity
public interface IProductResponseClient {

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
}
