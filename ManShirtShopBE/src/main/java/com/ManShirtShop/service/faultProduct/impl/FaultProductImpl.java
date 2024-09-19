package com.ManShirtShop.service.faultProduct.impl;


import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.faultProduct.FaultProductRequest;
import com.ManShirtShop.dto.faultProduct.FaultProductResponse;
import com.ManShirtShop.dto.product.ProductDetailResponse;
import com.ManShirtShop.entities.FaultProduct;
import com.ManShirtShop.entities.ProductDetail;
import com.ManShirtShop.repository.FaultProductRepository;
import com.ManShirtShop.repository.ProductDetailRepository;
import com.ManShirtShop.service.faultProduct.FaultProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
    public class FaultProductImpl implements FaultProductService {
    private FaultProductRepository faultProductRepository;
    private ProductDetailRepository productDetailRepository;

    @Autowired
    public FaultProductImpl(FaultProductRepository faultProductRepository,
                                   ProductDetailRepository productDetailRepository) {
        this.faultProductRepository = faultProductRepository;
        this.productDetailRepository = productDetailRepository;
    }

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!faultProductRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public FaultProductResponse addFaultProduct(FaultProductRequest faultProductRequest) {
        faultProductRequest.setId(null);
        FaultProduct faultProduct = new FaultProduct();
        faultProduct.setCreateBy("admin");
        faultProduct.setCreateTime(Timestamp.from(Instant.now()));
        faultProduct.setStatus(0); // Mặc định đặt status là 1
        faultProduct.setDescription(faultProductRequest.getDescription());
        int faultProductQuantity = faultProductRequest.getQuantity();

        if (faultProductRequest.getQuantity() < 0) {
            throw new IllegalArgumentException("Invalid quantity. Quantity cannot be negative.");
        }


        ProductDetail productDetail = productDetailRepository.findById(faultProductRequest.getProductDetailId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product detail ID"));
        if (faultProductRequest.getQuantity() > productDetail.getQuantity()) {
            throw new IllegalArgumentException("Invalid quantity. Quantity exceeds the available stock.");
        }
        int currentProductDetailQuantity = productDetail.getQuantity();

        if (faultProductQuantity > currentProductDetailQuantity) {
            throw new IllegalArgumentException("Invalid quantity. Quantity exceeds the available stock.");
        }

        faultProduct.setQuantity(faultProductQuantity);
        faultProduct.setProductDetail(productDetail);
        faultProduct = faultProductRepository.save(faultProduct);

        // Cập nhật lại trường product_detail_id trong JSON trả về
        FaultProductResponse faultProductResponse = ObjectMapperUtils.map(faultProduct, FaultProductResponse.class);
        faultProductResponse.setProductDetailResponse(ObjectMapperUtils.map(productDetail, ProductDetailResponse.class));

        // Giảm giá trị quantity của sản phẩm tương ứng trong bảng product detail
        currentProductDetailQuantity -= faultProductQuantity;
        productDetail.setQuantity(currentProductDetailQuantity);
        productDetailRepository.save(productDetail);

        return faultProductResponse;
    }



    @Override
    public FaultProductResponse revertFaultProduct(FaultProductRequest faultProductRequest) {
        if (!checkIb(faultProductRequest.getId())) {
            return null;
        }

        FaultProduct faultProductDB = faultProductRepository.findById(faultProductRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid faultProduct ID"));

        ProductDetail productDetailDB = productDetailRepository.findById(faultProductDB.getProductDetail().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid productDetail ID"));

        int faultProductQuantity = faultProductRequest.getQuantity();

        if (faultProductRequest.getQuantity() < 0) {
            throw new IllegalArgumentException("Invalid quantity. Quantity cannot be negative.1");
        }

        if (faultProductRequest.getQuantity() > faultProductDB.getQuantity()) {
            throw new IllegalArgumentException("Invalid quantity. Quantity exceeds the available stock.");
        }
//        // Tăng quantity trong ProductDetail
//        int currentProductDetailQuantity = productDetailDB.getQuantity();
//
//        currentProductDetailQuantity += faultProductQuantity;


        // Trừ số lượng tương ứng của faultProduct
        faultProductDB.setQuantity(faultProductDB.getQuantity() - faultProductQuantity);

        // Cộng lại số lượng tương ứng cho ProductDetail
        int updatedProductDetailQuantity = productDetailDB.getQuantity() + faultProductQuantity;

        productDetailDB.setQuantity(updatedProductDetailQuantity);

        // Cập nhật quantity trong FaultProductDB
        faultProductDB.setStatus(faultProductDB.getQuantity() == 0 ? 1 : 0);
        faultProductDB.setUpdateBy("Admin NEWMEN");
        faultProductDB.setUpdateTime(Timestamp.from(Instant.now()));
        faultProductDB.setDescription(faultProductRequest.getDescription());

        productDetailRepository.save(productDetailDB);
        faultProductRepository.save(faultProductDB);

        FaultProductResponse faultProductResponse = ObjectMapperUtils.map(faultProductDB, FaultProductResponse.class);
        faultProductResponse.setProductDetailResponse(ObjectMapperUtils.map(productDetailDB, ProductDetailResponse.class));
        return faultProductResponse;
    }


    @Override
    public FaultProductResponse updateFaultProduct(FaultProductRequest faultProductRequest) {
        if (!checkIb(faultProductRequest.getId())) {
            return null;
        }
        Integer faultProductId = faultProductRequest.getId();

        FaultProduct faultProductDB = faultProductRepository.findById(faultProductId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid faultProduct ID"));

        ProductDetail productDetailDB = productDetailRepository.findById(faultProductDB.getProductDetail().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid productDetail ID"));

        int quantityToUpdate = faultProductRequest.getQuantity();

        if (quantityToUpdate < 0) {
            throw new IllegalArgumentException("Invalid quantity. Quantity cannot be negative. Version 0 ");
        }

        if (quantityToUpdate> productDetailDB.getQuantity()) {
            throw new IllegalArgumentException("Invalid quantity. Quantity exceeds the available stock. Version 1 ");
        }
        int currentFaultProductQuantity = faultProductDB.getQuantity();

        // Tính toán sự thay đổi số lượng
        int quantityChange = quantityToUpdate - currentFaultProductQuantity;

        // Kiểm tra số lượng mới không vượt quá số lượng hiện có trong ProductDetail
        ProductDetail productDetail = faultProductDB.getProductDetail();
        int currentProductDetailQuantity = productDetail.getQuantity();
        int updatedProductDetailQuantity = currentProductDetailQuantity - quantityChange;
        if (updatedProductDetailQuantity < 0) {
            throw new IllegalArgumentException("Invalid quantity. Quantity exceeds the available stock. Version 2");
        }

        // Cộng dồn số lượng mới vào faultProduct và trừ đi số lượng trong productDetail
        faultProductDB.setQuantity(faultProductDB.getQuantity() + quantityToUpdate);
        productDetail.setQuantity(currentProductDetailQuantity-quantityToUpdate);

        // Cập nhật các thuộc tính khác của FaultProduct
        faultProductDB.setUpdateBy("Admin NEWMEN");
        faultProductDB.setUpdateTime(Timestamp.from(Instant.now()));
        faultProductDB.setDescription(faultProductRequest.getDescription());

        // Lưu các thay đổi vào cơ sở dữ liệu
        faultProductRepository.save(faultProductDB);
        productDetailRepository.save(productDetail);

        // Tạo và trả về đối tượng response
        FaultProductResponse faultProductResponse = ObjectMapperUtils.map(faultProductDB, FaultProductResponse.class);
        faultProductResponse.setCreateBy(faultProductDB.getCreateBy());
        faultProductResponse.setCreateTime(faultProductDB.getCreateTime());
        faultProductResponse.setStatus(faultProductDB.getStatus());
        faultProductResponse.setDescription(faultProductDB.getDescription());
        faultProductResponse.setProductDetailResponse(ObjectMapperUtils.map(productDetailDB, ProductDetailResponse.class));
        return faultProductResponse;
    }

    @Override
    public List<FaultProductResponse> getAllFaultProductByProductDetailsIdAndStatus(Integer productDetailId, Integer status) {
        List<FaultProduct> faultProducts;
        if (status != null) {
            faultProducts = faultProductRepository.findAllByProductDetailIdAndStatus(productDetailId, status);
        } else {
            faultProducts = faultProductRepository.findAllByProductDetailIdAndStatus(productDetailId,null);
        }
        List<FaultProductResponse> faultProductResponses = new ArrayList<>();
        for (FaultProduct faultProduct : faultProducts) {
            FaultProductResponse faultProductResponse = ObjectMapperUtils.map(faultProduct, FaultProductResponse.class);
            faultProductResponse.setProductDetailResponse(ObjectMapperUtils.map(faultProduct.getProductDetail(), ProductDetailResponse.class));
            faultProductResponses.add(faultProductResponse);
        }
        return faultProductResponses;
    }

}

