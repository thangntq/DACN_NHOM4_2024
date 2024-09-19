package com.ManShirtShop.service.rating;

import java.util.List;
import java.util.Map;

import com.ManShirtShop.dto.rating_dto.RatingRequest;
import com.ManShirtShop.dto.rating_dto.RatingResponse;



public interface RatingService {
    List<RatingResponse> getAll();

    RatingResponse Create(RatingRequest requet);

    RatingResponse update(RatingRequest requet);

    RatingResponse delete(Integer id);

    List<RatingResponse> findById(Integer id);
    Map<String,Object> updateStatusOn(Integer id);
    Map<String,Object> updateStatusOff(Integer id);

}
