package com.ManShirtShop.service.client.order_detail;

import java.util.List;

import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClient;
import com.ManShirtShop.dto.client.oderDto.ProductDetailOderRequet;

public interface OrderDetailServiceClient {
     List<OrderDetailResponseClient> create(List<ProductDetailOderRequet> lstProductDetail,Integer orderId);
}
