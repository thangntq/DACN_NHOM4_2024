package com.ManShirtShop.dto.Color_Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorRequest {
    private Integer id;
    private String name;
    private Integer status;
}
