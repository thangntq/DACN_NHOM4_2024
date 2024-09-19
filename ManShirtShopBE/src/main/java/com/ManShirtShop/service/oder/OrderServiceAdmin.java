package com.ManShirtShop.service.oder;

import java.util.List;
import java.util.Map;

import com.ManShirtShop.dto.client.oderDto.OrderRequestClient;
import com.ManShirtShop.dto.client.oderDto.OrderResponseClient;
import com.ManShirtShop.dto.order_the_store.OrderAllAdminByStatus;
import com.ManShirtShop.dto.order_the_store.OrderRequestAdmin;
import com.ManShirtShop.dto.order_the_store.OrderResponeAdmin;
import com.ManShirtShop.dto.order_the_store.SumAllOrder;

public interface OrderServiceAdmin {
    OrderResponeAdmin create(OrderRequestAdmin oderRequest);

    OrderResponeAdmin findById(Integer id);

    List<OrderAllAdminByStatus> findByStatus(Integer status);

    OrderResponeAdmin findByCode(String code);

    List<OrderResponeAdmin> updateStatus(List<Integer> id, Integer status);

    List<OrderResponeAdmin> findALl();

    List<OrderResponeAdmin> updateStatusChuanBiHang(List<Integer> lstId);

    List<OrderResponeAdmin> updateStatusThanhCong(List<Integer> lstId);
    
    Map<String,Object> updateStatusGiaoHang(Integer lstId);

    Map<String,Object> updateStatusHuy(Integer idOrder);

    Map<String,Object> updateStatusGiaoThatBai(Integer idOrder);

     OrderResponeAdmin updateOrder(OrderRequestAdmin request);

    SumAllOrder sumAllOrder();
}
