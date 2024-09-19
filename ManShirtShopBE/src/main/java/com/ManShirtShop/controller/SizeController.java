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

import com.ManShirtShop.dto.size_dto.SizeRequest;
import com.ManShirtShop.dto.size_dto.SizeResponse;
import com.ManShirtShop.service.size.SizeService;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/size")
@Tag(name = "Size api")
public class SizeController {
    @Autowired
    SizeService sizeService;

    @GetMapping(value =  "findAll")
    public ResponseEntity<List<SizeResponse>> getALl(){
        return ResponseEntity.ok().body(sizeService.getAll());
    }

    @GetMapping(value =  "findById")
    public ResponseEntity<SizeResponse> findById(@RequestParam Integer id){
        return ResponseEntity.ok().body(sizeService.findById(id));
    }

    @PostMapping(value =  "create",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SizeResponse> create(@RequestBody SizeRequest request){
        System.out.println("1-----" + request);
        return ResponseEntity.ok().body(sizeService.Create(request));
    }

    @PostMapping(value =  "update",consumes = {MediaType.APPLICATION_JSON_VALUE})
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<SizeResponse> update(@RequestBody SizeRequest material){
        return ResponseEntity.ok().body(sizeService.update(material));
    }

    @DeleteMapping(value =  "delete")
    public ResponseEntity<SizeResponse> delete(@RequestParam Integer id){
        return ResponseEntity.ok().body(sizeService.delete(id));
    }
}
