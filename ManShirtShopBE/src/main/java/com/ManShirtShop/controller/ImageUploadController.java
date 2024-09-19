package com.ManShirtShop.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ManShirtShop.service.uploadImage.ImageUploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ImageUploadController {
    @Autowired
    ImageUploadService imageUploadService;

    @Tag(name = "Product Image api")
    @Operation(summary = "Api upload ảnh ", description = "Cho tải ảnh lên ")
    @PostMapping(value = "image/upload")
    public ResponseEntity<?> uploadImages(@RequestParam("files") MultipartFile[] files,@RequestParam("drgCode")  String drgCode) throws IOException {
        // if (files == null || files.length == 0) {
        //     throw new RuntimeException("Invalid Images!!");
        // }

        // List<String> uploadedImageIds = new ArrayList<>();

        // for (MultipartFile file : files) {
        //     UUID uuid = UUID.randomUUID();
        //     String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //     Files.createDirectories(Paths.get("images"));
        //     Path path = Paths.get("images/" + uuid.toString() + "__" + fileName);
        //     Path absolutePath = path.toAbsolutePath();
        //     try {
        //         Files.copy(file.getInputStream(), absolutePath, StandardCopyOption.REPLACE_EXISTING);
        //         uploadedImageIds.add(uuid.toString() + "__" + fileName);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // }

        // Map<String, Object> response = new HashMap<>();
        // response.put("imageIds", uploadedImageIds);
        return imageUploadService.uploadImagesAll(files);
    }

    @GetMapping("image/uploaded")
    public ResponseEntity<?> getUploadedImages() throws IOException {
        List<String> uploadedImageIds = new ArrayList<>();

        Files.list(Paths.get("images"))
                .forEach(img -> uploadedImageIds.add(img.getFileName().toString()));

        Map<String, Object> response = new HashMap<>();
        response.put("uploadedImageIds", uploadedImageIds);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping(path = "image/{imageId}")
    public ResponseEntity<?> getImage(@PathVariable String imageId) throws IOException {
        Optional<Path> images = Files.list(Paths.get("images/")).filter(img -> img.getFileName().toString().equals(imageId)).findFirst();
        if (images.isPresent()) {
            final ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(images.get()));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.IMAGE_JPEG)
                    .contentLength(inputStream.contentLength())
                    .body(inputStream);
        }
        return ResponseEntity.ok().build();
    }
}
    
