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

import com.ManShirtShop.dto.form.FormRequest;
import com.ManShirtShop.dto.form.FormResponse;
import com.ManShirtShop.service.form.FormService;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/form")
@Tag(name = "Form api")
public class FormController {
    @Autowired
    FormService formService;

    @GetMapping(value =  "findAll")
    public ResponseEntity<List<FormResponse>> getALl(){
        return ResponseEntity.ok().body(formService.getAll());
    }

    @GetMapping(value =  "findById")
    public ResponseEntity<FormResponse> findById(@RequestParam Integer id){
        return ResponseEntity.ok().body(formService.findById(id));
    }

    @PostMapping(value =  "create",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<FormResponse> create(@RequestBody FormRequest formrequest){
        System.out.println("1-----" + formrequest);
        return ResponseEntity.ok().body(formService.Create(formrequest));
    }

    @PostMapping(value =  "update",consumes = {MediaType.APPLICATION_JSON_VALUE})
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<FormResponse> update(@RequestBody FormRequest material){
        return ResponseEntity.ok().body(formService.update(material));
    }

    @DeleteMapping(value =  "delete")
    public ResponseEntity<FormResponse> delete(@RequestParam Integer id){
        return ResponseEntity.ok().body(formService.delete(id));
    }

    @Operation(summary = "Tìm kiếm theo tên thuộc tính gần đúng")
    @GetMapping("/findBySimilarName")
    public List<FormResponse> findBySimilarName(@RequestParam String name) {
        return formService.findBySimilarName(name);
    }
}
