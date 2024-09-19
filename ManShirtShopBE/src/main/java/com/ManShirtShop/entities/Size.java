package com.ManShirtShop.entities;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ManShirtShop.common.excel.DropdownItem;
import com.ManShirtShop.entities.base.BaseEntity;

import lombok.Data;

@Entity
@Data
@Table(name = "size")
public class Size extends BaseEntity implements DropdownItem {



    @Column(length=10)

    private String code;
    @Column(length=255)
    private String description;
    @OneToMany(mappedBy="size")
    private Set<ProductDetail> productDetail;

    @Override
    public String getName() {
        return this.code;
    }
}
