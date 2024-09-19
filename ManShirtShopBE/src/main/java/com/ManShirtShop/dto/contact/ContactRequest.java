package com.ManShirtShop.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactRequest {
    private Integer id;
    private String name;
    private String addressName;
    private String district;
    private String phone;
    private String province;
    private String ward;
    private int status;
}
