package com.ManShirtShop.service.collar;

import java.util.List;

import com.ManShirtShop.dto.Collar_Dto.CollarRequest;
import com.ManShirtShop.dto.Collar_Dto.CollarResponse;
import com.ManShirtShop.dto.Color_Dto.ColorResponse;

public interface CollarService {
    List<CollarResponse> getAll();

    CollarResponse Create(CollarRequest request);

    CollarResponse update(CollarRequest request);

    CollarResponse delete(Integer id);

    CollarResponse findById(Integer id);

    List<CollarResponse> findBySimilarName(String name);
}
