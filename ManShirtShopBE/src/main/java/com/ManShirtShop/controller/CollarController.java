package com.ManShirtShop.controller;

import java.util.List;

import com.ManShirtShop.repository.CollarRepository;
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

import com.ManShirtShop.dto.Collar_Dto.CollarRequest;
import com.ManShirtShop.dto.Collar_Dto.CollarResponse;
import com.ManShirtShop.service.collar.CollarService;
import io.swagger.v3.oas.annotations.tags.Tag;
/**
 * CollarController
 */
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/collar")
@Tag(name = "Collar api")
public class CollarController {
    @Autowired
    CollarService collarService;
    @Autowired
    private CollarRepository collarRepository;

    @GetMapping(value = "findAll")
    public ResponseEntity<List<CollarResponse>> getALl() {
        return ResponseEntity.ok().body(collarService.getAll());
    }

    @GetMapping(value =  "findById")
    public ResponseEntity<CollarResponse> findById(@RequestParam Integer id){
        return ResponseEntity.ok().body(collarService.findById(id));
    }

    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<CollarResponse> create(@RequestBody CollarRequest collar) {
        return ResponseEntity.ok().body(collarService.Create(collar));
    }

    @PostMapping(value = "update", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<CollarResponse> update(@RequestBody CollarRequest collar) {
        return ResponseEntity.ok().body(collarService.update(collar));
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<CollarResponse> delete(@RequestParam Integer id) {
        return ResponseEntity.ok().body(collarService.delete(id));
    }
    @Operation(summary = "Tìm kiếm theo tên thuộc tính gần đúng")
    @GetMapping("/findBySimilarName")
    public List<CollarResponse> findBySimilarName(@RequestParam String name) {
        return collarService.findBySimilarName(name);
    }
}