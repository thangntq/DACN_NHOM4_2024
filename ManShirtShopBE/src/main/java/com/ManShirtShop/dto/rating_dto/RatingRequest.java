package com.ManShirtShop.dto.rating_dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
    private Integer id;
    private double rating;
    private String content;
//    private Integer customerId;
    private Integer orderId;
    private Integer productId;
//    private Integer status;
    private List<String> iamges;
}
