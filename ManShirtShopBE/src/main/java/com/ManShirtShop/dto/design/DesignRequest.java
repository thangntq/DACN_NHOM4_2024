package com.ManShirtShop.dto.design;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DesignRequest {
    private Integer id ;
    private String name;
    private int status;
}
