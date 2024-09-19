package com.ManShirtShop.service.rating.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import com.ManShirtShop.Authentication.dto.user.EmailUser;
import com.ManShirtShop.dto.rating_dto.ProductDetailRating;
import com.ManShirtShop.dto.rating_image_dto.RatingImageResponse;
import com.ManShirtShop.entities.*;
import com.ManShirtShop.repository.*;
import com.ManShirtShop.service.rating_image.RatingImageService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.rating_dto.RatingRequest;
import com.ManShirtShop.dto.rating_dto.RatingResponse;
import com.ManShirtShop.dto.rating_dto.Status;
import com.ManShirtShop.service.customer.CustomerService;
import com.ManShirtShop.service.oder.OrderServiceAdmin;
import com.ManShirtShop.service.product.ProductService;
import com.ManShirtShop.service.rating.RatingService;
import com.ManShirtShop.service.size.SizeService;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OderRepository orderRepository;

    @Autowired
    RatingImageService ratingImageService;

    @Autowired
    RatingImageRepository ratingImageRepository;
    private String name;

    @Override
    public List<RatingResponse> getAll() {
        List<RatingResponse> listAll = ratingRepository.findAll().stream()
                .map(x -> mapObjectRating(x)).toList();
        return listAll;
    }

    @Override
    public RatingResponse Create(RatingRequest request) {
        Integer id = 0;
        String name = "";
        try {
            id = customerRepository.findIdByEmail(EmailUser.getEmailUser());
            name = customerRepository.findFullNameByEmail(EmailUser.getEmailUser());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Custommer", null);
        }
        Customer cus = new Customer();
        cus.setId(id);
        Optional<Product> opProduct = productRepository.findById(request.getProductId());
        Optional<Order> order = orderRepository.findById(request.getOrderId());
        if (order.get().getCustomer().getId() != id) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không có quyền dánh giá order này", null);
        }
        Optional<Rating> ratingCheck = ratingRepository.findByOrderAndCustomAndProductId(order.get().getId(), id, opProduct.get().getId());
        if (ratingCheck.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đã đánh giá rồi cu", null);
        }
        request.setId(null);
        Rating entity = ObjectMapperUtils.map(request, Rating.class);
        entity.setRating(request.getRating());
        entity.setContent(request.getContent());
        entity.setStatus(Status.EFFECT);
        entity.setCustomer(cus);
        try {
            if (!opProduct.isPresent()) {
                throw new IllegalArgumentException("product not found");
            }
            entity.setProduct(opProduct.get());
        } catch (Exception e) {
        }
        try {
            if (!order.isPresent()) {
                throw new IllegalArgumentException("order not found");
            }
            entity.setOrder(order.get());
        } catch (Exception e) {
        }
        entity.setCreateBy(name);
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity = ratingRepository.save(entity);
        List<RatingImage> lstIamge = new ArrayList<>();
        for (String url : request.getIamges()) {
            RatingImage rImage = new RatingImage();
            rImage.setImage(url);
            rImage.setCreateBy(customerRepository.findFullNameByEmail(EmailUser.getEmailUser()));
            rImage.setCreateTime(Timestamp.from(Instant.now()));
            rImage.setRating(entity);
            rImage.setCreateBy(name);
            lstIamge.add(rImage);
        }
        ratingImageRepository.saveAll(lstIamge);
        RatingResponse respone = ObjectMapperUtils.map(entity, RatingResponse.class);
        return respone;
    }

    @Override
    public RatingResponse update(RatingRequest request) {
        if (!checkIb(request.getId())) {
            return null;
        }
        Integer id = customerRepository.findIdByEmail(EmailUser.getEmailUser());
        String name = customerRepository.findFullNameByEmail(EmailUser.getEmailUser());
        Customer cus = new Customer();
        cus.setId(id);
//        Optional<Customer> customer = customerRepository.findById(request.getCustomerId());
        Optional<Product> opProduct = productRepository.findById(request.getProductId());
        Optional<Order> order = orderRepository.findById(request.getOrderId());

        Rating eDb = ratingRepository.findById(request.getId()).get();
        Rating entity = ObjectMapperUtils.map(request, Rating.class);
        entity.setRating(request.getRating());
        entity.setContent(request.getContent());
//        entity.setStatus(request.getStatus());
//        try {
//            if (!customer.isPresent()) {
//                throw new IllegalArgumentException("customer not found");
//            }
//            entity.setCustomer(customer.get());
//        } catch (Exception e) {
//        }
        try {
            if (!opProduct.isPresent()) {
                throw new IllegalArgumentException("product not found");
            }
            entity.setProduct(opProduct.get());
        } catch (Exception e) {
        }
        try {
            if (!order.isPresent()) {
                throw new IllegalArgumentException("order not found");
            }
            entity.setOrder(order.get());
        } catch (Exception e) {
        }
        entity.setUpdateBy("admin");
        entity.setUpdateTime(Timestamp.from(Instant.now()));
        entity.setCreateBy(eDb.getCreateBy());
        entity.setCreateTime(eDb.getCreateTime());
        return ObjectMapperUtils.map(ratingRepository.save(entity), RatingResponse.class);
    }

    @Override
    public RatingResponse delete(Integer id) {
        if (!checkIb(id)) {
            return null;
        }
        Rating e = ratingRepository.findById(id).get();
        e.setStatus(Status.DELETE);
        ratingRepository.save(e);
        return ObjectMapperUtils.map(e, RatingResponse.class);
    }

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!ratingRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public List<RatingResponse> findById(Integer id) {// lấy theo id product
        if (id <= 0 || id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Product", null);
        }
        List<Rating> rating = ratingRepository.findByProductId(id);
        List<RatingResponse> lst = rating.stream().map(x -> mapObjectRating(x)).toList();
        return lst;
    }

    @Override
    public Map<String, Object> updateStatusOn(Integer id) {
        Map<String, Object> map = new HashMap<>();
        Optional<Rating> rating = ratingRepository.findById(id);
        if (!rating.isPresent()) {
            map.put("status", false);
            map.put("message", "Không tìm thấy rating");
        }
        rating.get().setStatus(1);
        ratingRepository.save(rating.get());
        map.put("status", true);
        map.put("message", "Thành công");
        return map;
    }

    @Override
    public Map<String, Object> updateStatusOff(Integer id) {
        Map<String, Object> map = new HashMap<>();
        Optional<Rating> rating = ratingRepository.findById(id);
        if (!rating.isPresent()) {
            map.put("status", false);
            map.put("message", "Không tìm thấy rating");
        }
        rating.get().setStatus(0);
        ratingRepository.save(rating.get());
        map.put("status", true);
        map.put("message", "Thành công");
        return map;
    }

    private RatingResponse mapObjectRating(Rating r) {
        RatingResponse response = RatingResponse.builder().id(r.getId())
                .rating(r.getRating())
                .content(r.getContent())
                .customerName(r.getCustomer().getFullname())
                .orderId(r.getOrder().getId())
                .productId(r.getProduct().getId())
                .createTime(r.getCreateTime())
                .updateTime(r.getCreateTime())
                .updateBy(r.getUpdateBy())
                .createBy(r.getCreateBy())
                .createBy(r.getCreateBy())
                .status(r.getStatus())
                .productName(r.getProduct().getName())
                .ratingImage(ObjectMapperUtils.mapAll(r.getRatingImage(), RatingImageResponse.class))
                .build();
        List<ProductDetailRating> lst = new ArrayList<>();
        for (OrderDetail x : r.getOrder().getOrderDetail()) {
           if (x.getProductDetail().getProduct().getId() == r.getProduct().getId())
               lst.add(
                       new ProductDetailRating(x.getProductDetail().getColor().getName(), x.getProductDetail().getSize().getCode()));
        }
        response.setProductDetail(lst);
        return response;
    }
}
