package com.ManShirtShop.entities;

import java.util.List;

import javax.persistence.Entity;
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
@Table
public class CheckOut extends BaseEntity {
    private String code;
    private String codeOder;
    @OneToMany(mappedBy = "checkOut")
    private List<CheckOutDetail> checkOutDetai;
}
