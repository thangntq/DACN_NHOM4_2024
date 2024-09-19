package com.ManShirtShop.dto.order_the_store;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAllAdminByStatus {

    Integer id;

    String code;

    String codeGhn;

    String fullname;

    Timestamp create_time;

    double total;

    Integer status_pay;

    Integer status;

    String note;

    boolean saleForm;
}
