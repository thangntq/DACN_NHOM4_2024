package com.ManShirtShop.service.client.oder.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ManShirtShop.repository.*;
import org.checkerframework.checker.units.qual.g;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ManShirtShop.Authentication.dto.user.EmailUser;
import com.ManShirtShop.common.contans.OrderContant;
import com.ManShirtShop.common.genCode.GenCode;
import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.ObjectResult;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;
import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClient;
import com.ManShirtShop.dto.client.oderDto.IOrderClient;
import com.ManShirtShop.dto.client.oderDto.OrderAllClient;
import com.ManShirtShop.dto.client.oderDto.OrderClientInSucces;
import com.ManShirtShop.dto.client.oderDto.OrderRequestClient;
import com.ManShirtShop.dto.client.oderDto.OrderResponseClient;
import com.ManShirtShop.dto.client.oderDto.ProductDetailOderRequet;
import com.ManShirtShop.dto.client.product.ProductResponseClient;
import com.ManShirtShop.dto.order_the_store.OderDetailResponseAdmin;
import com.ManShirtShop.dto.order_the_store.OrderResponeAdmin;
import com.ManShirtShop.dto.order_the_store.ProductDetailOderRequetAdmin;
import com.ManShirtShop.dto.product_Image_dto.ProductImageResponse;
import com.ManShirtShop.entities.Address;
import com.ManShirtShop.entities.CheckOut;
import com.ManShirtShop.entities.Customer;
import com.ManShirtShop.entities.Employee;
import com.ManShirtShop.entities.Order;
import com.ManShirtShop.entities.OrderDetail;
import com.ManShirtShop.entities.ProductDiscount;
import com.ManShirtShop.entities.Voucher;
import com.ManShirtShop.rest.vnpay.service.VnpayCreate;
import com.ManShirtShop.service.client.oder.OderClientService;
import com.ManShirtShop.service.client.order_detail.OrderDetailServiceClient;
import com.ManShirtShop.service.oder.impl.OrderServiceAdminImpl;
import com.ManShirtShop.service.oderDetail.OderDetailService;
import com.ManShirtShop.service.oderDetail.impl.OderdetailServiceImpl;

@Service
public class OderClientserviceImpl implements OderClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OderdetailServiceImpl.class);
    @Autowired
    OderRepository oderRepository;

    @Autowired
    OrderDetailRepositoty orderDetailRepositoty;

    @Autowired
    CustomerRepository customerRepolsitory;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    OrderDetailServiceClient oderDetailService;

    @Autowired
    ProductDiscountRepository productDiscountRepository;

    @Autowired
    AdressRepository adressRepository;

    @Autowired
    CheckOutRepository checkOutRepository;

    @Autowired
    VnpayCreate vnpayCreate;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    ReturnRepository returnRepository;

    @Override
    @Transactional
    public ObjectResult create(OrderRequestClient oderRequest) {
        if (oderRequest.getId() > 0) {
            Optional<Order> od = oderRepository.findById(oderRequest.getId());
            if (od == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy order :");
            }
            Order order = od.get();
            order.setPaymentType(oderRequest.getPaymentType());
            ObjectResult res = new ObjectResult();
            Map<String, Object> map = new HashMap<>();
            if (oderRequest.getPaymentType().equals("VNPAY")) {
                try {
                    res.setMessage("ok");
                    res.setStatus(true);
                    map.put("link", vnpayCreate.create(order.getTotal(), order.getCode()));
                    map.put("type", true);
                    res.setResponseData(map);
                    return res;
                } catch (UnsupportedEncodingException e) {
                    res.setMessage("ok");
                    res.setResponseData(null);
                    res.setStatus(false);
                    map.put("link", null);
                    map.put("type", null);
                    return res;
                }
            }
            OrderResponseClient oResponse = ObjectMapperUtils.map(order, OrderResponseClient.class);
            res.setMessage("ok");
            res.setStatus(true);
            map.put("detail", oResponse);
            map.put("type", false);
            res.setResponseData(map);
            return res;
        }
        oderRequest.setId(-1);
        Order order = ObjectMapperUtils.map(oderRequest, Order.class);
        if (oderRequest.getVoucher() <= 0 || oderRequest.getVoucher() == null) {
            order.setVoucher(null);
        } else {
            Optional<Voucher> optionalVoucher = voucherRepository.findById(oderRequest.getVoucher());
            if (optionalVoucher == null) {
                order.setVoucher(null);
            } else {
                order.setVoucher(optionalVoucher.get());
            }
        } // check voucher
        Customer eCustomer = new Customer();
        Integer id = customerRepolsitory.findIdByEmail(EmailUser.getEmailUser());
        eCustomer.setId(id);
        Address eAddress = ObjectMapperUtils.map(oderRequest.getAddress(), Address.class);
        if (eAddress.getId() == null || eAddress.getId() <= 0) {
            eAddress.setCustomer(eCustomer);
            eAddress.setCreateBy("SYSTEM");
            eAddress.setStatus(0);
            eAddress.setCreateTime(Timestamp.from(Instant.now()));
            eAddress.setIsDefault(false);
            eAddress.setFullName(oderRequest.getShipName());
            eAddress.setPhone(oderRequest.getShipPhone());
            adressRepository.save(eAddress);
        }
        order.setOrderDetail(null);
        order.setAddress(eAddress.getAddress());
        order.setCityName(eAddress.getCityName());
        order.setWardName(eAddress.getWardName());
        order.setDistrictName(eAddress.getDistrictName());
        order.setIdCity(eAddress.getIdCity());
        order.setIdDistrict(eAddress.getIdDistrict());
        order.setIdWard(eAddress.getIdWard());

        order.setCreateBy("Khách hàng");
        order.setCreateTime(Timestamp.from(Instant.now()));
        order.setUpdateTime(Timestamp.from(Instant.now()));
        order.setCustomer(eCustomer);
        order.setEmployee(null);
        order.setSaleForm(OrderContant.ONLINE);
        order.setStatus(OrderContant.STATUS_DEFAULT);
        order = oderRepository.save(order);
        List<OrderDetailResponseClient> oderDetail = oderDetailService.create(oderRequest.getLstProductDetail(),
                order.getId());
        Double gia = 0.0;
        for (OrderDetailResponseClient x : oderDetail) {
            if (x.getDiscount() > 0) {
                gia = gia + ((x.getUnitprice() - (x.getUnitprice() * x.getDiscount() / 100))
                        * x.getQuantity());
            } else {
                gia = gia + (x.getUnitprice() * x.getQuantity());
            }
        }
        if (order.getVoucher() != null) {
            if (order.getVoucher().getMin() == null || order.getVoucher().getMin() == 0
                    || order.getVoucher().getMin() <= gia) {// check min có null hoặc là
                if (order.getVoucher().isType() == true) {// giảm giá theo số tiền
                    gia = gia - order.getVoucher().getDiscount();
                } else {// giảm giá theo phần trăm
                    double soTienDuocGiam = gia * (order.getVoucher().getDiscount() / 100);
                    if (order.getVoucher().getMax() == null || order.getVoucher().getMax() == 0
                            || order.getVoucher().getMax() >= soTienDuocGiam) {
                        gia = gia - soTienDuocGiam;
                    } else {
                        gia = gia - order.getVoucher().getMax();
                    }
                }
            }
        }
        if (gia < 0) {
            gia = 0.0;
        }
        gia = gia + oderRequest.getFreight();
        order.setTotal(gia);
        order = oderRepository.save(order);
        order.setCode("HD" + GenCode.code(order.getId()));
        // order.setPaymentType("COD");
        order = oderRepository.save(order);
        Optional<CheckOut> checkOut = checkOutRepository.findByCode(oderRequest.getCodeCheckOut());
        if (checkOut == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Không tìm thấy checkOut:" + oderRequest.getCodeCheckOut());
        }
        checkOut.get().setCodeOder(order.getCode());
        checkOutRepository.save(checkOut.get());
        ObjectResult res = new ObjectResult();
        Map<String, Object> map = new HashMap<>();
        if (oderRequest.getPaymentType().equals("VNPAY")) {
            try {
                res.setMessage("ok");
                res.setStatus(true);
                map.put("link", vnpayCreate.create(order.getTotal(), order.getCode()));
                map.put("type", true);
                res.setResponseData(map);
                return res;
            } catch (UnsupportedEncodingException e) {
                res.setMessage("ok");
                res.setResponseData(null);
                res.setStatus(false);
                map.put("link", null);
                map.put("type", null);
                return res;
            }
        }
        OrderResponseClient oResponse = ObjectMapperUtils.map(order, OrderResponseClient.class);
        oResponse.setOrderDetail(oderDetail);
        res.setMessage("ok");
        res.setStatus(true);
        map.put("detail", oResponse);
        map.put("type", false);
        res.setResponseData(map);
        return res;
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
    public OrderResponseClient findByIdOrder(Integer id) {
        Order order = oderRepository.findById(id).get();
        OrderResponseClient response = ObjectMapperUtils.map(order, OrderResponseClient.class);
        for (OrderDetail x : order.getOrderDetail()) {
            for (OrderDetailResponseClient y : response.getOrderDetail()) {
                y.setProductDetailId(ObjectMapperUtils.map(x.getProductDetail(), ProductDetailResponse.class));
            }
        }
        return response;
    }

    @Override
    public List<OrderAllClient> findByIdClient() {
        Integer id = customerRepolsitory.findIdByEmail(EmailUser.getEmailUser());
        List<IOrderClient> order = oderRepository.getAllByCustomerId(id);
        List<OrderAllClient> lst = new ArrayList<>();
        for (IOrderClient x : order) {
            lst.add(mapOrderClient(x));
        }
        return lst;
    }

    public OrderAllClient mapOrderClient(IOrderClient o) {
        return OrderAllClient.builder().code(o.getCode())
                .status(o.getStatus()).createTime(o.getCreate_time()).address(o.getAddress())
                .cityName(o.getCity_name()).districtName(o.getDistrict_name()).wardName(o.getWard_name())
                .total(o.getTotal()).statusPay(o.getStatus_pay()).codeGhn(o.getCode_ghn()).build();
    }

    @Override
    public Boolean updateStatusPay(String code) {
        Optional<Order> order = oderRepository.findByCode(code);
        if (order == null || order.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy order :" + order);
        }
        order.get().setStatusPay(OrderContant.DA_THANH_TOAN);
        oderRepository.save(order.get());
        return true;
    }

    @Override
    public OrderResponseClient huyOrder(Integer id) {
        Optional<Order> order = oderRepository.findById(id);
        if (order == null || order.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Order");
        }
        if (order.get().getStatus() == OrderContant.STATUS_DEFAULT
                || order.get().getStatus() == OrderContant.STATUS_CHUAN_BI_HANG) {
            order.get().setStatus(OrderContant.STATUS_DA_HUY);
            oderRepository.save(order.get());
            return ObjectMapperUtils.map(order, OrderResponseClient.class);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Không được update");
    }

    @Override
    public OrderResponseClient findByCode(String code) {
        Optional<Order> order = oderRepository.findByCode(code);
        if (order == null || order.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Order");
        }
        String codeCheckOut = checkOutRepository.findByCodeOder(order.get().getCode());
        OrderResponseClient response = ObjectMapperUtils.map(order.get(), OrderResponseClient.class);
        response.setCodeCheckOut(codeCheckOut);
        Integer index = 0;
        for (OrderDetail x : order.get().getOrderDetail()) {
            response.getOrderDetail().get(index)
                    .setProductDetailId(ObjectMapperUtils.map(x.getProductDetail(), ProductDetailResponse.class));
            Optional<Integer> id = ratingRepository.findIdByOrderAndCustomAndProductId(order.get().getId(), order.get().getCustomer().getId(), x.getProductDetail().getProduct().getId());
            if (id.isPresent()) {
                response.getOrderDetail().get(index).setCheckRating(true);
            } else {
                response.getOrderDetail().get(index).setCheckRating(false);
            }
            index = index + 1;
        }
        Optional<String> codeReturn = returnRepository.getCodeByIdOrder(order.get().getId());
        if (codeReturn.isPresent()){
            response.setCodeReturn(codeReturn.get());
        }
        return response;
    }

    @Override
    public OrderClientInSucces findByCodeSucces(String code) {
        Integer id = customerRepolsitory.findIdByEmail(EmailUser.getEmailUser());
        Optional<Order> order = oderRepository.findByCodeInSucsses(code, id);
        if (order == null || order.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "không tìmm thấy order");
        }
        OrderClientInSucces orderClientInSucces = ObjectMapperUtils.map(order.get(), OrderClientInSucces.class);
        String checkOut = checkOutRepository.findByCodeOder(code);
        orderClientInSucces.setCodeCheckOut(checkOut);
        return orderClientInSucces;
    }

    @Override
    public OrderResponseClient updateOrderClient(OrderRequestClient oderRequest) {
        Optional<Order> order = oderRepository.findById(oderRequest.getId());
        if (order == null || order.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "dữ liệu không hợp lệ");
        }
        if (order.get().getStatus() != OrderContant.STATUS_DEFAULT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không có quyền sửa Order, Liên hệ cửa hàng!");
        }
        Integer id = customerRepolsitory.findIdByEmail(EmailUser.getEmailUser());
        String fullName = customerRepolsitory.findFullNameByEmail(EmailUser.getEmailUser());
        Customer customer = new Customer();
        customer.setId(id);
        order.get().setAddress(oderRequest.getAddress().getAddress());
        order.get().setCityName(oderRequest.getAddress().getCityName());
        order.get().setDistrictName(oderRequest.getAddress().getDistrictName());
        order.get().setFreight(oderRequest.getFreight());
        order.get().setIdCity(oderRequest.getAddress().getIdCity());
        order.get().setIdDistrict(oderRequest.getAddress().getIdDistrict());
        order.get().setIdWard(oderRequest.getAddress().getIdWard());
        order.get().setNote(oderRequest.getNote());
        order.get().setShipName(oderRequest.getShipName());
        order.get().setShipPhone(oderRequest.getShipPhone());
        order.get().setUpdateTime(Timestamp.from(Instant.now()));
        order.get().setUpdateBy(fullName);
        order.get().setWardName(oderRequest.getAddress().getWardName());
        Address eAddress = ObjectMapperUtils.map(oderRequest.getAddress(), Address.class);
        if (eAddress.getId() == null || eAddress.getId() <= 0) {
            eAddress.setCustomer(customer);
            eAddress.setCreateBy("SYSTEM");
            eAddress.setStatus(0);
            eAddress.setCreateTime(Timestamp.from(Instant.now()));
            eAddress.setIsDefault(false);
            eAddress.setFullName(oderRequest.getShipName());
            eAddress.setPhone(oderRequest.getShipPhone());
            adressRepository.save(eAddress);
        }
        List<OrderDetail> orderDetailListUpdate = new ArrayList<>();
        List<OrderDetail> lstOrderDetailDelete = new ArrayList<>();
        List<ProductDetailOderRequet> lstOrderDetailCreate = new ArrayList<>();
        Integer indexRequets = 0;
        for (OrderDetail x : order.get().getOrderDetail()) {
            indexRequets = 0;
            for (ProductDetailOderRequet y : oderRequest.getLstProductDetail()) {
                if (Objects.equals(x.getProductDetail().getId(), y.getId())) {
                    if (!(x.getQuantity().intValue() == y.getQuantity().intValue())) {
                        if (x.getQuantity().intValue() < y.getQuantity().intValue()) {
                            int quantityUpdate = y.getQuantity() - x.getQuantity();
                            productDetailRepository.updateTruQuantity(quantityUpdate,
                                    x.getProductDetail().getId());
                        } else {
                            Integer quantityUpdate = x.getQuantity() - y.getQuantity();
                            productDetailRepository.updateCongQuantity(quantityUpdate, x.getProductDetail().getId());
                        } // ngược lại : ra được số sản phẩm bớt đi sẽ được cộng vào db
                        x.setQuantity(y.getQuantity());
                        orderDetailListUpdate.add(x);
                    } // nếu id bằng nhau mà khác số lương => update số lượng
                    break;// out fore
                } // check xem có id productdetail nào bằng nhau không;
                indexRequets = indexRequets + 1;
                if (indexRequets == oderRequest.getLstProductDetail().size()) {
                    lstOrderDetailDelete.add(x);
                    productDetailRepository.updateCongQuantity(x.getQuantity(),
                            x.getProductDetail().getId());
                }
            }
        }
        for (ProductDetailOderRequet x : oderRequest.getLstProductDetail()) {
            indexRequets = 0;
            for (OrderDetail y : order.get().getOrderDetail()) {
                if (Objects.equals(x.getId(), y.getProductDetail().getId())) {
                    break;
                }
                indexRequets = indexRequets + 1;
                if (indexRequets == order.get().getOrderDetail().size()) {
                    lstOrderDetailCreate.add(x);
                }
            }
        }
        Double gia = 0.0;
        List<OrderDetailResponseClient> lstNew = new ArrayList<>();
        if (!lstOrderDetailCreate.isEmpty()) {
            lstNew = oderDetailService.create(lstOrderDetailCreate, order.get().getId());
            for (OrderDetailResponseClient x : lstNew) {
                if (x.getDiscount() > 0) {
                    gia = gia + ((x.getUnitprice() - (x.getUnitprice() * x.getDiscount() / 100))
                            * x.getQuantity());
                } else {
                    gia = gia + (x.getUnitprice() * x.getQuantity());
                }
            }
        }
        if (!lstOrderDetailDelete.isEmpty()) {
            for (OrderDetail x : lstOrderDetailDelete) {
                order.get().getOrderDetail().remove(x);
            }
            orderDetailRepositoty.deleteAll(lstOrderDetailDelete);
        }
        for (OrderDetail x : order.get().getOrderDetail()) {
            if (x.getDisCount() > 0) {
                gia = gia + ((x.getUnitprice() - (x.getUnitprice() * x.getDisCount() / 100))
                        * x.getQuantity());
            } else {
                gia = gia + (x.getUnitprice() * x.getQuantity());
            }
        }
        if (oderRequest.getVoucher() <= 0 || oderRequest.getVoucher() == null) {
            order.get().setVoucher(null);
        } else {
            Optional<Voucher> optionalVoucher = voucherRepository.findById(oderRequest.getVoucher());
            if (optionalVoucher == null) {
                order.get().setVoucher(null);
            } else {
                order.get().setVoucher(optionalVoucher.get());
            }
        } // check voucher
        if (order.get().getVoucher() != null) {
            if (order.get().getVoucher().getMin() == null || order.get().getVoucher().getMin() == 0
                    || order.get().getVoucher().getMin() <= gia) {// check min có null hoặc là
                if (order.get().getVoucher().isType() == true) {// giảm giá theo số tiền
                    gia = gia - order.get().getVoucher().getDiscount();
                } else {// giảm giá theo phần trăm
                    double soTienDuocGiam = gia * (order.get().getVoucher().getDiscount() / 100);
                    if (order.get().getVoucher().getMax() == null || order.get().getVoucher().getMax() == 0
                            || order.get().getVoucher().getMax() >= soTienDuocGiam) {
                        gia = gia - soTienDuocGiam;
                    } else {
                        gia = gia - order.get().getVoucher().getMax();
                    }
                }
            }
        }
        if (gia < 0) {
            gia = 0.0;
        }
        gia = gia + oderRequest.getFreight();
        order.get().setTotal(gia);
        Order o = oderRepository.save(order.get());
        return ObjectMapperUtils.map(o, OrderResponseClient.class);
    }

}
