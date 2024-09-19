package com.ManShirtShop.Authentication.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ResponseUser {
    private String email;
    private String fullname;
    private LocalDateTime birthDate;
    private String address;
    private String phone;
    private String photo;
    private List<String> roles;
    private int status;

}
