package com.ManShirtShop.dto.client.adress;

import com.ManShirtShop.dto.customer.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdressResponseClient {
    private Integer id;
    private String createBy;
    private Timestamp createTime;
    private int status;
    private String updateBy;
    private Timestamp updateTime;
    private String fullName;
    private String phone;
    private String address;
    private String cityName;
    private String districtName;
    private String other;
    private String wardName;
    private Integer idCity;
    private Integer idDistrict;
    private String idWard;
    private boolean isDefault;
    private CustomerResponse customerResponse;
}
