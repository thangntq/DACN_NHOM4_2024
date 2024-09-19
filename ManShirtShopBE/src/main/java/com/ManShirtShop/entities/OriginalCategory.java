package com.ManShirtShop.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.ManShirtShop.entities.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Original_category")
public class OriginalCategory extends BaseEntity {
    @Column(length=50)
    private String name;
    @OneToMany(mappedBy="originalCategory")
    private List<Category> category;
}
