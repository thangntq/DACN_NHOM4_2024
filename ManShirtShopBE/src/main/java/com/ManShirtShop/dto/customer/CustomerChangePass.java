package com.ManShirtShop.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerChangePass {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
