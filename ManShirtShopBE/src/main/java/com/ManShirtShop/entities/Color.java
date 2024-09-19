package com.ManShirtShop.entities;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ManShirtShop.common.excel.DropdownItem;
import com.ManShirtShop.entities.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "color")
public class Color extends BaseEntity implements DropdownItem {

    @Column(length=50)
    private String name;
    @OneToMany( mappedBy = "color")
    private Set<ProductDetail> productDetail;
    @OneToMany(mappedBy = "color")
    private Set<ProductImage> productImage;

    @Override
    public String getName() {
        return this.name;
    }
}
