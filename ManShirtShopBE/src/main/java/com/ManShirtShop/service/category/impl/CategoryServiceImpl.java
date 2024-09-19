package com.ManShirtShop.service.category.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.OriginalCategory_Dto.OriginalCategoryResponse;
import com.ManShirtShop.dto.category.CategoryRequest;
import com.ManShirtShop.dto.category.CategoryResponse;
import com.ManShirtShop.dto.employee.employeeRequest;
import com.ManShirtShop.dto.employee.employeeResponse;
import com.ManShirtShop.entities.Category;
import com.ManShirtShop.entities.Employee;
import com.ManShirtShop.entities.OriginalCategory;
import com.ManShirtShop.entities.Role;
import com.ManShirtShop.repository.CategoryRepository;
import com.ManShirtShop.repository.OriginalCategoryRepository;
import com.ManShirtShop.service.category.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    OriginalCategoryRepository originalCategoryRepository;

    public Boolean checkId(Integer id, Boolean checkDB) {
        if (id <= 0 || id == null) {
            return true;
        }
        if (!checkDB) { // check id db
            return true;
        }
        return false;
    }
    public Category checkAndReturnOriginalCategory(CategoryRequest request) {
        if (checkId(request.getCategoryId(), originalCategoryRepository.existsById(request.getCategoryId()))) {
            return null;
        }
        Category entity = ObjectMapperUtils.map(request, Category.class);

        OriginalCategory oCate = new OriginalCategory();
        oCate.setId(request.getCategoryId());
        entity.setOriginalCategory(oCate);

        return entity;
    }
    @Override
    public List<CategoryResponse> getAll() {
        List<CategoryResponse> listAll = ObjectMapperUtils.mapAll(categoryRepository.getByIdChaNull(), CategoryResponse.class);
        return listAll; 
    }

    @Override
    public CategoryResponse Create(CategoryRequest request) {
        request.setId(0);
        Category entity = checkAndReturnOriginalCategory(request);
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        entity = categoryRepository.save(entity);
        return ObjectMapperUtils.map(entity, CategoryResponse.class);
    }

    @Override
    public CategoryResponse update(CategoryRequest request) {
        if (checkId(request.getId(), categoryRepository.existsById(request.getId()))) {
            return null;
        }
        Category entityDB = categoryRepository.findById(request.getId()).get();
        Category entity = checkAndReturnOriginalCategory(request);
        entity.setCreateBy(entityDB.getCreateBy());
        entity.setCreateTime(entityDB.getCreateTime());
        entity.setUpdateBy("amdin");
        entity.setUpdateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        entity = categoryRepository.save(entity);
        return ObjectMapperUtils.map(entity, CategoryResponse.class);
    }

    @Override
    public CategoryResponse delete(Integer id) {
        if (checkId(id, categoryRepository.existsById(id))) {
            return null;
        }
        Category entity = categoryRepository.findById(id).get();
        entity.setStatus(1);
        entity = categoryRepository.save(entity);
        return ObjectMapperUtils.map(entity, CategoryResponse.class);
    }

    @Override
    public CategoryResponse findById(Integer id) {
        if (checkId(id, categoryRepository.existsById(id))) {
            return null;
        }
        Category entity = categoryRepository.findById(id).get();
        return ObjectMapperUtils.map(entity, CategoryResponse.class);
    }

    
}
