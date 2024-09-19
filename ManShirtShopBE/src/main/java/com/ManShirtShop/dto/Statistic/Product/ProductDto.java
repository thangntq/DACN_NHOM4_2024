package com.ManShirtShop.dto.Statistic.Product;

public interface ProductDto {
    Integer getId();
    String getName();
    Long getQuantityOrder();
    Long getQuantityReturn();
    Long getQuantityExchange();
    Double getTotalOrder();
    Double getTotalExchange();
    Double getTotalReturn();
    String getNameCa();
    String getNameDe();
    String getNameF();
    String getNameM();
    String getNameSl();
    String getNameC();
}