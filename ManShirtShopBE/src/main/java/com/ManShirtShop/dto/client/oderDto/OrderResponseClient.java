package com.ManShirtShop.dto.client.oderDto;

import java.sql.Timestamp;
import java.util.List;

import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClient;
import com.ManShirtShop.dto.customer.CustomerResponse;
import com.ManShirtShop.dto.employee.employeeResponse;
import com.ManShirtShop.dto.voucher.VoucherResponse;
import com.ManShirtShop.entities.Customer;
import com.ManShirtShop.entities.Employee;
import com.ManShirtShop.entities.OrderDetail;
import com.ManShirtShop.entities.Rating;
import com.ManShirtShop.entities.Voucher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderResponseClient {
    private Integer id;
    private String code;
    private int status;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String updateBy;
    private String createBy;
    private double freight;
    private String shipName;
    private String address;
    private String cityName;
    private String districtName;
    private String wardName;
    private String shipPhone;
    private String note;
    private String paymentType;
    private double total;
    private int statusPay;
    private String codeGhn;
    private String codeCheckOut;
    private Integer idCity;
    private Integer idDistrict;
    private String idWard;
    private CustomerResponse customer;
    private employeeResponse employee;
    private List<OrderDetailResponseClient> orderDetail;
    // private List<Rating> rating;
    private VoucherResponse voucher;
    private String codeReturn;
}
