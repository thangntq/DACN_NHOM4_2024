package com.ManShirtShop.dto.Statistic.Product;

import java.util.List;

import com.ManShirtShop.dto.client.product.ProductImageResponseClient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopProductResponse {
    Integer id;
    String fullName;
    String nameCa;
    String nameDe;
    String nameF;
    String nameM;
    String nameSl;
    String nameC;
    Long quantity;
    Double total;
    List<ProductImageResponseClient> productImage;
}
