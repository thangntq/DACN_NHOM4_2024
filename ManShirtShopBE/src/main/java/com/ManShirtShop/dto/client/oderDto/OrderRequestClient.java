package com.ManShirtShop.dto.client.oderDto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderRequestClient {
    private Integer id;
    private String code;
    private double freight;
    private String shipName;
    // private String address;
    // private String cityName;
    // private String districtName;
    // private String wardName;
    private String shipPhone;
    private String note;
    private String paymentType;
    // private double total;
    private Integer statusPay;
    // private Integer idDistrict;
    // private String idWard;
    private Integer voucher;
    private String codeCheckOut;
    private AddressRequestOrderClient address;
    private List<ProductDetailOderRequet> lstProductDetail;
    
}
