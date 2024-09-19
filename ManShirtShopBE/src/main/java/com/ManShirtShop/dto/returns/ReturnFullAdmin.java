package com.ManShirtShop.dto.returns;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ReturnFullAdmin {
    private Integer id;
    private Integer status;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;
    private String reason;
    private String code;
}
