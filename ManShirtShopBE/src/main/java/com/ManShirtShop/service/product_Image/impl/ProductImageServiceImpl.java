package com.ManShirtShop.service.product_Image.impl;

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

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.common.mapperUtil.ResponseFormat;
import com.ManShirtShop.controller.ImageUploadController;
import com.ManShirtShop.dto.product_Image_dto.ProductImageRequest;
import com.ManShirtShop.dto.product_Image_dto.ProductImageResponse;
import com.ManShirtShop.dto.product_Image_dto.Status;
import com.ManShirtShop.entities.Color;
import com.ManShirtShop.entities.Product;
import com.ManShirtShop.entities.ProductImage;
import com.ManShirtShop.repository.ColorRepository;
import com.ManShirtShop.repository.ProductImageRepository;
import com.ManShirtShop.repository.ProductRepository;
import com.ManShirtShop.service.product_Image.ProductImageService;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    private static final Logger logger = LoggerFactory.getLogger(ProductImageServiceImpl.class);


    @Autowired
    ProductImageRepository productImageRepository;

    @Autowired
    ImageUploadController imageUploadController;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ColorRepository collorRepository;
    @Autowired
    ResponseFormat responseFormat;

    @Override
    public List<ProductImageResponse> getAll() {
        List<ProductImageResponse> listAll = ObjectMapperUtils
                .mapAll(productImageRepository.findAll(), ProductImageResponse.class)
                .stream().filter(e -> e.getStatus() == Status.STOCKING).toList();
        return listAll;
    }

    @Override
    public ProductImageResponse Create(ProductImageRequest request) throws InvalidClassException {
        Color c = collorRepository.findById(request.getColorId()).get();
        if (c.getId() != request.getColorId()) {
            throw new InvalidClassException("Invalid color");
        }
        Product p = productRepository.findById(request.getProductId()).get();
        ProductImage entity = ObjectMapperUtils.map(request, ProductImage.class);
        entity.setColor(c);
        entity.setProduct(p);
        // entity.setUrlImage(request.getUrlImage());
        entity.setStatus(Status.STOCKING);
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity = productImageRepository.save(entity);
        // ProductImageResponse respone = ObjectMapperUtils.map(entity,
        // ProductImageResponse.class);
        return ObjectMapperUtils.map(entity, ProductImageResponse.class);
    }

    @Override
    public ProductImageResponse update(ProductImageRequest request) {
        if (!checkIb(request.getId())) {
            return null;
        }
        ProductImage eDb = productImageRepository.findById(request.getId()).get();
        ProductImage e = ObjectMapperUtils.map(request, ProductImage.class);
        e.setColor(eDb.getColor());
        e.setProduct(eDb.getProduct());
        // e.setUrlImage(request.getUrlImage());
        e.setStatus(Status.STOCKING);
        e.setUpdateBy("admin");
        e.setUpdateTime(Timestamp.from(Instant.now()));
        e.setCreateBy(eDb.getCreateBy());
        e.setCreateTime(eDb.getCreateTime());
        return ObjectMapperUtils.map(productImageRepository.save(e), ProductImageResponse.class);
    }

    @Override
    public ProductImageResponse delete(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        ProductImage e = productImageRepository.findById(id).get();
        e.setStatus(Status.SOLD_OUT);
        productImageRepository.save(e);
        return ObjectMapperUtils.map(e, ProductImageResponse.class);
    }

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!productImageRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public ProductImageResponse getById(Integer id) {
        // if (!checkIb(productImageId)) {
        //     return null;
        // }
        // return ObjectMapperUtils.map(productImageRepository.findById(id).get(), ProductImageResponse.class);
     
        ProductImage proImage = productImageRepository.findById(id).get();
				

                    return ObjectMapperUtils.map(proImage, ProductImageResponse.class);
		
    }

}
