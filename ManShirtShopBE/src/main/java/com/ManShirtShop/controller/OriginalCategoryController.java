package com.ManShirtShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ManShirtShop.dto.OriginalCategory_Dto.OriginalCategoryRequest;
import com.ManShirtShop.dto.OriginalCategory_Dto.OriginalCategoryResponse;
import com.ManShirtShop.dto.category.CategoryRequest;
import com.ManShirtShop.dto.category.CategoryResponse;
import com.ManShirtShop.service.category.CategoryService;
import com.ManShirtShop.service.originalCategory.OriginalCategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/originalCategory")
@Tag(name = "Original Category api")
public class OriginalCategoryController {
    @Autowired
    OriginalCategoryService originalCategoryService;

    @GetMapping(value = "findAll")
    public ResponseEntity<List<OriginalCategoryResponse>> getALl() {
        return ResponseEntity.ok().body(originalCategoryService.getAll());
    }

    @GetMapping(value = "findById")
    public ResponseEntity<OriginalCategoryResponse> findById(@RequestParam Integer id) {
        return ResponseEntity.ok().body(originalCategoryService.findById(id));
    }

    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<OriginalCategoryResponse> create(@RequestBody OriginalCategoryRequest categoryRequest) {
        return ResponseEntity.ok().body(originalCategoryService.Create(categoryRequest));
    }

    @PostMapping(value =  "update",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<OriginalCategoryResponse> update(@RequestBody OriginalCategoryRequest categoryRequest){
        return ResponseEntity.ok().body(originalCategoryService.update(categoryRequest));
    }

    @DeleteMapping(value =  "delete")
    public ResponseEntity<OriginalCategoryResponse> delete(@RequestParam Integer id){
        return ResponseEntity.ok().body(originalCategoryService.delete(id));
    }
}
