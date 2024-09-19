package com.ManShirtShop.dto.client.product;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SizeResponseClient {
    private int id;
    private String code;
    private Integer status;
}
