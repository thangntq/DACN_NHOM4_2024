package com.ManShirtShop.dto.order_the_store;

import java.util.List;

import com.ManShirtShop.dto.client.oderDto.ProductDetailOderRequet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderRequestAdmin {
    private Integer id;
    private String code;
    private double freight;
    private String shipName;
    private String address;
    private String cityName;
    private String districtName;
    private String wardName;
    private String shipPhone;
    private String note;
    private String paymentType;
    private double total;
    private Integer statusPay;
    private boolean saleForm;
    private Integer idCity;
    private Integer idDistrict;
    private String idWard;
    private Integer voucher;
    private Integer employeed;
    private Integer customer;
    private Integer status;
    private List<ProductDetailOderRequetAdmin> lstProductDetail;
}
