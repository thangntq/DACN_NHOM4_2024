package com.ManShirtShop.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
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

import lombok.Data;

@Entity
@Data
@Table(name = "employee")
public class Employee  extends BaseEntity{

    @Column(length=100)
    private String email;
    @Column(length=50)
    private String password;
    @Column(length=255)
    private String fullname;
    @Column(name="birth_date")
    private LocalDateTime birthDate;
    @Column(length=255)
    private String address;
    @Column(length=20)
    private String phone;
    @Column(length=255)
    private String photo;
    @Column(length=255)
    private String note;
    @OneToMany(mappedBy="employee")
    private Set<Exchange> exchange;
    @OneToMany(mappedBy="employee")
    private Set<Order> order;
    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;

}
