package com.ManShirtShop.service.design.impl;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.design.DesignRequest;
import com.ManShirtShop.dto.design.DesignResponse;
import com.ManShirtShop.dto.discount.DiscountResponse;
import com.ManShirtShop.dto.form.FormRequest;
import com.ManShirtShop.dto.form.FormResponse;
import com.ManShirtShop.entities.Design;
import com.ManShirtShop.entities.Discount;
import com.ManShirtShop.entities.Form;
import com.ManShirtShop.repository.DesignRepository;
import com.ManShirtShop.repository.FormRepository;
import com.ManShirtShop.service.design.DesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class DesignServiceImpl implements DesignService {
    @Autowired
    DesignRepository designRepository;


    @Override
    public List<DesignResponse> getAll() {
        List<DesignResponse> listAll = ObjectMapperUtils.mapAll(designRepository.findAll(), DesignResponse.class)
        .stream().filter(e->e.getStatus() == 0).toList();;
        return listAll;
    }

    @Override
    public DesignResponse Create(DesignRequest request) {
        request.setId(null);
        Design entity = ObjectMapperUtils.map(request, Design.class);
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity = designRepository.save(entity);
        DesignResponse respone = ObjectMapperUtils.map(entity, DesignResponse.class);
        return respone;
    }

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!designRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public DesignResponse Update(DesignRequest request) {
        if (!checkIb(request.getId())) {
            return null;
        }
        Design eDb = designRepository.findById(request.getId()).get();
        Design e = ObjectMapperUtils.map(request, Design.class);
        e.setUpdateBy("admin");
        e.setUpdateTime(Timestamp.from(Instant.now()));
        e.setCreateBy(eDb.getCreateBy());
        e.setCreateTime(eDb.getCreateTime());
        return ObjectMapperUtils.map(designRepository.save(e), DesignResponse.class);
    }

    @Override
    public DesignResponse delete(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        Design e = designRepository.findById(id).get();
        e.setStatus(1);
        e = designRepository.save(e);
        return ObjectMapperUtils.map(e, DesignResponse.class);
    }

    @Override
    public DesignResponse findById(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        return ObjectMapperUtils.map(designRepository.findById(id).get(), DesignResponse.class);
    }

    @Override
    public List<DesignResponse> findBySimilarName(String name) {
        List<Design> designs = designRepository.findBySimilarName(name);
        return ObjectMapperUtils.mapAll(designs, DesignResponse.class);
    }
}
