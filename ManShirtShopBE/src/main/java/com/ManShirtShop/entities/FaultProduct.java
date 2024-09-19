package com.ManShirtShop.entities;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ManShirtShop.entities.base.BaseEntity;

import lombok.Data;

@Entity
@Data
@Table(name = "fault_product")
public class FaultProduct extends BaseEntity {

    private int quantity;
    @Column(length = 255)
    private String description;
    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;


}
