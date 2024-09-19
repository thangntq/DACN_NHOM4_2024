package com.ManShirtShop.dto.client.product;

import java.sql.Timestamp;

import com.ManShirtShop.dto.Color_Dto.ColorResponse;
import com.ManShirtShop.dto.size_dto.SizeResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorResponseClient {
    private int id;
    private String name;
}
