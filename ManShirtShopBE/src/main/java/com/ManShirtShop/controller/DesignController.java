package com.ManShirtShop.controller;

import com.ManShirtShop.dto.design.DesignRequest;
import com.ManShirtShop.dto.design.DesignResponse;
import com.ManShirtShop.dto.discount.DiscountResponse;
import com.ManShirtShop.dto.form.FormRequest;
import com.ManShirtShop.dto.form.FormResponse;
import com.ManShirtShop.repository.FormRepository;
import com.ManShirtShop.service.design.DesignService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/design")
@Tag(name = "Design api")
public class DesignController {
    @Autowired
    DesignService designService;
    @Autowired
    private FormRepository formRepository;

    @GetMapping(value =  "findAll")
    public ResponseEntity<List<DesignResponse>> getALl() {
            return ResponseEntity.ok().body(designService.getAll());
        }

    @GetMapping(value =  "findById")
    public ResponseEntity<DesignResponse> findById(@RequestParam Integer id){
        return ResponseEntity.ok().body(designService.findById(id));
    }

    @PostMapping(value =  "create",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DesignResponse> create(@RequestBody DesignRequest designrequest){
        System.out.println("1-----" + designrequest);
        return ResponseEntity.ok().body(designService.Create(designrequest));
    }

    @PostMapping(value =  "update",consumes = {MediaType.APPLICATION_JSON_VALUE})
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<DesignResponse> update(@RequestBody DesignRequest designrequest){
        return ResponseEntity.ok().body(designService.Update(designrequest));
    }

    @DeleteMapping(value =  "delete")
    public ResponseEntity<DesignResponse> delete(@RequestParam Integer id){
        return ResponseEntity.ok().body(designService.delete(id));
    }
    @Operation(summary = "Tìm kiếm theo tên thuộc tính gần đúng")
    @GetMapping("/findBySimilarName")
    public List<DesignResponse> findBySimilarName(@RequestParam String name) {
        return designService.findBySimilarName(name);
    }
}
