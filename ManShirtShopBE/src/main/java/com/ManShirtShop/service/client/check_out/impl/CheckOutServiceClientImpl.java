package com.ManShirtShop.service.client.check_out.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import com.ManShirtShop.dto.client.check_out.ProductDetailResponseClient;
import com.ManShirtShop.dto.client.product.ColorResponseClient;
import com.ManShirtShop.dto.client.product.SizeResponseClient;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;
import com.ManShirtShop.dto.client.check_out.CheckOutDetailResponseClient;
import com.ManShirtShop.dto.client.check_out.CheckOutResponseClient;
import com.ManShirtShop.dto.client.check_out.ProductDetailCheckOut;
import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClient;
import com.ManShirtShop.dto.client.oderDto.OrderResponseClient;
import com.ManShirtShop.entities.CheckOut;
import com.ManShirtShop.entities.CheckOutDetail;
import com.ManShirtShop.entities.Order;
import com.ManShirtShop.entities.OrderDetail;
import com.ManShirtShop.entities.ProductDetail;
import com.ManShirtShop.repository.CheckOutDetailRepository;
import com.ManShirtShop.repository.CheckOutRepository;
import com.ManShirtShop.repository.OderRepository;
import com.ManShirtShop.repository.ProductDetailRepository;
import com.ManShirtShop.repository.ProductDiscountRepository;
import com.ManShirtShop.repository.ProductImageRepository;
import com.ManShirtShop.service.client.check_out.CheckOutServiceClient;

@Service
public class CheckOutServiceClientImpl implements CheckOutServiceClient {

    @Autowired
    OderRepository oderRepository;

    @Autowired
    CheckOutRepository checkOutRepository;

    @Autowired
    CheckOutDetailRepository checkOutDetailRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    ProductImageRepository productImageRepository;

    @Autowired
    ProductDiscountRepository productDiscountRepository;

    @Override
    @Transactional
    public Map<String, String> create(List<ProductDetailCheckOut> lsDetailCheckOut) {
        if (lsDetailCheckOut.isEmpty() || lsDetailCheckOut == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "rỗng");
        }
        UUID uuid = UUID.randomUUID();
        CheckOut checkOut = new CheckOut();
        checkOut.setCode(uuid.toString());
        checkOut.setCreateBy("User");
        checkOut.setCreateTime(Timestamp.from(Instant.now()));
        checkOut = checkOutRepository.save(checkOut);
        Integer index = 0;
        List<CheckOutDetail> lstCheckOutDetails = new ArrayList<>();
        for (ProductDetailCheckOut x : lsDetailCheckOut) {
            Optional<ProductDetail> productDetail = productDetailRepository.findById(x.getId());
            if (productDetail == null || productDetail.get().getStatus() == 1) {
                throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Không có ProductDetail hoặc đã ngừng bán");
            }
            System.out.println(x.getId());
            System.out.println(lsDetailCheckOut.get(index).getId());
            CheckOutDetail checkOutDetail = new CheckOutDetail();
            checkOutDetail.setCheckOut(checkOut);
            checkOutDetail.setProductDetail(productDetail.get());
            checkOutDetail.setQuantity(lsDetailCheckOut.get(index).getQuantity());
            index = index + 1;
            lstCheckOutDetails.add(checkOutDetail);
        }
        checkOutDetailRepository.saveAll(lstCheckOutDetails);
        Map map = new HashMap<>();
        map.put("code", uuid.toString());
        return map;
    }

    @Override
    public Map<String, Object> get(String code) {
        Map<String, Object> map = new HashMap<>();
        if (code == null || code.isBlank()) {
            map.put("order", null);
            map.put("checkOut", null);
            map.put("status", false);
            return map;
        }

        Optional<CheckOut> checkOut = checkOutDetailRepository.findByCode(code);
        if (checkOut == null || checkOut.isEmpty()) {
            map.put("order", null);
            map.put("checkOut", null);
            map.put("status", false);
            return map;
        }
        if (checkOut.get().getCodeOder() != null) {
            Optional<Order> order = oderRepository.findByCode(checkOut.get().getCodeOder());
            if (order == null) {
                map.put("order", null);
                map.put("checkOut", null);
                map.put("status", false);
                return map;
            }
            // System.out.println(order.get().getId());
            OrderResponseClient od = ObjectMapperUtils.map(order.get(), OrderResponseClient.class);
            Integer indexOrderDetail = 0;
            for (OrderDetail x : order.get().getOrderDetail()) {
                od.getOrderDetail().get(indexOrderDetail)
                        .setProductDetailId(ObjectMapperUtils.map(x.getProductDetail(), ProductDetailResponse.class));
                indexOrderDetail = indexOrderDetail + 1;
            }
            map.put("order", od);
            map.put("checkOut", null);
            map.put("status", true);
            return map;
        }
        CheckOutResponseClient cout = ObjectMapperUtils.map(checkOut, CheckOutResponseClient.class);
        cout.setCheckOutDetails(
                ObjectMapperUtils.mapAll(checkOut.get().getCheckOutDetai(), CheckOutDetailResponseClient.class));
        Integer indexCheckOutDetail = 0;
        System.out.println("1");
        for (CheckOutDetail x : checkOut.get().getCheckOutDetai()) {
            if (x.getProductDetail().getStatus() == 1) {
                throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "đã ngừng bán");
            }
            String url = productImageRepository.findByProductIdAndColorId(x.getProductDetail().getProduct().getId(),
                    x.getProductDetail().getColor().getId());
            String namePrd = x.getProductDetail().getProduct().getName();
            Integer persen = productDiscountRepository.getPersen(x.getProductDetail().getProduct().getId());
            Double giaSp = x.getProductDetail().getProduct().getPrice();
             System.out.println(2);
            if (url == null) {
                System.out.println("5");
                System.out.println(x.getProductDetail().getProduct().getId());
                System.out.println(x.getProductDetail().getColor().getId());
                List<String> lstUrl = productImageRepository.findByProductIdAndColorIdMainImg(
                        x.getProductDetail().getProduct().getId(),
                        x.getProductDetail().getColor().getId());
                cout.getCheckOutDetails().get(indexCheckOutDetail).getProductDetail().setUrl(lstUrl.get(0));
            } else {
                cout.getCheckOutDetails().get(indexCheckOutDetail).getProductDetail().setUrl(url);
            }
            System.out.println("4");
            if (persen == null) {
                cout.getCheckOutDetails().get(indexCheckOutDetail).getProductDetail().setDiscountSp(0);
            } else {
                cout.getCheckOutDetails().get(indexCheckOutDetail).getProductDetail().setDiscountSp(persen);
            }
            cout.getCheckOutDetails().get(indexCheckOutDetail).getProductDetail().setGiaSp(giaSp);
            ;
            cout.getCheckOutDetails().get(indexCheckOutDetail).getProductDetail().setNameProduct(namePrd);
            indexCheckOutDetail = indexCheckOutDetail + 1;
        }
        System.out.println("3");
        map.put("order", null);
        map.put("checkOut", cout);
        map.put("status", true);
        return map;
    }

    @Override
    public Map<String, Object> checkCart(List<ProductDetailCheckOut> lsDetailCheckOut) {
        List<CheckOutDetailResponseClient> lstCheckOutDetails = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        Integer i = 0;
        for (ProductDetailCheckOut x : lsDetailCheckOut) {
            i = i+1;
            Optional<ProductDetail> productDetail = productDetailRepository.findById(x.getId());
            if (productDetail == null || productDetail.get().getStatus() == 1 || productDetail.get().getProduct().getStatus() == 1) {
                System.out.println(x.getId());
                continue;
            }
            if (productDetail.get().getQuantity() <= 0 || productDetail.get().getQuantity() < x.getQuantity()) {
                continue;
            }
            System.out.println(x.getId());
            CheckOutDetailResponseClient res = new CheckOutDetailResponseClient();
            com.ManShirtShop.dto.client.check_out.ProductDetailResponseClient detail = new ProductDetailResponseClient();
            String url = productImageRepository.findByProductIdAndColorId(productDetail.get().getProduct().getId(),
                    productDetail.get().getColor().getId());
            if (url != null) {
                detail.setUrl(url);
            }
            Integer persen = productDiscountRepository.getPersen(productDetail.get().getProduct().getId());
            if (persen == null) {
                detail.setDiscountSp(0);
            } else {
                detail.setDiscountSp(persen);
            }

            res.setQuantity(x.getQuantity());
            detail.setQuantity(x.getQuantity());
            detail.setProductId(productDetail.get().getProduct().getId());
            detail.setBarCode(productDetail.get().getBarCode());
            detail.setId(productDetail.get().getId());
            detail.setColor(new ColorResponseClient(productDetail.get().getColor().getId(), productDetail.get().getColor().getName()));
            detail.setSize(new SizeResponseClient(productDetail.get().getSize().getId(), productDetail.get().getSize().getCode(), productDetail.get().getSize().getStatus()));
            detail.setNameProduct(productDetail.get().getProduct().getName());
            detail.setGiaSp(productDetail.get().getProduct().getPrice());
            res.setProductDetail(detail);
            lstCheckOutDetails.add(res);
        }
        System.out.println(i);
        map.put("status", true);
        map.put("data", lstCheckOutDetails);
        map.put("message", "ok");
        return map;
    }

}
