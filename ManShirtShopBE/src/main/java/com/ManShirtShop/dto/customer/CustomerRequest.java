package com.ManShirtShop.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerRequest {
    private Integer id;
    private LocalDateTime birthDate;
    private String email;
    private String fullname;
    private String password;
    private String phone;
    private String photo;
}
