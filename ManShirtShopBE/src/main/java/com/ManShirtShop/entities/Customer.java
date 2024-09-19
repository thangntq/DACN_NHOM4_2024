// Generated with g9.

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
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "customer")
public class Customer  extends BaseEntity {

    @Column(length=100)
    private String email;
    @Column(length=255)
    private String password;
    @Column(length=255)
    private String fullname;
    @Column(name="birth_date")
    private LocalDateTime birthDate;
    @Column(length=255)
    private String phone;
    @Column(length=255)
    private String photo;
    @OneToMany(mappedBy="customer")
    private List<Address> address;
    @OneToMany(mappedBy="customer")
    private List<Exchange> exchange;
    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Order> order;
    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Rating> rating;
    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonIgnore
    private Role role;


}
