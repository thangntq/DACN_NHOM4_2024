package com.ManShirtShop.dto.category;

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
public class CategoryResponsechille {
    private Integer id;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String updateBy;
    private String createBy;
    private Integer status;
    private String name;
    private CategoryRequest categoryId;
    private List<CategoryResponsechille> lstCategory;
}
