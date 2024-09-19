package com.ManShirtShop.dto.size_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SizeRequest {
    private Integer id;
    private String code;
    private String description;
    private Integer status;
}
