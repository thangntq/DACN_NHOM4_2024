package com.ManShirtShop.service.category;

import java.util.List;

import com.ManShirtShop.dto.category.CategoryRequest;
import com.ManShirtShop.dto.category.CategoryResponse;

public interface CategoryService {
    List<CategoryResponse> getAll();

    CategoryResponse Create(CategoryRequest request);

    CategoryResponse update(CategoryRequest request);

    CategoryResponse delete(Integer id);

    CategoryResponse findById(Integer id);
}
