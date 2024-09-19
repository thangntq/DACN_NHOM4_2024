package com.ManShirtShop.dto.OriginalCategory_Dto;

import java.sql.Timestamp;
import java.util.List;

import com.ManShirtShop.dto.category.CategoryResponse;

import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OriginalCategoryResponse {
    private Integer id;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String updateBy;
    private String createBy;
    private Integer status;
    private String name;
    private List<CategoryResponse> category; 
}
