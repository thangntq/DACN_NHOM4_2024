package com.ManShirtShop.service.oderDetail;

import java.util.List;

import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailRequestClient;
import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClient;
import com.ManShirtShop.dto.client.oderDto.ProductDetailOderRequet;
import com.ManShirtShop.dto.order_the_store.OderDetailResponseAdmin;
import com.ManShirtShop.dto.order_the_store.ProductDetailOderRequetAdmin;
import com.ManShirtShop.entities.OrderDetail;

public interface OderDetailService {
    List<OderDetailResponseAdmin> create(List<ProductDetailOderRequetAdmin> lstRequest,Integer inOder);

    OderDetailResponseAdmin findById(Integer id);

    void deleteByIdOrder(List<OrderDetail> lst);

    void updateQuantityProductDetail(List<OrderDetail> lst);
}
