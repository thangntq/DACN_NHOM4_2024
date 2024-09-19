package com.ManShirtShop.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ManShirtShop.dto.client.oderDto.IOrderClient;
import com.ManShirtShop.entities.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "orders")
public class Order extends BaseEntity{

    private String code;
    private double freight;
    @Column(name = "ship_name", length = 255)
    private String shipName;
    @Column(length = 255)
    private String address;
    @Column(name = "city_name", length = 255)
    private String cityName;
    @Column(name = "district_name", length = 255)
    private String districtName;
    @Column(name = "ward_name", length = 255)
    private String wardName;
    @Column(name = "ship_phone", length = 20)
    private String shipPhone;
    @Column(length = 255)
    private String note;
    @Column(name = "payment_type", length = 255)
    private String paymentType;
    @Column
    private double total;
    @Column(name = "status_pay")
    private Integer statusPay;

    @Column(name = "sale_form")
    private boolean saleForm;
    @Column(name = "id_city")
    private Integer idCity;
    @Column(name = "id_district")
    private Integer idDistrict;
    @Column(name = "id_ward")
    private String idWard;
    @Column(name = "code_ghn")
    private String codeGhn;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetail;
    @OneToMany(mappedBy = "order")
    private List<Rating> rating;
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;
    public Order(String code, String paymentType, Integer statusPay) {
        this.code = code;
        this.paymentType = paymentType;
        this.statusPay = statusPay;
    }

    
}
