package com.ManShirtShop.dto.Statistic.Customer;

public interface CustomerDto {
    Integer getId();
    String getFullName();
    Long getQuantityOrder();
    Long getQuantityReturn();
    Long getQuantityExchange();
    String getPhoto();
    String getEmail();
    String getPhone();
}
