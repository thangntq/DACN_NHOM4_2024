package com.ManShirtShop.dto.client.product;

import java.sql.Timestamp;

import javax.persistence.Column;

import com.ManShirtShop.dto.Color_Dto.ColorResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponseClient {
    private int id;
    private String urlImage;
    private boolean mainImage;
    private ColorResponseClient color;
    private int productId;
    private int status;
}
