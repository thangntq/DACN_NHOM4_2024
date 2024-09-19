package com.ManShirtShop.dto.material_Dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMaterial {
    private int id;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String updateBy;
    private String createBy;
    private int status;
    private String name;
}
