package com.ManShirtShop.dto.Statistic.Customer;

import com.ManShirtShop.dto.customer.CustomerResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopCustomerResponse {
    private Integer id;
    private String fullName;
    private String photo;
    private String phone;
    private String email;
    private Long countProduct;
}
