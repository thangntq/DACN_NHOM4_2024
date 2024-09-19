package com.ManShirtShop.dto.client.check_out;

import java.util.List;

import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CheckOutResponseClient {
    private String code;
    private String codeOder;
    List<CheckOutDetailResponseClient> checkOutDetails;
}
