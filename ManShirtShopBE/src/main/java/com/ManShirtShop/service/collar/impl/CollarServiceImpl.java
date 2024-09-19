package com.ManShirtShop.service.collar.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import com.ManShirtShop.dto.Color_Dto.ColorResponse;
import com.ManShirtShop.entities.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.Collar_Dto.CollarRequest;
import com.ManShirtShop.dto.Collar_Dto.CollarResponse;
import com.ManShirtShop.entities.Collar;
import com.ManShirtShop.repository.CollarRepository;
import com.ManShirtShop.service.collar.CollarService;

@Service
public class CollarServiceImpl implements CollarService {

    @Autowired
    CollarRepository collarRepository;
    @Override
    public List<CollarResponse> getAll() {
        List<CollarResponse> listAll = ObjectMapperUtils.mapAll(collarRepository.findAll(), CollarResponse.class)
        .stream().filter(e->e.getStatus() == 0).toList();;
        return listAll;
    }

    @Override
    public CollarResponse Create(CollarRequest requet) {
        requet.setId(null);
        Collar entity = ObjectMapperUtils.map(requet, Collar.class);
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity = collarRepository.save(entity);
        CollarResponse respone = ObjectMapperUtils.map(entity, CollarResponse.class);
        return respone;
    }

    @Override
    public CollarResponse update(CollarRequest requet) {
        if (!checkIb(requet.getId())) {
            return null;
        }
        Collar entityDb = collarRepository.findById(requet.getId()).get();
        Collar entity = ObjectMapperUtils.map(requet, Collar.class);
        entity.setCreateBy(entityDb.getCreateBy());
        entity.setCreateTime(entityDb.getCreateTime());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(Timestamp.from(Instant.now()));
        return ObjectMapperUtils.map(collarRepository.save(entity), CollarResponse.class);
    }

    @Override
    public CollarResponse delete(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        Collar entity = collarRepository.findById(id).get();
        entity.setUpdateBy("admin");
        entity.setUpdateTime(Timestamp.from(Instant.now()));
        entity.setStatus(1);
        return ObjectMapperUtils.map(collarRepository.save(entity), CollarResponse.class);
    }
    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!collarRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public CollarResponse findById(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        return ObjectMapperUtils.map(collarRepository.findById(id).get(), CollarResponse.class);
    }

    @Override
    public List<CollarResponse> findBySimilarName(String name) {
        List<Collar> collars = collarRepository.findBySimilarName(name);
        return ObjectMapperUtils.mapAll(collars, CollarResponse.class);
    }

}
