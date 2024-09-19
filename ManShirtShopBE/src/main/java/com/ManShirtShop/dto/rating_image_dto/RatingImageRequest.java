package com.ManShirtShop.dto.rating_image_dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingImageRequest {
    private Integer id;
    private String image;
    private Integer status;
    private Integer ratingId;
}
