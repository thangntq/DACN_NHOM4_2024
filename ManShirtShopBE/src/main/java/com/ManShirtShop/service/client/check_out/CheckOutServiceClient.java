package com.ManShirtShop.service.client.check_out;

import java.util.List;
import java.util.Map;

import com.ManShirtShop.dto.client.check_out.CheckOutResponseClient;
import com.ManShirtShop.dto.client.check_out.ProductDetailCheckOut;

public interface CheckOutServiceClient {
    Map<String,String> create(List<ProductDetailCheckOut> lsDetailCheckOut);
    Map<String,Object> get(String code);

    Map<String,Object> checkCart(List<ProductDetailCheckOut> lsDetailCheckOut);

}
