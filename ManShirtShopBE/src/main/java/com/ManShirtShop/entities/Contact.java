package com.ManShirtShop.entities;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.checkerframework.checker.units.qual.C;

import com.ManShirtShop.entities.base.BaseEntity;

import lombok.Data;

@Entity
@Data
@Table(name = "contact")
public class Contact extends BaseEntity{

    @Column(length=255)
    private String name;
    @Column(length=20)
    private String phone;
    @Column(length=255)
    private String ward;
    @Column(length=255)
    private String district;
    @Column(length=255)
    private String province;
    @Column(name="address_name", length=255)
    private String addressName;
    @Column
    private Integer idDistrict;
    @Column
    private String idWard;
}
