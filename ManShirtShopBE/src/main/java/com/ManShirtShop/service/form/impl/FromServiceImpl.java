package com.ManShirtShop.service.form.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.form.FormRequest;
import com.ManShirtShop.dto.form.FormResponse;
import com.ManShirtShop.entities.Form;
import com.ManShirtShop.repository.FormRepository;
import com.ManShirtShop.service.form.FormService;

@Service
public class FromServiceImpl implements FormService{

    @Autowired
    FormRepository formRepository;

    @Override
    public List<FormResponse> getAll() {
        List<FormResponse> listAll = ObjectMapperUtils.mapAll(formRepository.getAllByStatus(), FormResponse.class);
        return listAll;            
    }

    @Override
    public FormResponse Create(FormRequest requet) {
        requet.setId(null);
        Form entity = ObjectMapperUtils.map(requet, Form.class);
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        entity = formRepository.save(entity);
        FormResponse respone = ObjectMapperUtils.map(entity, FormResponse.class);
        return respone;
    }

    @Override
    public FormResponse update(FormRequest requet) {
        if (!checkIb(requet.getId())) {
            return null;
        }
        Form eDb = formRepository.findById(requet.getId()).get();
        Form e = ObjectMapperUtils.map(requet, Form.class);
        e.setUpdateBy("admin");
        e.setUpdateTime(Timestamp.from(Instant.now()));
        e.setCreateBy(eDb.getCreateBy());
        e.setCreateTime(eDb.getCreateTime());
        e.setStatus(0);
        return ObjectMapperUtils.map(formRepository.save(e), FormResponse.class);
    }

    @Override
    public FormResponse delete(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        Form e = formRepository.findById(id).get();
        e.setStatus(1);
        e = formRepository.save(e);
        return ObjectMapperUtils.map(e, FormResponse.class);
    }
    

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!formRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public FormResponse findById(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        return ObjectMapperUtils.map(formRepository.findById(id).get(), FormResponse.class);
    }

    @Override
    public List<FormResponse> findBySimilarName(String name) {
        List<Form> forms = formRepository.findBySimilarName(name);
        return ObjectMapperUtils.mapAll(forms, FormResponse.class);
    }
}
