package com.ManShirtShop.service.rating_image.impl;

import java.io.IOException;
import java.io.InvalidClassException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.management.AttributeNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.common.mapperUtil.ResponseFormat;
import com.ManShirtShop.dto.rating_image_dto.RatingImageRequest;
import com.ManShirtShop.dto.rating_image_dto.RatingImageResponse;
import com.ManShirtShop.entities.Rating;
import com.ManShirtShop.entities.RatingImage;
import com.ManShirtShop.repository.ColorRepository;
import com.ManShirtShop.repository.ProductRepository;
import com.ManShirtShop.repository.RatingImageRepository;
import com.ManShirtShop.repository.RatingRepository;
import com.ManShirtShop.service.FirebaseFileService.FirebaseFileService;
import com.ManShirtShop.service.rating.RatingService;
import com.ManShirtShop.service.rating_image.RatingImageService;

@Service
public class RatingImageServiceImpl implements RatingImageService {
    private static final Logger logger = LoggerFactory.getLogger(RatingImageServiceImpl.class);


    @Autowired
    RatingImageRepository imageRatingRepository;

    @Autowired
    FirebaseFileService firebaseFileService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ColorRepository collorRepository;
    @Autowired
    ResponseFormat responseFormat;

    @Autowired
    RatingRepository ratingService;

    @Override
    public List<RatingImageResponse> getAll() {
        List<RatingImageResponse> listAll = ObjectMapperUtils
                .mapAll(imageRatingRepository.findAll(), RatingImageResponse.class);
              
        return listAll;
    }

    @Override
    public RatingImageResponse Create(RatingImageRequest request,MultipartFile file ) throws IOException {
        Optional<Rating> image = ratingService.findById(request.getRatingId());
        RatingImage ratingImage = new RatingImage();
        ratingImage.setImage(firebaseFileService.saveTest(file));
        ratingImage.setStatus(0);
        ratingImage.setCreateTime(Timestamp.from(Instant.now()));
        if(image != null){
            ratingImage.setRating(image.get());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid rating", null);
        }
imageRatingRepository.save(ratingImage);
      
        return ObjectMapperUtils.map(ratingImage, RatingImageResponse.class);
    }

    @Override
    public RatingImageResponse update(RatingImageRequest request) {
        if (!checkIb(request.getId())) {
            return null;
        }
        Optional<Rating> image = ratingService.findById(request.getId());
        RatingImage ratingImage = new RatingImage();
        ratingImage.setId(request.getId());
        ratingImage.setStatus(1);
        ratingImage.setUpdateTime(Timestamp.from(Instant.now()));
        if(image != null){
            ratingImage.setRating(image.get());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid rating", null);
        }
        return ObjectMapperUtils.map(imageRatingRepository.save(ratingImage), RatingImageResponse.class);
    }

    @Override
    public RatingImageResponse delete(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        RatingImage e = imageRatingRepository.findById(id).get();
        e.setStatus(2);
        imageRatingRepository.save(e);
        return ObjectMapperUtils.map(e, RatingImageResponse.class);
    }

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!imageRatingRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public RatingImageResponse getById(Integer id) {
        RatingImage proImage = imageRatingRepository.findById(id).get();
                    return ObjectMapperUtils.map(proImage, RatingImageResponse.class);
		
    }

}
