package com.ManShirtShop.service.client.oder;

import java.util.List;

import com.ManShirtShop.dto.ObjectResult;
import com.ManShirtShop.dto.client.oderDto.OrderAllClient;
import com.ManShirtShop.dto.client.oderDto.OrderClientInSucces;
import com.ManShirtShop.dto.client.oderDto.OrderRequestClient;
import com.ManShirtShop.dto.client.oderDto.OrderResponseClient;
import com.ManShirtShop.dto.order_the_store.OrderResponeAdmin;

public interface OderClientService {
    ObjectResult create(OrderRequestClient oderRequest);

    OrderResponseClient findByIdOrder(Integer id);

    OrderResponseClient findByCode(String code);

    List<OrderAllClient> findByIdClient();

    Boolean updateStatusPay(String code);
    // List<OrderResponseClient> getAllOrderByClient(Integer id);

    OrderResponseClient huyOrder(Integer id);

    OrderClientInSucces findByCodeSucces(String code);

    OrderResponseClient updateOrderClient(OrderRequestClient oderRequest);
}
