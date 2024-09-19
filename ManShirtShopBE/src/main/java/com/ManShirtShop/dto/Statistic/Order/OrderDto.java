package com.ManShirtShop.dto.Statistic.Order;

public interface OrderDto {
    Integer getMonth();
    Integer getId();
    Double getTotalOrder();
    Double getTotalReturn();
    Double getExchange();
    Double getTotalExchange();
}
