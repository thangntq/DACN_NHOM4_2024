package com.ManShirtShop.service.client.product_client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterClientRequest {
    private List<String> color;
    private List<String> oriCategory;
    private List<String> category;
    private List<String> design;
    private List<String> form;
    private List<String> material;
    private List<String> size;
    private List<String> collar;
    private List<String> sleeve;
    private Double low;
    private Double high;
    private Integer discount;
    private Integer idDiscount;
}
