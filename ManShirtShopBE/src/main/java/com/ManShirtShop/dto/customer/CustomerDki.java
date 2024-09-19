package com.ManShirtShop.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDki {
    private String fullname;
    private String phone;
    private String password;
    private String confirmPassword;
    private String code;
}
