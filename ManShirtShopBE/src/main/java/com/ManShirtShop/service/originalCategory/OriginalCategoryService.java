package com.ManShirtShop.service.originalCategory;

import java.util.List;

import com.ManShirtShop.dto.OriginalCategory_Dto.OriginalCategoryRequest;
import com.ManShirtShop.dto.OriginalCategory_Dto.OriginalCategoryResponse;

public interface OriginalCategoryService {
    List<OriginalCategoryResponse> getAll();

    OriginalCategoryResponse Create(OriginalCategoryRequest request);

    OriginalCategoryResponse update(OriginalCategoryRequest request);

    OriginalCategoryResponse delete(Integer id);

    OriginalCategoryResponse findById(Integer id);
}
