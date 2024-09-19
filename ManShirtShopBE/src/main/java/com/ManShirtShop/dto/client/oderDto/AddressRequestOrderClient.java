package com.ManShirtShop.dto.client.oderDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressRequestOrderClient {
    private Integer id;
    private String address;
    private String cityName;
    private String districtName;
    private String other;
    private String wardName;
    private Integer idCity;
    private Integer idDistrict;
    private String idWard;
}
