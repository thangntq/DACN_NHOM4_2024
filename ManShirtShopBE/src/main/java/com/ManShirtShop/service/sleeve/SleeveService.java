package com.ManShirtShop.service.sleeve;

import java.util.List;

import com.ManShirtShop.dto.sleeve.SleeveRequest;
import com.ManShirtShop.dto.sleeve.SleeveResponse;

public interface SleeveService {
    List<SleeveResponse> getAll();

    SleeveResponse Create(SleeveRequest requet);

    SleeveResponse update(SleeveRequest requet);

    SleeveResponse delete(Integer id);

    SleeveResponse findById(Integer id);

    List<SleeveResponse> findBySimilarName(String name);
}
