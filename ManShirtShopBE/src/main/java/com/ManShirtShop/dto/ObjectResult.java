package com.ManShirtShop.dto;

import com.google.auto.value.AutoValue.Builder;

import lombok.Data;

@Data
@Builder
public class ObjectResult {
    private String message;
    private boolean status;
    private Object responseData;
}
