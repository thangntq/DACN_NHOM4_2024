package com.ManShirtShop.dto.sleeve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SleeveRequest {
    private Integer id;
    private String name;
    private double diameter;
}
