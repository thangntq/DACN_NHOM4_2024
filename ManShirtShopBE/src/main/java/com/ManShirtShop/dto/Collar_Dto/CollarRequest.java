package com.ManShirtShop.dto.Collar_Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollarRequest {
    private Integer id;
    private String name;
    private Integer status;
}
