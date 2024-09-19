package com.ManShirtShop.entities;

import javax.persistence.Column;

import com.ManShirtShop.entities.base.BaseEntity;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "back_ground")
public class BackGround extends BaseEntity{
    @Column
    private Integer type;
    @Column
    private String url;
    @Column
    private String content;
}
