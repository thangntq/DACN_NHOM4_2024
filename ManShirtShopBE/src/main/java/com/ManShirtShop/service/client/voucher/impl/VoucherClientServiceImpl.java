package com.ManShirtShop.service.client.voucher.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.client.oderDto.ProductDetailOderRequet;
import com.ManShirtShop.dto.voucher.VoucherResponse;
import com.ManShirtShop.entities.ProductDetail;
import com.ManShirtShop.entities.ProductDiscount;
import com.ManShirtShop.entities.Voucher;
import com.ManShirtShop.repository.ProductDetailRepository;
import com.ManShirtShop.repository.ProductDiscountRepository;
import com.ManShirtShop.repository.ProductRepository;
import com.ManShirtShop.repository.VoucherRepository;
import com.ManShirtShop.service.client.voucher.VoucherClientService;

@Service
public class VoucherClientServiceImpl implements VoucherClientService {

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    ProductDiscountRepository productDiscountRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Override
    public List<VoucherResponse> getAllVoucherIsOrderDetail(List<ProductDetailOderRequet> lstId) {
        List<Integer> listIdProducDetail = new ArrayList<>();
        for (ProductDetailOderRequet x : lstId) {
            listIdProducDetail.add(x.getId());
        }
        List<ProductDetail> lProductDetails = productDetailRepository.findAllById(listIdProducDetail);
        Integer index = 0;
        Double toltal = 0.0;
        for (ProductDetail x : lProductDetails) {
            Optional<ProductDiscount> productDisOptional = productDiscountRepository
                    .getByProductI2dAndStatus(x.getProduct().getId());
            if (productDisOptional.isPresent()) {
                toltal = toltal + (
                        (x.getProduct().getPrice()
                        - (x.getProduct().getPrice() * productDisOptional.get().getPercent() / 100))
                        * lstId.get(index).getQuantity()
                        );
            } else {
                toltal = toltal + (x.getProduct().getPrice() * lstId.get(index).getQuantity());
            }
            index++;
        }
        System.out.println(toltal);
        List<Voucher> lst = voucherRepository.findClientByTotal(toltal);
        return ObjectMapperUtils.mapAll(lst, VoucherResponse.class);
    }

}
