package com.ManShirtShop.dto.voucher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoucherResponse {
    private Integer id;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String updateBy;
    private String createBy;
    private Integer status;
    private String name;
    private String code;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer discount;

    private boolean type;

    private Double min;

    private Double max;
}
