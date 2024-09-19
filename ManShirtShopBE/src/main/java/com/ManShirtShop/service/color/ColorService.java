package com.ManShirtShop.service.color;

import java.util.List;

import com.ManShirtShop.dto.Color_Dto.ColorRequest;
import com.ManShirtShop.dto.Color_Dto.ColorResponse;
import com.ManShirtShop.dto.design.DesignResponse;

public interface ColorService {
    List<ColorResponse> getAll();

    ColorResponse Create(ColorRequest request);

    ColorResponse update(ColorRequest request);

    ColorResponse delete(Integer id);

    ColorResponse findById(Integer id);

    List<ColorResponse> findBySimilarName(String name);
}
