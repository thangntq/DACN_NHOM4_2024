package com.ManShirtShop.service.form;

import java.util.List;

import com.ManShirtShop.dto.form.FormRequest;
import com.ManShirtShop.dto.form.FormResponse;

public interface FormService {
    List<FormResponse> getAll();

    FormResponse Create(FormRequest requet);

    FormResponse update(FormRequest requet);

    FormResponse delete(Integer id);

    FormResponse findById(Integer id);

    List<FormResponse> findBySimilarName(String name);
}
