package com.ManShirtShop.dto.rating_image_dto;

import java.sql.Timestamp;

import com.ManShirtShop.dto.rating_dto.RatingResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingImageResponse {
    private Integer id;
    private Integer rating;
    private String image;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String updateBy;
    private String createBy;
}
