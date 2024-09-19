package com.ManShirtShop.dto.faultProduct;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FaultProductRequest {
    private Integer id;
    private Integer status;
    private String description;
    private Integer quantity;
    @JsonProperty("product_detail_id")
    private Integer productDetailId;
}