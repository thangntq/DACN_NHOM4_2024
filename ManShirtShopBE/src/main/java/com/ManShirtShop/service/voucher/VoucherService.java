package com.ManShirtShop.service.voucher;

import com.ManShirtShop.dto.voucher.VoucherRequest;
import com.ManShirtShop.dto.voucher.VoucherResponse;

import java.util.List;

public interface VoucherService {
    VoucherResponse findByIdWithOrder(Integer voucherId);

    List<VoucherResponse> getAll();
    VoucherResponse create(VoucherRequest request);

    List<VoucherResponse> getActiveVouchers();

    List<VoucherResponse> getUpcomingVouchers();

    VoucherResponse delete(Integer id);
    VoucherResponse findById(Integer id);
    VoucherResponse findByCode(String code);
}
