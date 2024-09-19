package com.ManShirtShop.entities;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "product_detail")
public class ProductDetail extends BaseEntity {

    @Column(name = "bar_code", length = 255)
    private String barCode;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;
    @OneToMany(mappedBy = "productDetail")
    private List<ExchangeDetail> exchangeDetail;
    @OneToMany(mappedBy = "productDetail") 
    private List<FaultProduct> faultProduct;
    @OneToMany(mappedBy = "productDetail")
    private List<OrderDetail> orderDetail;
     @OneToMany(mappedBy = "productDetail",fetch = FetchType.LAZY)
    private List<CheckOutDetail> checkOutDetails;
    
}
