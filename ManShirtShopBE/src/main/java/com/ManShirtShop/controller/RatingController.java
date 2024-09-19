package com.ManShirtShop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ManShirtShop.dto.rating_dto.RatingRequest;
import com.ManShirtShop.dto.rating_dto.RatingResponse;
import com.ManShirtShop.service.rating.RatingService;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/Rating")
@Tag(name = "Rating api")
public class RatingController {
    @Autowired
    RatingService ratingService;

    @GetMapping(value =  "findAll")
    public ResponseEntity<List<RatingResponse>> getALl(){
        return ResponseEntity.ok().body(ratingService.getAll());
    }

    @GetMapping(value =  "findByIdProduct")
    public ResponseEntity<List<RatingResponse>> findById(@RequestParam Integer id){
        return ResponseEntity.ok().body(ratingService.findById(id));
    }

    @PostMapping(value =  "create",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RatingResponse> create(@RequestBody RatingRequest request){
        System.out.println("1-----" + request);
        return ResponseEntity.ok().body(ratingService.Create(request));
    }

    @PostMapping(value =  "update",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RatingResponse> update(@RequestBody RatingRequest rating){
        return ResponseEntity.ok().body(ratingService.update(rating));
    }

    @DeleteMapping(value =  "delete")
    public ResponseEntity<RatingResponse> delete(@RequestParam Integer id){
        return ResponseEntity.ok().body(ratingService.delete(id));
    }

    @GetMapping(value =  "updateStausOne")
    public ResponseEntity<Map> updateStausOne(@RequestParam Integer id){
        return ResponseEntity.ok().body(ratingService.updateStatusOn(id));
    }

    @GetMapping(value =  "updateStausOff")
    public ResponseEntity<Map> updateStausOff(@RequestParam Integer id){
        return ResponseEntity.ok().body(ratingService.updateStatusOff(id));
    }
}
