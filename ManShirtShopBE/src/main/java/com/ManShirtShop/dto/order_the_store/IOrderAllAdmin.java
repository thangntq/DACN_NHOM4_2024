package com.ManShirtShop.dto.order_the_store;

import java.sql.Timestamp;

public interface IOrderAllAdmin {

    Integer getId();

    String getCode();

    String getCode_ghn();

    boolean getSale_form();

    String getFullname();

    Timestamp getCreate_time();

    double getTotal();

    Integer getStatus_pay();

    Integer getStatus();
    String getNote();
}
