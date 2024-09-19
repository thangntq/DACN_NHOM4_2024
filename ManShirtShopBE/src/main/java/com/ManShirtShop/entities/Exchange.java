package com.ManShirtShop.entities;

import java.io.Serializable;
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

import com.ManShirtShop.entities.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exchange")
public class Exchange extends BaseEntity {

    private String code;
    @Column(name="update_name", length=50)
    private String updateName;
    private double freight;
    @Column(name="ship_name", length=255)
    private String shipName;
    @Column(name="ship_address", length=255)
    private String shipAddress;
    @Column(name="ship_phone", length=20)
    private String shipPhone;
    @Column(length=255)
    private String note;
    private double total;
    @Column(name="status_pay")
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
    @JoinColumn(name="customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;
    @OneToMany(mappedBy="exchange")
    private List<ExchangeDetail> exchangeDetail;
    @ManyToOne
    @JoinColumn(name="returnId")
    private Return returnId;


}
