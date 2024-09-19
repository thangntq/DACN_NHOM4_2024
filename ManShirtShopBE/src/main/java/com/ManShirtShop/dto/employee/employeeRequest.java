package com.ManShirtShop.dto.employee;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class employeeRequest {
    private int id;
    private String email;
    private String password;
    private String fullname;
    private LocalDateTime birthDate;
    private String address;
    private String phone;
    private String photo;
    private String note;
    private Integer role;
}
