package com.ManShirtShop.dto.order_the_store;

import java.sql.Timestamp;
import java.util.List;

import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClient;
import com.ManShirtShop.dto.customer.CustomerResponse;
import com.ManShirtShop.dto.employee.employeeResponse;
import com.ManShirtShop.dto.voucher.VoucherResponse;
import com.ManShirtShop.entities.Customer;
import com.ManShirtShop.entities.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponeAdmin {
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
    private boolean saleForm;
    private String codeGhn;
    private Integer idCity;
    private Integer idDistrict;
    private String idWard;
    private CustomerResponse customer;
    private employeeResponse employee;
    private List<OderDetailResponseAdmin> orderDetail;
    // private List<Rating> rating;
    private VoucherResponse voucher;
}