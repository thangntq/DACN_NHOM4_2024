package com.ManShirtShop.dto.client.product;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
public class SearchProductClient {

    @Id
    Integer id;

    String code;

    String name;

    Double price;

    String description;

    double weight;

    Integer laster;

    Integer sale;

    Double rating;

    Integer discount;

    Integer soluongdaban;

}
