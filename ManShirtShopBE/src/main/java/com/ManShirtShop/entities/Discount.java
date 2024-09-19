package com.ManShirtShop.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ManShirtShop.entities.base.BaseEntity;

import lombok.Data;

@Entity
@Data
@Table(name = "discount")
public class Discount extends BaseEntity{
    
    @Column(length=255)
    private String name;
    @Column(length=255)
    private String description;
    @Column(name="start_date") 
    public LocalDateTime startDate;
    @Column(name="end_date")
    public LocalDateTime endDate;

    @OneToMany(mappedBy = "discountId")
    private List<ProductDiscount> productDiscount;

}
