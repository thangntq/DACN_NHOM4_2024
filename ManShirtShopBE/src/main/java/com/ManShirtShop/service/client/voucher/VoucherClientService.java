package com.ManShirtShop.service.client.voucher;

import java.util.List;

import com.ManShirtShop.dto.client.oderDto.ProductDetailOderRequet;
import com.ManShirtShop.dto.voucher.VoucherResponse;

public interface VoucherClientService {
    List<VoucherResponse> getAllVoucherIsOrderDetail(List<ProductDetailOderRequet> lstId);
}
