package com.ManShirtShop.dto.voucher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoucherRequest {
    private Integer id;
    private String name;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer status;

    private Integer discount;

    private boolean type;

    private Double min;

    private Double max;


    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
