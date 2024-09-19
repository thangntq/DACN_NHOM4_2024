package com.ManShirtShop.dto.adress;

import com.ManShirtShop.dto.customer.CustomerRequest;
import com.ManShirtShop.dto.customer.CustomerResponse;
import com.ManShirtShop.entities.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdressRequest {
    private Integer id;
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
}
