package com.ManShirtShop.service.voucher.impl;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.voucher.VoucherRequest;
import com.ManShirtShop.dto.voucher.VoucherResponse;
import com.ManShirtShop.entities.Voucher;
import com.ManShirtShop.repository.VoucherRepository;
import com.ManShirtShop.service.voucher.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class VoucherImpl implements VoucherService {
    @Autowired
    VoucherRepository voucherRepository;

    /*Lấy thông tin về một Voucher cụ thể kèm theo danh sách các Order liên quan đến nó,
    mà không cần lấy kèm danh sách Order */
    @Override
    public VoucherResponse findByIdWithOrder(Integer voucherId) {
        Voucher voucher = voucherRepository.findByIdWithOrder(voucherId);
        if (voucher == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voucher not found");
        }

        VoucherResponse voucherResponse = ObjectMapperUtils.map(voucher, VoucherResponse.class);
        return voucherResponse;
    }

    @Override
    public List<VoucherResponse> getAll() {
        List<VoucherResponse> listAll = ObjectMapperUtils.mapAll(voucherRepository.findAll(), VoucherResponse.class)
                .stream().filter(e->e.getStatus()==0).toList();;
        return listAll;
    }

    @Override
    public VoucherResponse create(VoucherRequest request) {
        request.setId(null);
        if (request.getStartDate().isAfter(request.getEndDate()) ||
                request.getStartDate().equals(request.getEndDate())) {
            // Trường hợp không hợp lệ: ngày bắt đầu lớn hơn ngày kết thúc hoặc cùng một ngày
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid start and end dates");
        }

        double min = request.getMin(); // Lấy giá trị Min từ request
        double max = request.getMax(); // Lấy giá trị Max từ request
        double discount = request.getDiscount(); // Lấy giá trị Discount từ request

        if (min < 0 || max < 0 || discount < 0 || (!request.getType() && discount > 100)) {
            // Trường hợp không hợp lệ: Min, Max, Discount nhỏ hơn 0 và Discount không được giảm giá vượt quá 100%
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid values for Min, Max, or Discount");
        }

        String giftCode = generateRandomCode();
        while (isGiftCodeExists(giftCode)) {
            giftCode = generateRandomCode();
        }

        Voucher entity = new Voucher();
        entity.setName(request.getName());
        entity.setCode(giftCode);
        entity.setDescription(request.getDescription());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());

        if (request.getType()) {
            // Trường hợp Type = True (Giảm giá theo số tiền)
            entity.setType(true);
            entity.setMin(min); // Số tiền tối thiểu do Admin Set nhưng không được < 0 vnd
            entity.setMax(null); // Không có giới hạn số tiền giảm tối đa
            entity.setDiscount(discount); // Số tiền giảm được truyền vào từ Request
        } else {
            // Trường hợp Type = False (Giảm giá theo phần trăm)
            entity.setType(false);
            entity.setMin(min); // Số tiền tối thiểu để sử dụng Voucher
            entity.setMax(max); // Số tiền giảm tối đa
            entity.setDiscount(discount); // Phần trăm giảm được truyền vào từ Request
        }

        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);

        entity = voucherRepository.save(entity);
        return ObjectMapperUtils.map(entity, VoucherResponse.class);
    }

    @Override
    public List<VoucherResponse> getActiveVouchers() {
        Instant currentInstant = Instant.now();
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentInstant, ZoneId.systemDefault());

        List<Voucher> activeVouchers = voucherRepository.findByStatusAndEndDateGreaterThan(0, currentDateTime);
        List<VoucherResponse> activeVoucherResponses = ObjectMapperUtils.mapAll(activeVouchers, VoucherResponse.class);

        return activeVoucherResponses;
    }


    @Override
    public List<VoucherResponse> getUpcomingVouchers() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        List<Voucher> upcomingVouchers = voucherRepository.findByStatusAndStartDateGreaterThanAndEndDateGreaterThan(0, currentDateTime, currentDateTime.plusDays(1));
        List<VoucherResponse> upcomingVoucherResponses = ObjectMapperUtils.mapAll(upcomingVouchers, VoucherResponse.class);

        return upcomingVoucherResponses;
    }

    @Override
    public VoucherResponse delete(Integer id) {
        if (!checkIb(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voucher not found");
        }
        Voucher e = voucherRepository.findById(id).get();
        e.setStatus(1);
        e = voucherRepository.save(e);
        return ObjectMapperUtils.map(e, VoucherResponse.class);
    }

    @Override
    public VoucherResponse findById(Integer id) {
        if (!checkIb(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voucher not found");
        }
        Voucher voucher = voucherRepository.findById(id).get();
        return ObjectMapperUtils.map(voucher, VoucherResponse.class);
    }
    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!voucherRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }
    // Generate random GiftCode
    private String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();
        while (code.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            code.append(chars.charAt(index));
        }
        return code.toString();
    }

    public boolean isGiftCodeExists(String giftCode) {
        return voucherRepository.existsByCode(giftCode);
    }

    @Override
    public VoucherResponse findByCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voucher not found");
        }
        Optional<Voucher> voucher = voucherRepository.findByStatusAndCode(code);
        if (voucher == null || voucher.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voucher not found");
        }
        return ObjectMapperUtils.map(voucher.get(), VoucherResponse.class);
    }
}
