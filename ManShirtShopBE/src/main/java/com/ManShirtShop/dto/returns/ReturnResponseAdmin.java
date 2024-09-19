package com.ManShirtShop.dto.returns;

import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnResponseAdmin {
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
