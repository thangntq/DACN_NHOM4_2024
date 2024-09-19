package com.ManShirtShop.service.color.Impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import com.ManShirtShop.dto.design.DesignResponse;
import com.ManShirtShop.entities.Design;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.Color_Dto.ColorRequest;
import com.ManShirtShop.dto.Color_Dto.ColorResponse;
import com.ManShirtShop.entities.Color;
import com.ManShirtShop.repository.ColorRepository;
import com.ManShirtShop.service.color.ColorService;

@Service
public class ColorServiceImpl implements ColorService{

    @Autowired
    ColorRepository colorRepository;
    @Override
    public List<ColorResponse> getAll() {
        List<ColorResponse> listAll = ObjectMapperUtils.mapAll(colorRepository.findAll(), ColorResponse.class)
        .stream().filter(e->e.getStatus() == 0).toList();;
        return listAll;
    }

    @Override
    public ColorResponse Create(ColorRequest request) {
        request.setId(null);
        Color entity = ObjectMapperUtils.map(request, Color.class);
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity = colorRepository.save(entity);
        ColorResponse respone = ObjectMapperUtils.map(entity, ColorResponse.class);
        return respone;
    }

    @Override
    public ColorResponse update(ColorRequest request) {
        if (!checkIb(request.getId())) {
            return null;
        }
        Color entityDb = colorRepository.findById(request.getId()).get();
        Color entity = ObjectMapperUtils.map(request, Color.class);
        entity.setCreateBy(entityDb.getCreateBy());
        entity.setCreateTime(entityDb.getCreateTime());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(Timestamp.from(Instant.now()));
        return ObjectMapperUtils.map(colorRepository.save(entity), ColorResponse.class);
    }

    @Override
    public ColorResponse delete(Integer id) {
        // TODO Auto-generated method stub
        if (!checkIb(id)) {
            return null;
        }
        Color entity = colorRepository.findById(id).get();
        entity.setUpdateBy("admin");
        entity.setUpdateTime(Timestamp.from(Instant.now()));
        entity.setStatus(1);
        return ObjectMapperUtils.map(colorRepository.save(entity), ColorResponse.class);
    }

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!colorRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public ColorResponse findById(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        return ObjectMapperUtils.map(colorRepository.findById(id).get(), ColorResponse.class);
    }

    @Override
    public List<ColorResponse> findBySimilarName(String name) {
        List<Color> colors = colorRepository.findBySimilarName(name);
        return ObjectMapperUtils.mapAll(colors, ColorResponse.class);
    }

}
