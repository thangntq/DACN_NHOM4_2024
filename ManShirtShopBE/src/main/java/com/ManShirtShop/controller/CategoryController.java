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

import com.ManShirtShop.dto.category.CategoryRequest;
import com.ManShirtShop.dto.category.CategoryResponse;
import com.ManShirtShop.service.category.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/category")
@Tag(name = "Category api")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "findAll")
    public ResponseEntity<List<CategoryResponse>> getALl() {
        return ResponseEntity.ok().body(categoryService.getAll());
    }

    @GetMapping(value = "findById")
    public ResponseEntity<CategoryResponse> findById(@RequestParam Integer id) {
        return ResponseEntity.ok().body(categoryService.findById(id));
    }

    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok().body(categoryService.Create(categoryRequest));
    }

    @PostMapping(value =  "update",consumes = {MediaType.APPLICATION_JSON_VALUE})
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<CategoryResponse> update(@RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.ok().body(categoryService.update(categoryRequest));
    }

    @DeleteMapping(value =  "delete")
    public ResponseEntity<CategoryResponse> delete(@RequestParam Integer id){
        return ResponseEntity.ok().body(categoryService.delete(id));
    }

}
