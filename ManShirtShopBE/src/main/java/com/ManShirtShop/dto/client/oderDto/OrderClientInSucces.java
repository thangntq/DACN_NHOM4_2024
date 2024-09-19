package com.ManShirtShop.dto.client.oderDto;

import java.sql.Timestamp;
import java.util.List;

import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClient;
import com.ManShirtShop.dto.customer.CustomerResponse;
import com.ManShirtShop.dto.employee.employeeResponse;
import com.ManShirtShop.dto.voucher.VoucherResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderClientInSucces {
    private String code;
    private String paymentType;
    private int statusPay;
    private String codeCheckOut;
}
