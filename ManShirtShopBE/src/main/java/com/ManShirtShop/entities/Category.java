package com.ManShirtShop.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "category")
public class Category extends BaseEntity implements DropdownItem {

    @Column(length=50)
    private String name;
    
    @ManyToOne
    @JoinColumn(name="original_id")
    private OriginalCategory originalCategory;

    @OneToMany(mappedBy="category")
    private List<Product> product;

    @Override
    public String getName() {
        return this.name;
    }
}
