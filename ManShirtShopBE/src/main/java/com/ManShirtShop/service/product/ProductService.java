package com.ManShirtShop.service.product;

import java.util.List;
import java.util.Map;

import com.ManShirtShop.service.client.product_client.ProductFilterClientRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ManShirtShop.dto.client.product.ProductResponseClient;
import com.ManShirtShop.dto.client.product_detail_client.ProductFullResponseClient;
import com.ManShirtShop.dto.product.ProductFilterRequest;
import com.ManShirtShop.dto.product.ProductReponse;
import com.ManShirtShop.dto.product.ProductRequest;
import com.ManShirtShop.dto.product_Detail_Image_Dto.ProductAllRequest;
import com.ManShirtShop.entities.Product;

public interface ProductService {
    List<ProductReponse> getAll();

    List<ProductReponse> getAllByFilter(ProductFilterRequest filter);

    ProductReponse create(ProductRequest requet);

    ProductReponse update(ProductAllRequest requet);

    Map<String,Object> delete(Integer id); // ngưng hoạt động sản phẩm

    Map<String,Object> hoatDong(Integer id); // ngưng hoạt động sản phẩm

    Map<String,Object> xoaMem(Integer id); // ngưng hoạt động sản phẩm

    ProductReponse findById(Integer id);

    ProductReponse createProductDetailImage(ProductAllRequest request);

    List<ProductResponseClient> getAllClient();

    ProductFullResponseClient getProductClientById(Integer id);

    List<ProductReponse> getAllByDiscount();

    public List<ProductResponseClient> fillterProductClient(ProductFilterClientRequest productFilterClientRequest);

    public List<ProductResponseClient> getTopProduct();

    public List<ProductResponseClient> getTopNewProduct();

    public List<ProductResponseClient> getTopDiscountProduct();

    public List<ProductResponseClient> getProductClientByDiscountId(String name);
}
