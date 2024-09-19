package com.ManShirtShop.service.uploadImage;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
    public ResponseEntity<?> uploadImagesAll(MultipartFile[] files) throws IOException;

    public String UploadOne(MultipartFile files);
}
