package com.ManShirtShop.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ManShirtShop.entities.base.BaseEntity;

import lombok.Data;

@Entity
@Data
@Table(name = "returns")
public class Return extends BaseEntity {

    @Column(length=255)
    private String video;
    @Column(length=255)
    private String reason;
    @Column(length=255)
    private String note;
    private double total;
    private String code;
    @OneToMany(mappedBy="returnId")
    private Set<Exchange> exchange;
    @OneToMany(mappedBy="returns")
    private List<ReturnDetail> returnDetail;

    public Return(Integer id, Integer status, Timestamp createTime, Timestamp updateTime, String updateBy, String createBy, String reason, String note, String code) {
        super(id, status, createTime, updateTime, updateBy, createBy);
        this.reason = reason;
        this.note = note;
        this.code = code;
    }

    public Return(Integer id, Integer status, Timestamp createTime,
                  Timestamp updateTime, String updateBy, String createBy) {
        super(id, status, createTime, updateTime, updateBy, createBy);
    }
}
