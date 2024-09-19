package com.ManShirtShop.service.oderDetail.impl;

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
import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailRequestClient;
import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClient;
import com.ManShirtShop.dto.client.oderDto.ProductDetailOderRequet;
import com.ManShirtShop.dto.order_the_store.OderDetailResponseAdmin;
import com.ManShirtShop.dto.order_the_store.ProductDetailOderRequetAdmin;
import com.ManShirtShop.entities.Order;
import com.ManShirtShop.entities.OrderDetail;
import com.ManShirtShop.entities.ProductDetail;
import com.ManShirtShop.entities.ProductDiscount;
import com.ManShirtShop.entities.Return;
import com.ManShirtShop.repository.OderRepository;
import com.ManShirtShop.repository.OrderDetailRepositoty;
import com.ManShirtShop.repository.ProductDetailRepository;
import com.ManShirtShop.repository.ProductDiscountRepository;
import com.ManShirtShop.repository.ReturnRepository;
import com.ManShirtShop.service.oderDetail.OderDetailService;

@Service
public class OderdetailServiceImpl implements OderDetailService {
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
    public List<OderDetailResponseAdmin> create(List<ProductDetailOderRequetAdmin> idProductDetail, Integer idOder) {
        if (idOder <= 0) {
            LOGGER.error(OderdetailServiceImpl.class + ": Không tìm thấy id Oder" + idOder);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy id order", null);
        }
        Optional<Order> order = oderRepository.findById(idOder);
        if (order == null || order.isEmpty()) {
            LOGGER.error(OderdetailServiceImpl.class + ": Không tìm thấy id Oder" + idOder);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy id order", null);
        }
        List<OrderDetail> lstEntity = new ArrayList<>();
        List<ProductDetail> lstProductDetail = new ArrayList<>();
        for (ProductDetailOderRequetAdmin x : idProductDetail) {
            if (x.getId() <= 0 || x.getId() == null) {
                LOGGER.error(OderdetailServiceImpl.class + ": Dữ liệu id product detail sai", idProductDetail);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Product Detail", null);
            }
        }
        for (ProductDetailOderRequetAdmin x : idProductDetail) {
            OrderDetail entity = new OrderDetail();
            Optional<ProductDetail> productDetailDB = productDetailRepository.findById(x.getId());
            if (productDetailDB == null || productDetailDB.isEmpty()) {
                LOGGER.error(OderdetailServiceImpl.class + ": Không tìm thấy id Product Detail", idProductDetail);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy Product Detail", null);
            } else {
                ProductDetail productDetail = productDetailDB.get();
                Optional<ProductDiscount> discount = productDiscountRepository
                        .getByProductI2dAndStatus(productDetail.getProduct().getId());

                Integer quantity = productDetail.getQuantity() - x.getQuantity();
                if (quantity < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số lượng mua vượt quá số lượng đang có",
                            null);
                }
                if (discount.isPresent()) {
                    entity.setDisCount(discount.get().getPercent());
                } else {
                    entity.setDisCount(0);
                }
                productDetail.setQuantity(quantity);
                // entity.setReturnId(null);
                entity.setOrder(order.get());
                entity.setProductDetail(productDetail);
                entity.setCreateBy("admin");
                entity.setCreateTime(Timestamp.from(Instant.now()));
                entity.setQuantity(x.getQuantity());
                entity.setUnitprice(productDetail.getProduct().getPrice());
                entity.setStatus(0);
                lstEntity.add(entity);
                lstProductDetail.add(productDetail);
            }
        }
        lstEntity = orderDetailRepositoty.saveAll(lstEntity);
        productDetailRepository.saveAll(lstProductDetail);
        List<OderDetailResponseAdmin> list = ObjectMapperUtils.mapAll(lstEntity, OderDetailResponseAdmin.class);
        int index = 0;
        for (OderDetailResponseAdmin od : list) {
            od.setProductDetailId(ObjectMapperUtils.map(lstProductDetail.get(index), ProductDetailResponse.class));
            index = index + 1;
        }
        return list;
    }

    public Boolean checkId(Integer id, Boolean checkDB) {
        if (id <= 0 || id == null) {
            return true;
        }
        if (!checkDB) { // check id db
            return true;
        }
        return false;
    }

    @Override
    public OderDetailResponseAdmin findById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    @Transactional
    public void deleteByIdOrder(List<OrderDetail> lst) {
        try {
            for (OrderDetail x : lst) {
                productDetailRepository.updateCongQuantity(x.getQuantity(), x.getProductDetail().getId());
            }
            orderDetailRepositoty.deleteAll(lst);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Liên Hệ Admin đi cu",
                    null);
        }

    }

    @Override
    public void updateQuantityProductDetail(List<OrderDetail> lst) {
        try {
            for (OrderDetail x : lst) {
                productDetailRepository.updateCongQuantity(x.getQuantity(), x.getProductDetail().getId());
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Liên Hệ Admin đi cu",null);
        }
    }

}
