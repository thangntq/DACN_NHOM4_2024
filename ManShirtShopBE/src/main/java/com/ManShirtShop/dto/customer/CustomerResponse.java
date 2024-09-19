package com.ManShirtShop.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ManShirtShop.dto.role.RoleResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerResponse {
    private Integer id;
    private String createBy;
    private Timestamp createTime;
    private int status;
    private String updateBy;
    private Timestamp updateTime;
    private LocalDateTime  birthDate;
    private String email;
    private String fullname;
    private String password;
    private String phone;
    private String photo;
    private RoleResponse role;

}
