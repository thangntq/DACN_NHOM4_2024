package com.ManShirtShop.service.client.order_detail.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;
import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClient;
import com.ManShirtShop.dto.client.oderDto.ProductDetailOderRequet;
import com.ManShirtShop.dto.order_the_store.OderDetailResponseAdmin;
import com.ManShirtShop.dto.order_the_store.ProductDetailOderRequetAdmin;
import com.ManShirtShop.entities.Order;
import com.ManShirtShop.entities.OrderDetail;
import com.ManShirtShop.entities.ProductDetail;
import com.ManShirtShop.entities.ProductDiscount;
import com.ManShirtShop.repository.OderRepository;
import com.ManShirtShop.repository.OrderDetailRepositoty;
import com.ManShirtShop.repository.ProductDetailRepository;
import com.ManShirtShop.repository.ProductDiscountRepository;
import com.ManShirtShop.repository.ReturnRepository;
import com.ManShirtShop.service.client.order_detail.OrderDetailServiceClient;
import com.ManShirtShop.service.oderDetail.impl.OderdetailServiceImpl;

@Service
public class OrderDetailServiceClientImpl implements OrderDetailServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OderdetailServiceImpl.class);
    @Autowired
    OrderDetailRepositoty orderDetailRepositoty;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    OderRepository oderRepository;

    @Autowired
    ReturnRepository returnRepository;
    @Autowired
    ProductDiscountRepository productDiscountRepository;

    @Override
    @Transactional
    public List<OrderDetailResponseClient> create(List<ProductDetailOderRequet> lstProductDetail, Integer orderId) {
        if (orderId <= 0) {
            LOGGER.error(OderdetailServiceImpl.class + ": Không tìm thấy id Oder" + orderId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy id order", null);
        }
        Optional<Order> order = oderRepository.findById(orderId);
        if (order == null || order.isEmpty()) {
            LOGGER.error(OderdetailServiceImpl.class + ": Không tìm thấy id Oder" + orderId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy id order", null);
        }
        List<OrderDetail> lstEntity = new ArrayList<>();
        List<ProductDetail> lstProductDetailDB = new ArrayList<>();
        for (ProductDetailOderRequet x : lstProductDetail) {
            if (x.getId() <= 0 || x.getId() == null) {
                LOGGER.error(OderdetailServiceImpl.class + ": Dữ liệu id product detail sai", lstProductDetail);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Product Detail", null);
            }
        }
        for (ProductDetailOderRequet x : lstProductDetail) {
            Optional<ProductDetail> productDetailDB = productDetailRepository.findById(x.getId());
            if (productDetailDB == null || productDetailDB.isEmpty()) {
                LOGGER.error(OderdetailServiceImpl.class + ": Không tìm thấy id Product Detail", lstProductDetail);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy Product Detail", null);
            } else {
                OrderDetail entity = new OrderDetail();
                ProductDetail productDetail = productDetailDB.get();
                Optional<ProductDiscount> discount = productDiscountRepository
                        .getByProductI2dAndStatus(productDetail.getProduct().getId());
                if (discount.isPresent()) {
                    entity.setDisCount(discount.get().getPercent());
                } else {
                    entity.setDisCount(0);
                }
                // entity.setReturnId(null);
                entity.setOrder(order.get());
                entity.setProductDetail(productDetail);
                entity.setCreateBy("Khách Hàng");
                entity.setCreateTime(Timestamp.from(Instant.now()));
                entity.setQuantity(x.getQuantity());
                entity.setUnitprice(productDetail.getProduct().getPrice());
                entity.setStatus(0);
                lstEntity.add(entity);
                lstProductDetailDB.add(productDetail);
            }
        }
        lstEntity = orderDetailRepositoty.saveAll(lstEntity);
        List<OrderDetailResponseClient> list = ObjectMapperUtils.mapAll(lstEntity, OrderDetailResponseClient.class);
        int index = 0;
        for (OrderDetailResponseClient od : list) {
            od.setProductDetailId(ObjectMapperUtils.map(lstProductDetailDB.get(index), ProductDetailResponse.class));
            index = index + 1;
            System.out.println("--------" + od.getProductDetailId().toString() + "--------||");
        }
        return list;
    }

}
