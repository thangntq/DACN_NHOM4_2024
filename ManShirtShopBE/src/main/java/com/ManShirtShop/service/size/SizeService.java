package com.ManShirtShop.service.size;

import java.util.List;

import com.ManShirtShop.dto.size_dto.SizeRequest;
import com.ManShirtShop.dto.size_dto.SizeResponse;

public interface SizeService {
    List<SizeResponse> getAll();

    SizeResponse Create(SizeRequest requet);

    SizeResponse update(SizeRequest requet);

    SizeResponse delete(Integer id);

    SizeResponse findById(Integer id);
}
