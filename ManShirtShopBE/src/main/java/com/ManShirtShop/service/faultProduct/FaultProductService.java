package com.ManShirtShop.service.faultProduct;


import com.ManShirtShop.dto.faultProduct.FaultProductRequest;
import com.ManShirtShop.dto.faultProduct.FaultProductResponse;
import com.ManShirtShop.entities.FaultProduct;
import com.ManShirtShop.entities.ProductDetail;

import java.util.List;

public interface FaultProductService {
    FaultProductResponse addFaultProduct(FaultProductRequest faultProductRequest);
    FaultProductResponse revertFaultProduct(FaultProductRequest faultProductRequest);

    FaultProductResponse updateFaultProduct(FaultProductRequest faultProductRequest);

    List<FaultProductResponse> getAllFaultProductByProductDetailsIdAndStatus(Integer productDetailId, Integer status);

//
//    List<ProductDetail> getProductDetailsByFaultProductId(Integer faultProductId);
//
//    ProductDetail findByFaultProductId(Integer faultProductId);
//
//    List<FaultProduct> getAllFaultProduct();
}
