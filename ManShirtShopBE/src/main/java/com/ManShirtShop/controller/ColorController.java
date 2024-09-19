package com.ManShirtShop.controller;
import java.util.List;

import com.ManShirtShop.repository.ColorRepository;
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
import com.ManShirtShop.dto.Color_Dto.ColorRequest;
import com.ManShirtShop.dto.Color_Dto.ColorResponse;
import com.ManShirtShop.service.color.ColorService;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/color")
@Tag(name = "Color api")
public class ColorController {
    @Autowired
    ColorService colorService;
    @Autowired
    private ColorRepository colorRepository;

    @GetMapping(value = "findAll")
    public ResponseEntity<List<ColorResponse>> getALl() {
        return ResponseEntity.ok().body(colorService.getAll());
    }

    @GetMapping(value =  "findById")
    public ResponseEntity<ColorResponse> findById(@RequestParam Integer id){
        return ResponseEntity.ok().body(colorService.findById(id));
    }

    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ColorResponse> create(@RequestBody ColorRequest color) {
        return ResponseEntity.ok().body(colorService.Create(color));
    }

    @PostMapping(value = "update", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<ColorResponse> update(@RequestBody ColorRequest color) {
        return ResponseEntity.ok().body(colorService.update(color));
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<ColorResponse> delete(@RequestParam Integer id) {
        return ResponseEntity.ok().body(colorService.delete(id));
    }
    @Operation(summary = "Tìm kiếm theo tên thuộc tính gần đúng")
    @GetMapping("/findBySimilarName")
    public List<ColorResponse> findBySimilarName(@RequestParam String name) {
        return colorService.findBySimilarName(name);
    }
}
