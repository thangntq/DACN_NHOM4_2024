package com.ManShirtShop.service.originalCategory.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.OriginalCategory_Dto.OriginalCategoryRequest;
import com.ManShirtShop.dto.OriginalCategory_Dto.OriginalCategoryResponse;
import com.ManShirtShop.dto.form.FormResponse;
import com.ManShirtShop.entities.Form;
import com.ManShirtShop.entities.OriginalCategory;
import com.ManShirtShop.repository.OriginalCategoryRepository;
import com.ManShirtShop.service.originalCategory.OriginalCategoryService;

@Service
public class OriginalCategoryServiceImpl implements OriginalCategoryService{
    @Autowired
    OriginalCategoryRepository originalCategoryRepository;
    
    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!originalCategoryRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public List<OriginalCategoryResponse> getAll() {
        List<OriginalCategoryResponse> listAll = ObjectMapperUtils.mapAll(originalCategoryRepository.getByIdChaNull(), OriginalCategoryResponse.class);
        return listAll;    
    }

    @Override
    public OriginalCategoryResponse Create(OriginalCategoryRequest request) {
        request.setId(null);
        OriginalCategory entity = ObjectMapperUtils.map(request, OriginalCategory.class);
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        entity = originalCategoryRepository.save(entity);
        OriginalCategoryResponse respone = ObjectMapperUtils.map(entity, OriginalCategoryResponse.class);
        return respone;
    }

    @Override
    public OriginalCategoryResponse update(OriginalCategoryRequest request) {
        if (!checkIb(request.getId())) {
            return null;
        }
        OriginalCategory eDb = originalCategoryRepository.findById(request.getId()).get();
        OriginalCategory e = ObjectMapperUtils.map(request, OriginalCategory.class);
        e.setUpdateBy("admin");
        e.setUpdateTime(Timestamp.from(Instant.now()));
        e.setCreateBy(eDb.getCreateBy());
        e.setCreateTime(eDb.getCreateTime());
        e.setStatus(0);
        return ObjectMapperUtils.map(originalCategoryRepository.save(e), OriginalCategoryResponse.class);
    }

    @Override
    public OriginalCategoryResponse delete(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        OriginalCategory e = originalCategoryRepository.findById(id).get();
        e.setStatus(1);
        e = originalCategoryRepository.save(e);
        return ObjectMapperUtils.map(e, OriginalCategoryResponse.class);
    }

    @Override
    public OriginalCategoryResponse findById(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        return ObjectMapperUtils.map(originalCategoryRepository.findById(id).get(), OriginalCategoryResponse.class);
    }
    
}
