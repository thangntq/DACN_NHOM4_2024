package com.ManShirtShop.dto.category;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

import com.ManShirtShop.entities.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryRequest {
    private Integer id;
    private String name;
    private Integer categoryId;
}
