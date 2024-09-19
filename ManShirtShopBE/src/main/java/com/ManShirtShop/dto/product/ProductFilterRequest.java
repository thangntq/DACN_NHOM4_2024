package com.ManShirtShop.dto.product;

import java.util.List;

import com.ManShirtShop.dto.Collar_Dto.CollarRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterRequest {
    private List<Integer> Color;
    private List<Integer> Category;
    private List<Integer> Design;
    private List<Integer> Form;
    private List<Integer> Material;
    private List<Integer> Size;
    private List<Integer> Collar;
    private List<Integer> Sleeve;
    private Integer Status;
    private Double Low;
    private Double High;
    private Integer Discount;
}
