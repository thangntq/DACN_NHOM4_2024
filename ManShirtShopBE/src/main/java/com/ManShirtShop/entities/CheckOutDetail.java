package com.ManShirtShop.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ManShirtShop.entities.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class CheckOutDetail extends BaseEntity {
    
    @JoinColumn(name = "quantity")
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;
    @ManyToOne
    @JoinColumn(name = "check_out_id")
    private CheckOut checkOut;
}
