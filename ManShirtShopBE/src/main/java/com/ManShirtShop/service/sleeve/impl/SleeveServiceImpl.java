package com.ManShirtShop.service.sleeve.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.sleeve.SleeveRequest;
import com.ManShirtShop.dto.sleeve.SleeveResponse;
import com.ManShirtShop.entities.Sleeve;
import com.ManShirtShop.repository.SleeveRepository;
import com.ManShirtShop.service.sleeve.SleeveService;

@Service
public class SleeveServiceImpl implements SleeveService {

    @Autowired
    SleeveRepository sleeveRepository;

    @Override
    public List<SleeveResponse> getAll() {
        List<SleeveResponse> listAll = ObjectMapperUtils.mapAll(sleeveRepository.getAllByStatus(), SleeveResponse.class);
        return listAll;
    }

    @Override
    public SleeveResponse Create(SleeveRequest requet) {
        requet.setId(null);
        Sleeve entity = ObjectMapperUtils.map(requet, Sleeve.class);
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        entity = sleeveRepository.save(entity);
        SleeveResponse respone = ObjectMapperUtils.map(entity, SleeveResponse.class);
        return respone;
    }

    @Override
    public SleeveResponse update(SleeveRequest requet) {
        if (!checkIb(requet.getId())) {
            return null;
        }
        Sleeve entityDb = sleeveRepository.findById(requet.getId()).get();
        Sleeve entity = ObjectMapperUtils.map(requet, Sleeve.class);
        entity.setCreateBy(entityDb.getCreateBy());
        entity.setCreateTime(entityDb.getCreateTime());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        return ObjectMapperUtils.map(sleeveRepository.save(entity), SleeveResponse.class);
    }

    @Override
    public SleeveResponse delete(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        Sleeve e = sleeveRepository.findById(id).get();
        e.setStatus(1);
        e = sleeveRepository.save(e);
        return ObjectMapperUtils.map(e, SleeveResponse.class);
    }

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!sleeveRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public SleeveResponse findById(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        return ObjectMapperUtils.map(sleeveRepository.findById(id).get(), SleeveResponse.class);
    }

    @Override
    public List<SleeveResponse> findBySimilarName(String name) {
        List<Sleeve> sleeves = sleeveRepository.findBySimilarName(name);
        return ObjectMapperUtils.mapAll(sleeves, SleeveResponse.class);
    }


}
