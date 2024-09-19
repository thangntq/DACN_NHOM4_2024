package com.ManShirtShop.service.design;

import com.ManShirtShop.dto.design.DesignRequest;
import com.ManShirtShop.dto.design.DesignResponse;
import com.ManShirtShop.dto.form.FormRequest;
import com.ManShirtShop.dto.form.FormResponse;

import java.util.List;

public interface DesignService {
    List<DesignResponse> getAll();
    DesignResponse Create(DesignRequest request);
    DesignResponse Update(DesignRequest request);
    DesignResponse delete(Integer id);
    DesignResponse findById(Integer id);
    List<DesignResponse> findBySimilarName(String name);
}
