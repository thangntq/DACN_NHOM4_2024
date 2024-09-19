package com.ManShirtShop.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ManShirtShop.entities.base.BaseEntity;

import com.ManShirtShop.common.excel.DropdownItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "design")
public class Design  extends BaseEntity implements DropdownItem {

    @Column(length = 255)
    private String name;
    @OneToMany(mappedBy = "design")
    private Set<Product> product;

    @Override
    public String getName() {
        return this.name;
    }
}
