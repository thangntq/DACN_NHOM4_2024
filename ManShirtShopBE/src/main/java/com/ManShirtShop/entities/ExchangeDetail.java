package com.ManShirtShop.entities;

import com.ManShirtShop.entities.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "exchange_detail")
public class ExchangeDetail extends BaseEntity {

    private int quantity;
    private double unitprice;
    private Integer discount;

    @ManyToOne
    @JoinColumn(name = "exchange_id")
    private Exchange exchange;
    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

}
