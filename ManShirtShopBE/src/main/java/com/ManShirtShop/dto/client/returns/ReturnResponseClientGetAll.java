package com.ManShirtShop.dto.client.returns;

import com.ManShirtShop.dto.client.returnDetail.ReturnDetailResponseClientGet;
import com.ManShirtShop.dto.returnDetail.ReturnDetailResponseAdminGet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnResponseClientGetAll {
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
    private String codeOrder;
    private List<ReturnDetailResponseClientGet> returnDetail; // Thêm danh sách ReturnDetailResponseCLient
}
