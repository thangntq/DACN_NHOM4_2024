package com.ManShirtShop.Authentication.sercurity.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ManShirtShop.Authentication.dto.response.ResponseUser;

public interface AuthService {
    List<ResponseUser> getInfomation(HttpServletRequest request);
}
