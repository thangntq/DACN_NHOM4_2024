package com.ManShirtShop.service.uploadImage.impl;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

    @Override
    public ResponseEntity<?> uploadImagesAll(MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            throw new RuntimeException("Invalid Images!!");
        }

        List<String> uploadedImageIds = new ArrayList<>();

        for (MultipartFile file : files) {
            // UUID uuid = UUID.randomUUID();
            // String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            // Files.createDirectories(Paths.get("images"));
            // Path path = Paths.get("images/" + uuid.toString() + "__" + fileName);
            // Path absolutePath = path.toAbsolutePath();
            // try {
            // Files.copy(file.getInputStream(), absolutePath,
            // StandardCopyOption.REPLACE_EXISTING);
            // uploadedImageIds.add(uuid.toString() + "__" + fileName);
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            System.out.println("ContenType------------------------------------------:"+file.getContentType());
            uploadedImageIds.add(UploadOne(file));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("imageIds", uploadedImageIds);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public String UploadOne(MultipartFile files) {
        if (files == null) {
            throw new RuntimeException("Invalid Images!!");
        }
        String fileName = null;
        try {
            UUID uuid = UUID.randomUUID();
            fileName = StringUtils.cleanPath(files.getOriginalFilename());
            Files.createDirectories(Paths.get("images"));
            Path path = Paths.get("images/" + uuid.toString() + "__" + fileName);
            Path absolutePath = path.toAbsolutePath();
            Files.copy(files.getInputStream(), absolutePath, StandardCopyOption.REPLACE_EXISTING);
            return uuid.toString() + "__" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
