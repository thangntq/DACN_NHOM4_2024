package com.ManShirtShop.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactResponse {
    private Integer id;
    private String createBy;
    private Timestamp createTime;
    private int status;
    private String updateBy;
    private Timestamp updateTime;
    private String addressName;
    private String district;
    private String name;
    private String phone;
    private String province;
    private String ward;
    private Integer idDistrict;
    private String idward;

}
