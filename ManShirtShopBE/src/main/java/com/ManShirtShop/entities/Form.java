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
@Table(name = "form")
public class Form extends BaseEntity implements DropdownItem {

    /** Primary key. */
    protected static final String PK = "id";

    @Column(length=255)
    private String name;
    @OneToMany(mappedBy="form")
    private Set<Product> product;

    @Override
    public String getName() {
        return this.name;
    }
}
