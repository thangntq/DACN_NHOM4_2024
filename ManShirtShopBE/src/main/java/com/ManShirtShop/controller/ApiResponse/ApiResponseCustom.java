package com.ManShirtShop.controller.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseCustom<T>{
    private int status;
    private String message;
    private T data;
}