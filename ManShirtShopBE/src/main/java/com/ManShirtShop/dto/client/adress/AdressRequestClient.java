package com.ManShirtShop.dto.client.adress;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdressRequestClient {
    private Integer id;
    private String address;
    private String cityName;
    private String districtName;
    private String other;
    private String wardName;
    private Integer idCity;
    private Integer idDistrict;
    private Integer idWard;
    private boolean isDefault;
    @JsonProperty("customer_id")
    private Integer customerId;
}
