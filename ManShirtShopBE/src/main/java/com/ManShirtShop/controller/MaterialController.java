package com.ManShirtShop.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
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
import com.ManShirtShop.dto.material_Dto.RequetMaterial;
import com.ManShirtShop.dto.material_Dto.ResponseMaterial;
import com.ManShirtShop.service.material.MaterialService;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/material")
@Tag(name = "Material api")
public class MaterialController {

    @Autowired
    MaterialService materialService;

    @GetMapping(value = "findAll")
    public ResponseEntity<List<ResponseMaterial>> getALl() {
        return ResponseEntity.ok().body(materialService.getAll());
    }

    @GetMapping(value = "findById")
    public ResponseEntity<ResponseMaterial> findById(@RequestParam Integer id) {
        return ResponseEntity.ok().body(materialService.findById(id));
    }

    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ResponseMaterial> create(@RequestBody RequetMaterial material) {
        return ResponseEntity.ok().body(materialService.Create(material));
    }

    @PostMapping(value = "update", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<ResponseMaterial> update(@RequestBody RequetMaterial material) {
        return ResponseEntity.ok().body(materialService.update(material));
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<ResponseMaterial> delete(@RequestParam Integer id) {
        return ResponseEntity.ok().body(materialService.delete(id));
    }

    @Operation(summary = "Tìm kiếm theo tên thuộc tính gần đúng")
    @GetMapping("/findBySimilarName")
    public List<ResponseMaterial> findBySimilarName(@RequestParam String name) {
        return materialService.findBySimilarName(name);
    }
}
