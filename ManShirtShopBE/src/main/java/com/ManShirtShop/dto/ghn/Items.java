package com.ManShirtShop.dto.ghn;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Items {
    String name;
    String code;
    Integer quantity;
    // Integer price;  
    // Integer length;
    // Integer width;
    // Integer weight = 2;
    // Integer height;
    // List<String> category;
}
