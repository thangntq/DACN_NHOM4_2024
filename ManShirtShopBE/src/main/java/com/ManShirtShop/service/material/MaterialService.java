package com.ManShirtShop.service.material;

import java.util.List;

import com.ManShirtShop.dto.material_Dto.RequetMaterial;
import com.ManShirtShop.dto.material_Dto.ResponseMaterial;

public interface MaterialService {
    List<ResponseMaterial> getAll();

    ResponseMaterial Create(RequetMaterial requet);

    ResponseMaterial update(RequetMaterial requet);

    ResponseMaterial delete(Integer id);

    ResponseMaterial findById(Integer id);

    List<ResponseMaterial> findBySimilarName(String name);
}
