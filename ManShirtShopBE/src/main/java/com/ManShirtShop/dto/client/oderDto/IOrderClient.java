package com.ManShirtShop.dto.client.oderDto;

import java.sql.Timestamp;

public interface IOrderClient {

    Integer getStatus();

    Timestamp getCreate_time();

    String getCode();

    String getCode_ghn();

    String getAddress();

    String getCity_name();

    String getDistrict_name();

    String getWard_name();

    double getTotal();

    Integer getStatus_pay();

}