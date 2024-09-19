package com.ManShirtShop.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ManShirtShop.dto.rating_image_dto.RatingImageRequest;
import com.ManShirtShop.dto.rating_image_dto.RatingImageResponse;
import com.ManShirtShop.service.rating_image.RatingImageService;

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/RatingImage")
@Tag(name = "RatingImage api")
public class RatingImageController {
    @Autowired
    RatingImageService ratingImageService;

    @GetMapping(value =  "findAll")
    public ResponseEntity<List<RatingImageResponse>> getALl(){
        return ResponseEntity.ok().body(ratingImageService.getAll());
    }

    @GetMapping(value =  "findById")
    public ResponseEntity<RatingImageResponse> findById(@RequestParam Integer id){
        return ResponseEntity.ok().body(ratingImageService.getById(id));
    }

    @PostMapping(value =  "create")
    public ResponseEntity<RatingImageResponse> create(
            @RequestPart("ratingImageRequest") RatingImageRequest request,
            @RequestPart("file") MultipartFile file)
                 throws IOException{
        System.out.println("1-----" + request);
        return ResponseEntity.ok().body(ratingImageService.Create(request,file));
    }

    @PutMapping(value =  "update",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RatingImageResponse> update(@RequestBody RatingImageRequest rating){
        return ResponseEntity.ok().body(ratingImageService.update(rating));
    }

    @DeleteMapping(value =  "delete")
    public ResponseEntity<RatingImageResponse> delete(@RequestParam Integer id){
        return ResponseEntity.ok().body(ratingImageService.delete(id));
    }
}
