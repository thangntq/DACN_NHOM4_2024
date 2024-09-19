package com.ManShirtShop.dto.client.oderDto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderAllClient {
    private String code;
    private String codeGhn;
    private int status;
    private Timestamp createTime;
    private String address;
    private String cityName;
    private String districtName;
    private String wardName;
    private double total;
    private int statusPay;

}
