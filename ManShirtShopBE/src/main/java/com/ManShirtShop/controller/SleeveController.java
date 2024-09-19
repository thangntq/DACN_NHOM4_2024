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

import com.ManShirtShop.dto.sleeve.SleeveRequest;
import com.ManShirtShop.dto.sleeve.SleeveResponse;
import com.ManShirtShop.service.sleeve.SleeveService;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/sleeve")
@Tag(name = "Sleeve api")
public class SleeveController {

    @Autowired
    SleeveService sleeveService;

    @GetMapping(value = "findAll")
    public ResponseEntity<List<SleeveResponse>> getALl() {
        return ResponseEntity.ok().body(sleeveService.getAll());
    }

    @GetMapping(value = "findById")
    public ResponseEntity<SleeveResponse> findById(@RequestParam Integer id) {
        return ResponseEntity.ok().body(sleeveService.findById(id));
    }

    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<SleeveResponse> create(@RequestBody SleeveRequest material) {
        return ResponseEntity.ok().body(sleeveService.Create(material));
    }

    @PostMapping(value = "update", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<SleeveResponse> update(@RequestBody SleeveRequest material) {
        return ResponseEntity.ok().body(sleeveService.update(material));
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<SleeveResponse> delete(@RequestParam Integer id) {
        return ResponseEntity.ok().body(sleeveService.delete(id));
    }

    @Operation(summary = "Tìm kiếm theo tên thuộc tính gần đúng")
    @GetMapping("/findBySimilarName")
    public List<SleeveResponse> findBySimilarName(@RequestParam String name) {
        return sleeveService.findBySimilarName(name);
    }
}
