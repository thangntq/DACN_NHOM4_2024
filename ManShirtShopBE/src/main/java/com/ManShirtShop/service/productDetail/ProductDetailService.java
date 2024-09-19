package com.ManShirtShop.service.productDetail;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;

import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailRequest;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;
import com.ManShirtShop.dto.Statistic.ProductDetail.ProductDetail;

public interface ProductDetailService {
    List<ProductDetailResponse> getAll();

    List<ProductDetailResponse> saveAll(List<ProductDetailRequest> request);

    ProductDetailResponse Create(ProductDetailRequest request);

    ProductDetailResponse update(ProductDetailRequest request);

    Map<String,Object> delete(Integer id);
    Map<String,Object> updateHoatDong(Integer id);

    Map<String,Object> updateXoa(Integer id);

    ProductDetailResponse findById(Integer id);

    ProductDetail findById2(Integer id);

    ProductDetailResponse findByBarcode(String barcode);

    BufferedImage getBarcode(String barCodeRequest);
}
