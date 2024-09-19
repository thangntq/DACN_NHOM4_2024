package com.ManShirtShop.entities.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/*
    @MappedSuperclass cho phép một thực thể kế thừa các thuộc tính từ một class lớn.
 */
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners({ AuditingEntityListener.class })
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "create_by")
    private String createBy;
}