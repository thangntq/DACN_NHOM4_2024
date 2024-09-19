package com.ManShirtShop.service.size.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.size_dto.SizeRequest;
import com.ManShirtShop.dto.size_dto.SizeResponse;
import com.ManShirtShop.dto.size_dto.Status;
import com.ManShirtShop.entities.Size;
import com.ManShirtShop.repository.SizeRepository;
import com.ManShirtShop.service.size.SizeService;

@Service
public class SizeServiceImpl implements SizeService {

    @Autowired
    SizeRepository sizeRepository;

    @Override
    public List<SizeResponse> getAll() {
        List<SizeResponse> listAll = ObjectMapperUtils.mapAll(sizeRepository.findAll(), SizeResponse.class)
        .stream().filter(e->e.getStatus().equals(Status.STOCKING)).toList();
        return listAll;
    }

    @Override
    public SizeResponse Create(SizeRequest request) {
        request.setId(null);
        Size entity = ObjectMapperUtils.map(request, Size.class);
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setStatus(Status.STOCKING);
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity = sizeRepository.save(entity);
        SizeResponse respone = ObjectMapperUtils.map(entity, SizeResponse.class);
        return respone;
    }

    @Override
    public SizeResponse update(SizeRequest request) {
        if (!checkIb(request.getId())) {
            return null;
        }
        Size eDb = sizeRepository.findById(request.getId()).get();
        Size e = ObjectMapperUtils.map(request, Size.class);
        e.setCode(request.getCode());
        e.setDescription(request.getDescription());
        e.setStatus(Status.STOCKING);
        e.setUpdateBy("admin");
        e.setUpdateTime(Timestamp.from(Instant.now()));
        e.setCreateBy(eDb.getCreateBy());
        e.setCreateTime(eDb.getCreateTime());
        return ObjectMapperUtils.map(sizeRepository.save(e), SizeResponse.class);
    }

    @Override
    public SizeResponse delete(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        Size e = sizeRepository.findById(id).get();
        e.setStatus(Status.SOLD_OUT);
        sizeRepository.save(e);
        return ObjectMapperUtils.map(e, SizeResponse.class);
    }
    

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!sizeRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public SizeResponse findById(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        return ObjectMapperUtils.map(sizeRepository.findById(id).get(), SizeResponse.class);
    }
}
