package com.ManShirtShop.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ManShirtShop.common.excel.DropdownItem;
import com.ManShirtShop.entities.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "product")
public class Product extends BaseEntity implements DropdownItem ,Serializable {

    @Column
    private String code;
    @Column(length = 255)
    private String name;
    private double price;
    @Column(length = 255)
    private String description;
    private double weight;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "product")
    private List<ProductDetail> productDetail;
    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImage;
    @OneToMany(mappedBy = "product")
    private List<Rating> rating;
    @ManyToOne
    @JoinColumn(name = "design_id")
    private Design design;
    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;
    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;
    @ManyToOne
    @JoinColumn(name = "sleeve_id")
    private Sleeve sleeve;
    @ManyToOne
    @JoinColumn(name = "collar_id")
    private Collar collar;
    @OneToMany(mappedBy = "productId")
    private List<ProductDiscount> productDiscount;
    // public Product(String code, String name, double price, String description, double weight,
    //         List<ProductDetail> productDetail, List<ProductImage> productImage, List<ProductDiscount> productDiscount) {
    //     this.code = code;
    //     this.name = name;
    //     this.price = price;
    //     this.description = description;
    //     this.weight = weight;
    //     this.productDetail = productDetail;
    //     this.productImage = productImage;
    //     this.productDiscount = productDiscount;
    // }
    // public Product(Integer id, Integer status, Timestamp createTime, Timestamp updateTime, String updateBy,
    //         String createBy, String code, String name, double price, String description, double weight,
    //         List<ProductDetail> productDetail, List<ProductImage> productImage, List<ProductDiscount> productDiscount) {
    //     super(id, status, createTime, updateTime, updateBy, createBy);
    //     this.code = code;
    //     this.name = name;
    //     this.price = price;
    //     this.description = description;
    //     this.weight = weight;
    //     this.productDetail = productDetail;
    //     this.productImage = productImage;
    //     this.productDiscount = productDiscount;
    // }

    // public Product(String code, String name, double price) {
    // this.code = code;
    // this.name = name;
    // this.price = price;
    // }

    // public Product(String code, String name, double price, Collection<ProductImage> productImages) {
    //     this.code = code;
    //     this.name = name;
    //     this.price = price;
    //     this.productImage = new ArrayList<>(productImages);
    // }

    @Override
    public String getName() {
        return this.name;
    }
}
