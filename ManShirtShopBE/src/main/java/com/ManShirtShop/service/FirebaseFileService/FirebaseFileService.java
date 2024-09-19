package com.ManShirtShop.service.FirebaseFileService;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin("*")
@Service
public class FirebaseFileService {

    private Storage storage;

//    @EventListener
//    public void init(ApplicationReadyEvent event) {
//        try {
////            ClassPathResource serviceAccount = new ClassPathResource("firebase-service-account-key.json");
//            storage = StorageOptions.newBuilder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
//                    .setProjectId("newmenstore-dbb2f").build().getService();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public String saveTest(MultipartFile file) throws IOException {
        String imageName = UUID.randomUUID().toString();
        Map<String, String> map = new HashMap<>();
        map.put("firebaseStorageDownloadTokens", imageName);
        BlobId blobId = BlobId.of("newmenstore-dbb2f.appspot.com", imageName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setMetadata(map)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getInputStream());
        StringBuilder url = new StringBuilder();
        url.append("https://firebasestorage.googleapis.com/v0/b/newmenstore-dbb2f.appspot.com/o/");
        url.append(imageName);
        url.append("?alt=media");
        return url.toString();
    }

//    public String deleteFile(String url) {
//        try {
//            String fileName =
//            url.replace("https://firebasestorage.googleapis.com/v0/b/newmenstore-dbb2f.appspot.com/o/", "").replace("?alt=media", "");
//            ClassPathResource serviceAccount = new ClassPathResource("firebase-service-account-key.json");
//            String projectId = "newmenstore-dbb2f";
//            Storage storage = StorageOptions.newBuilder()
//                    .setProjectId(projectId)
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
//                    .build()
//                    .getService();
//            String bucketName = "newmenstore-dbb2f.appspot.com";
//            Bucket bucket = storage.get(bucketName);
//            bucket.get(fileName).delete();
//            System.out.println("File deleted successfully." + fileName);
//            return "Xoá thành công";
//        } catch (Exception e) {
//            return "Xoá không thành công";
//        }
//
//    }
}