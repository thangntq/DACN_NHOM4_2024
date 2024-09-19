package com.ManShirtShop.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ManShirtShop.entities.base.BaseEntity;

import com.ManShirtShop.common.excel.DropdownItem;
import lombok.Data;

@Entity
@Data
@Table(name = "voucher")
public class Voucher extends BaseEntity implements DropdownItem {
    
    @Column(length=255)
    private String name;
    @Column(length=255)
    private String code;
    @Column(length=255)
    private String description;
    @Column(name="start_date")
    private LocalDateTime startDate;
    @Column(name="end_date")
    private LocalDateTime endDate;
    @Column(name = "type")
    private boolean type; // kiểu tiền hoặc %
    @Column(name = "discount")
    private Double discount;// tiền hoặc phần trăm
    @Column(name = "min")
    private Double min;// số tiền tối thiểu để sử dụng voucher
    @Column(name = "max")
    private Double max;// số tiền giảm tối đa
    @OneToMany(mappedBy="voucher")
    private List<Order> order;

    @Override
    public String getName() {
        return this.name;
    }

}
