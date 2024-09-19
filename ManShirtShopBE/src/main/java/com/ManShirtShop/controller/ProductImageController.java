package com.ManShirtShop.controller;

import java.io.InvalidClassException;
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

import com.ManShirtShop.dto.product_Image_dto.ProductImageRequest;
import com.ManShirtShop.dto.product_Image_dto.ProductImageResponse;
import com.ManShirtShop.service.product_Image.ProductImageService;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/product_images")
@Tag(name = "Product Image api")
public class ProductImageController {
    @Autowired
    ProductImageService productImageService;

    @GetMapping(value =  "findAll")
    public ResponseEntity<List<ProductImageResponse>> getALl(){
        return ResponseEntity.ok().body(productImageService.getAll());
    }

    @GetMapping(value =  "findById")
    public ResponseEntity<Object> findById(@RequestParam int id) {
        System.out.println("findById"+id);
        return ResponseEntity.ok().body(productImageService.getById(id));
    }

    @PostMapping(value =  "create",consumes = {MediaType.APPLICATION_JSON_VALUE})
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<ProductImageResponse> create(@RequestBody ProductImageRequest request) throws InvalidClassException{
        return ResponseEntity.ok().body(productImageService.Create(request));
    }

    @PostMapping(value =  "update",consumes = {MediaType.APPLICATION_JSON_VALUE})
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<ProductImageResponse> update(@RequestBody ProductImageRequest request){
        return ResponseEntity.ok().body(productImageService.update(request));
    }

    @DeleteMapping(value =  "delete")
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<ProductImageResponse> delete(@RequestParam Integer id) {
        return ResponseEntity.ok().body(productImageService.delete(id));
    }
    
}
