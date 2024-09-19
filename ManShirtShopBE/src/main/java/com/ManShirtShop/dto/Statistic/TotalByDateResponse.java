package com.ManShirtShop.dto.Statistic;

import java.util.List;

import com.ManShirtShop.dto.Statistic.Customer.TopCustomerResponse;
import com.ManShirtShop.dto.Statistic.Product.TopProductResponse;
import com.ManShirtShop.dto.Statistic.Total.OrderResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalByDateResponse {
    List<OrderResponse> order;
    Integer orderNumber;
    Double total;
}
