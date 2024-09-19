package com.ManShirtShop.dto.employee;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.ManShirtShop.dto.role.RoleResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class employeeResponse {
    private int id;
    private String email;
    private String password;
    private String fullname;
    private LocalDateTime birthDate;
    private String address;
    private String phone;
    private String photo;
    private String note;
    private RoleResponse role;
    private int status;
    private Timestamp createTime;

    private Timestamp updateTime;

    private String updateBy;

    private String createBy;
}
