package com.ManShirtShop.dto.Statistic.ProductDetail;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ManShirtShop.dto.Color_Dto.ColorResponse;
import com.ManShirtShop.dto.product.ProductReponse;
import com.ManShirtShop.dto.size_dto.SizeResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {
    int id;
    String colorName;
    String sizeName;
    int idProduct;
    String nameProduct;
    Double price;
}
