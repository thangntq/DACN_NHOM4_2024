package com.ManShirtShop.dto.client.oderDto;

import java.util.List;

import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDetailOderRequet {
    Integer id;
    Integer quantity;
}
