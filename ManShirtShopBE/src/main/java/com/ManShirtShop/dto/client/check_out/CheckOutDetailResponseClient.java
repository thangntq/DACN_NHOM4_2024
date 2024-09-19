package com.ManShirtShop.dto.client.check_out;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ManShirtShop.dto.client.product.ProductDetailResponseClient;
import com.ManShirtShop.entities.CheckOut;
import com.ManShirtShop.entities.ProductDetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CheckOutDetailResponseClient {

    private Integer quantity;
    // private CheckOut checkOut;
    private com.ManShirtShop.dto.client.check_out.ProductDetailResponseClient productDetail;
}
