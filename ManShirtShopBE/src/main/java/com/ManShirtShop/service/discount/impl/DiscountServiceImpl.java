package com.ManShirtShop.service.discount.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.common.mapperUtil.ResponseFormat;
import com.ManShirtShop.dto.discount.DiscountRequest;
import com.ManShirtShop.dto.discount.DiscountResponse;
import com.ManShirtShop.dto.product_Discount.ProductDiscoutRequest;
import com.ManShirtShop.entities.Discount;
import com.ManShirtShop.entities.ProductDiscount;
import com.ManShirtShop.repository.DiscountRepository;
import com.ManShirtShop.repository.ProductDiscountRepository;
import com.ManShirtShop.service.discount.DiscountService;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    ProductDiscountRepository productDiscountRepository;

    @Autowired
    ResponseFormat responseFormat;

    @Override
    public List<DiscountResponse> getAll() {
        List<Discount> geListDb = discountRepository.getAllByStatus();
        return ObjectMapperUtils.mapAll(geListDb, DiscountResponse.class);
    }

    @Override
    @Transactional
    public ResponseEntity<?> create(DiscountRequest request) {
        request.setId(-1);
        Discount entity = ObjectMapperUtils.map(request, Discount.class);
        if (entity.getStartDate().isAfter(entity.getEndDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ngày bắt đầu phải bé hơn ngày kết thúc");
        }
        entity.setStatus(0);
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity = discountRepository.save(entity);
        // if (!request.getLstProductDiscount().isEmpty() || !(request.getLstProductDiscount() == null)) {
        //     List<ProductDiscount> list = new ArrayList<>();
        //     for (ProductDiscoutRequest x : request.getLstProductDiscount()) {
        //         ProductDiscount e = new ProductDiscount();
        //         e.setId(-1);
        //         e.setDiscountId(entity);
        //         e.setProductId(null);
        //         e.setPercent(x.getPercent());
        //         list.add(e);
        //     }
        //     productDiscountRepository.saveAll(list);
        // }
        return ResponseEntity.ok(ObjectMapperUtils.map(entity, DiscountResponse.class));
    }

    @Override
    public ResponseEntity<?> update(DiscountRequest request) {
        if (!checkIb(request.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy Discount");
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ngày bắt đầu phải bé hơn ngày kết thúc");
        }
        Discount entityDB = discountRepository.findById(request.getId()).get();
        Discount entity = ObjectMapperUtils.map(request, Discount.class);
        entity.setCreateBy(entityDB.getCreateBy());
        entity.setCreateTime(entityDB.getCreateTime());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        entity = discountRepository.save(entity);
        return ResponseEntity.ok(ObjectMapperUtils.map(entity, DiscountResponse.class));
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        if (!checkIb(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy Discount");
        }
        Discount e = discountRepository.findById(id).get();
        e.setStatus(1);
        e = discountRepository.save(e);
        return ResponseEntity.ok(ObjectMapperUtils.map(e, DiscountResponse.class));
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
        if (!checkIb(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy Discount");
        }
        Discount discount = discountRepository.findById(id).get();
        return ResponseEntity.ok(ObjectMapperUtils.map(discount, DiscountResponse.class));
    }

    @Override
    public ResponseEntity<?> findByName(String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy Discount");
        }
        Optional<Discount> discount = discountRepository.getByname(name);
        if (discount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy Discount");
        }
        return ResponseEntity.ok(ObjectMapperUtils.map(discount.get(), DiscountResponse.class));
    }

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!discountRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }


}
