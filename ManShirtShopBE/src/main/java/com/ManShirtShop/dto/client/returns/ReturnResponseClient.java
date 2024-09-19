package com.ManShirtShop.dto.client.returns;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnResponseClient {
    private Integer id ;
    private Integer status;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;
    private String note;
    private String reason;
    private Double total;
    private String video;
    private String code;
}
