package com.ManShirtShop.dto.returns;

import java.sql.Timestamp;

public interface IReturnFullAdmin {
    Integer getId();
    Integer getStatus();
    String getCreate_by();
    Timestamp getCreate_time();
    String getUpdate_by();
    Timestamp getUpdate_time();
    String getReason();
    String getCode();
}
