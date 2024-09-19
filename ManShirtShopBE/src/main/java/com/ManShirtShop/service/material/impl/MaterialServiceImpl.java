package com.ManShirtShop.service.material.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.material_Dto.RequetMaterial;
import com.ManShirtShop.dto.material_Dto.ResponseMaterial;
import com.ManShirtShop.entities.Material;
import com.ManShirtShop.repository.MaterialRepository;
import com.ManShirtShop.service.material.MaterialService;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    MaterialRepository materialRepository;

    @Override
    public List<ResponseMaterial> getAll() {
        List<ResponseMaterial> listAll = ObjectMapperUtils.mapAll(materialRepository.getAllByStatus(), ResponseMaterial.class);
        return listAll;
    }

    @Override
    public ResponseMaterial Create(RequetMaterial requet) {
        requet.setId(null);
        Material entity = ObjectMapperUtils.map(requet, Material.class);
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        entity = materialRepository.save(entity);
        ResponseMaterial respone = ObjectMapperUtils.map(entity, ResponseMaterial.class);
        return respone;
    }

    @Override
    public ResponseMaterial update(RequetMaterial requet) {
        if (!checkIb(requet.getId())) {
            return null;
        }
        Material entityDb = materialRepository.findById(requet.getId()).get();
        Material entity = ObjectMapperUtils.map(requet, Material.class);
        entity.setCreateBy(entityDb.getCreateBy());
        entity.setCreateTime(entityDb.getCreateTime());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        return ObjectMapperUtils.map(materialRepository.save(entity), ResponseMaterial.class);
    }

    @Override
    public ResponseMaterial delete(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        Material e = materialRepository.findById(id).get();
        e.setStatus(1);
        e = materialRepository.save(e);
        return ObjectMapperUtils.map(e, ResponseMaterial.class);
    }

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!materialRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public ResponseMaterial findById(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        return ObjectMapperUtils.map(materialRepository.findById(id).get(), ResponseMaterial.class);
    }

    @Override
    public List<ResponseMaterial> findBySimilarName(String name) {
        List<Material> materials = materialRepository.findBySimilarName(name);
        return ObjectMapperUtils.mapAll(materials, ResponseMaterial.class);
    }

}
