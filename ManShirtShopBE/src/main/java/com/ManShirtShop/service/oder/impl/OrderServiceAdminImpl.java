package com.ManShirtShop.service.oder.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import javax.transaction.Transactional;

import com.ManShirtShop.dto.order_the_store.*;
import com.ManShirtShop.service.email.SendEmailService;
import org.checkerframework.checker.units.qual.g;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ManShirtShop.Authentication.dto.user.EmailUser;
import com.ManShirtShop.common.contans.OrderContant;
import com.ManShirtShop.common.genCode.GenCode;
import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;
import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClient;
import com.ManShirtShop.dto.client.oderDto.OrderRequestClient;
import com.ManShirtShop.dto.client.oderDto.OrderResponseClient;
import com.ManShirtShop.dto.product.ProductReponse;
import com.ManShirtShop.dto.product_Image_dto.ProductImageResponse;
import com.ManShirtShop.entities.Customer;
import com.ManShirtShop.entities.Employee;
import com.ManShirtShop.entities.Order;
import com.ManShirtShop.entities.OrderDetail;
import com.ManShirtShop.entities.Product;
import com.ManShirtShop.entities.ProductDetail;
import com.ManShirtShop.entities.ProductDiscount;
import com.ManShirtShop.entities.Voucher;
import com.ManShirtShop.repository.CustomerRepolsitory;
import com.ManShirtShop.repository.EmployeeRepository;
import com.ManShirtShop.repository.OderRepository;
import com.ManShirtShop.repository.OrderDetailRepositoty;
import com.ManShirtShop.repository.ProductDetailRepository;
import com.ManShirtShop.repository.ProductDiscountRepository;
import com.ManShirtShop.repository.ProductRepository;
import com.ManShirtShop.repository.VoucherRepository;
import com.ManShirtShop.rest.giao_hang_nhanh.GhnService;
import com.ManShirtShop.rest.giao_hang_nhanh.GhnServiceImpl;
import com.ManShirtShop.service.client.oder.OderClientService;
import com.ManShirtShop.service.oder.OrderServiceAdmin;
import com.ManShirtShop.service.oderDetail.OderDetailService;
import com.ManShirtShop.service.oderDetail.impl.OderdetailServiceImpl;

@Service
@Transactional
public class OrderServiceAdminImpl implements OrderServiceAdmin {
    private static final Logger LOGGER = LoggerFactory.getLogger(OderdetailServiceImpl.class);
    @Autowired
    OderRepository oderRepository;

    @Autowired
    OrderDetailRepositoty orderDetailRepositoty;

    @Autowired
    CustomerRepolsitory customerRepolsitory;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    OderDetailService oderDetailService;

    @Autowired
    CustomerRepolsitory customerRepo;

    @Autowired
    ProductDiscountRepository productDiscountRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    GhnServiceImpl ghnService;

    @Autowired
    SendEmailService sendEmail;

    @Override
    @Transactional
    public OrderResponeAdmin create(OrderRequestAdmin oderRequest) {
        if (oderRequest.getStatus() == 0 || oderRequest.getStatus() == 1 || oderRequest.getStatus() == 2
                || oderRequest.getStatus() == 3 || oderRequest.getStatus() == 4 || oderRequest.getStatus() == 5) {
            LOGGER.info(OrderServiceAdminImpl.class + "Status update");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trạng thái hoá đơn không đúng", null);
        }
        oderRequest.setId(-1);
        if (checkId(oderRequest.getCustomer())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Custommer", null);
        }
        Optional<Customer> optionalCustomer = customerRepo.findById(oderRequest.getCustomer());
        if (optionalCustomer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Custommer", null);
        }
        Order order = ObjectMapperUtils.map(oderRequest, Order.class);
        if (checkId(oderRequest.getVoucher())) {
            order.setVoucher(null);
        } else {
            Optional<Voucher> optionalVoucher = voucherRepository.findById(oderRequest.getVoucher());
            if (optionalVoucher == null) {
                order.setVoucher(null);
            } else {
                order.setVoucher(optionalVoucher.get());
            }
        } // check voucher
        Optional<Employee> employee = employeeRepository.findByEmail(EmailUser.getEmailUser());
        order.setOrderDetail(null);
        order.setCreateBy(employee.get().getFullname());
        order.setCreateTime(Timestamp.from(Instant.now()));
        order.setUpdateTime(Timestamp.from(Instant.now()));
        order.setCustomer(optionalCustomer.get());
        order.setEmployee(employee.get());
        order = oderRepository.save(order);
        // order = oderRepository.findById(order.getId()).get();
        List<OderDetailResponseAdmin> oderDetail = oderDetailService.create(oderRequest.getLstProductDetail(),
                order.getId());
        Double gia = 0.0;
        for (OderDetailResponseAdmin x : oderDetail) {
            // Optional<ProductDiscount> productDisOptional = productDiscountRepository
            // .getByProductI2dAndStatus(x.getProductDetailId().getProduct().getId());
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
        order = oderRepository.save(order);
        OrderResponeAdmin oResponse = ObjectMapperUtils.map(order, OrderResponeAdmin.class);
        oResponse.setOrderDetail(oderDetail);
        return oResponse;
    }

    @Override
    public OrderResponeAdmin findById(Integer id) {
        Optional<Order> order = oderRepository.findById(id);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Order", null);
        }
        OrderResponeAdmin response = ObjectMapperUtils.map(order, OrderResponeAdmin.class);
        return response;
    }

    @Override
    public List<OrderAllAdminByStatus> findByStatus(Integer status) {
        // List<IOrderAllAdmin> order = oderRepository.getAllByAdmin(status);
        List<OrderAllAdminByStatus> response = new ArrayList<>();
        for (IOrderAllAdmin x : oderRepository.getAllByAdmin(status)) {
            OrderAllAdminByStatus y = ObjectMapperUtils.map(x, OrderAllAdminByStatus.class);
            y.setFullname(x.getFullname());
            y.setSaleForm(x.getSale_form());
            y.setCodeGhn(x.getCode_ghn());
            response.add(y);
        }
        return response;
    }

    @Override
    public OrderResponeAdmin findByCode(String code) {
        Optional<Order> order = oderRepository.findByCode(code);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Order", null);
        }
        OrderResponeAdmin response = ObjectMapperUtils.map(order, OrderResponeAdmin.class);
        Integer index = 0;
        for (OrderDetail x : order.get().getOrderDetail()) {
            response.getOrderDetail().get(index)
                    .setProductDetailId(ObjectMapperUtils.map(x.getProductDetail(), ProductDetailResponse.class));
            index = index + 1;
        }
        return response;
    }

    @Override
    public List<OrderResponeAdmin> updateStatus(List<Integer> lstId, Integer status) {
//        List<Order> listOrder = new ArrayList<>();
//        if (lstId == null || lstId.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Order", null);
//        }
//        if (status == 0 || status == 2 || status == 1
//                || status == 3 || status == 4 || status == 5) {
//            LOGGER.info(OrderServiceAdminImpl.class + "Status update");
//        } else {
//            LOGGER.info(OrderServiceAdminImpl.class + "Status sai");
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trạng thái hoá đơn không đúng", null);
//        }
//        for (Integer id : lstId) {
//            if (id <= 0) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Order", null);
//            }
//            Optional<Order> orderDB = oderRepository.findById(id);
//            if (orderDB == null) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Order", null);
//            }
//            Order order = orderDB.get();
//            order.setStatus(status);
//            order.setUpdateBy("admin");
//            order.setUpdateTime(Timestamp.from(Instant.now()));
//            listOrder.add(order);
//        }
//        listOrder = oderRepository.saveAll(listOrder);
        return null;
    }

    @Override
    public List<OrderResponeAdmin> findALl() {
        List<Order> getAll = oderRepository.getAllByStatus();
        List<OrderResponeAdmin> lst = ObjectMapperUtils.mapAll(getAll, OrderResponeAdmin.class);
        return null;
    }

    public Boolean checkId(Integer id) {
        if (id <= 0 || id == null) {
            return true;
        }
        return false;
    }

    public Boolean checkString(String check) {
        if (check.isBlank() || check == null) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public List<OrderResponeAdmin> updateStatusChuanBiHang(List<Integer> lstId) {
        try {
            Optional<Employee> employee = employeeRepository.findByEmail(EmailUser.getEmailUser());
            List<Order> lst = oderRepository.findAllById(lstId);
            for (Order x : lst) {
                x.setStatus(OrderContant.STATUS_CHUAN_BI_HANG);
                x.setUpdateTime(Timestamp.from(Instant.now()));
                x.setEmployee(employee.get());
                x.setUpdateBy(employee.get().getFullname());
                List<ProductDetail> lDetails = new ArrayList<>();
                for (OrderDetail pDetail : x.getOrderDetail()) {
                    ProductDetail p = productDetailRepository.findById(pDetail.getProductDetail().getId()).get();
                    p.setQuantity(p.getQuantity() - pDetail.getQuantity());
                    lDetails.add(p);
                }
                productDetailRepository.saveAll(lDetails);
                if (x.getCustomer().getEmail() != null || !x.getCustomer().getEmail().isBlank()) {
                    sendEmail.start(x.getCustomer().getEmail(),"Đơn hàng đã được xác nhận và đơn hàng của bạn đang được chuẩn bị!");
                }
            }
            lst = oderRepository.saveAll(lst);

            return ObjectMapperUtils.mapAll(lst, OrderResponeAdmin.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dữ liệu không hợp lệ");
        }
    }

    @Override
    @Transactional
    public List<OrderResponeAdmin> updateStatusThanhCong(List<Integer> lstId) {
        try {
            List<Order> lst = oderRepository.findAllById(lstId);
            for (Order x : lst) {
                x.setStatus(OrderContant.STATUS_THANH_CONG);
                x.setStatusPay(OrderContant.DA_THANH_TOAN);
                x.setUpdateTime(Timestamp.from(Instant.now()));
                String fullName = employeeRepository.getFullNameByEmail(EmailUser.getEmailUser());
                x.setUpdateBy(fullName);
                if (x.getCustomer().getEmail() != null || !x.getCustomer().getEmail().isBlank()) {
                    List<String> lstEmail = new ArrayList<>();
                    sendEmail.start(x.getCustomer().getEmail(),"Đơn hàng của bạn đã được giao thành công.!");
                }
            }
            lst = oderRepository.saveAll(lst);
            return ObjectMapperUtils.mapAll(lst, OrderResponeAdmin.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dữ liệu không hợp lệ");
        }
    }

    @Override
    @Transactional
    public Map<String, Object> updateStatusGiaoHang(Integer lstId) {
        // try {
        Map map = new HashMap<>();
        String a = ghnService.ghncreate(lstId);
        map.put("message", a);
        return map;
    }

    @Override
    public Map<String, Object> updateStatusHuy(Integer idOrder) {
        try {
            Map map = new HashMap<>();
            Optional<Order> order = oderRepository.findById(idOrder);
            order.get().setStatus(OrderContant.STATUS_DA_HUY);
            order.get().setUpdateTime(Timestamp.from(Instant.now()));
            String fullName = employeeRepository.getFullNameByEmail(EmailUser.getEmailUser());
            order.get().setUpdateBy(fullName);
            oderRepository.save(order.get());
            oderDetailService.updateQuantityProductDetail(order.get().getOrderDetail());
            if (order.get().getCustomer().getEmail() != null || !order.get().getCustomer().getEmail().isBlank()) {
                sendEmail.start(order.get().getCustomer().getEmail(),"Đơn hàng của bạn đã bị Huỷ!");
            }
            map.put("message", "Thành công");
            return map;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dữ liệu không hợp lệ");
        }
    }

    @Override
    public Map<String, Object> updateStatusGiaoThatBai(Integer idOrder) {
        try {
            Map map = new HashMap<>();
            Optional<Order> order = oderRepository.findById(idOrder);
            order.get().setStatus(OrderContant.STATUS_GIAO_THAT_BAI);
            order.get().setUpdateTime(Timestamp.from(Instant.now()));
            String fullName = employeeRepository.getFullNameByEmail(EmailUser.getEmailUser());
            order.get().setUpdateBy(fullName);
            oderDetailService.updateQuantityProductDetail(order.get().getOrderDetail());
            oderRepository.save(order.get());
            if (order.get().getCustomer().getEmail() != null || !order.get().getCustomer().getEmail().isBlank()) {
                sendEmail.start(order.get().getCustomer().getEmail(),"Đơn hàng của bạn đã giao thất bại!");
            }
            map.put("message", "Thành công");
            return map;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dữ liệu không hợp lệ");
        }
    }

    @Override
    @Transactional
    public OrderResponeAdmin updateOrder(OrderRequestAdmin request) {
        return updateOrder2(request);
    }

    @Override
    public SumAllOrder sumAllOrder() {
        SumAllOrder sumAllOrder = new SumAllOrder();
        sumAllOrder.setChoDuyet(oderRepository.sumAllOrderByStatus(0));
        sumAllOrder.setChuanBiHang(oderRepository.sumAllOrderByStatus(1));
        sumAllOrder.setDangGiao(oderRepository.sumAllOrderByStatus(2));
        sumAllOrder.setThanhCong(oderRepository.sumAllOrderByStatus(3));
        sumAllOrder.setDaHuy(oderRepository.sumAllOrderByStatus(4));
        sumAllOrder.setThatBai(oderRepository.sumAllOrderByStatus(5));
        return sumAllOrder;
    }

    @Transactional
    public OrderResponeAdmin updateOrder2(OrderRequestAdmin request) {
        Optional<Order> order = oderRepository.findById(request.getId());
        if (order == null || order.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "dữ liệu không hợp lệ");
        }
        if (order.get().getStatus() != OrderContant.STATUS_CHUAN_BI_HANG) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trạng thái order cho biết Order không còn được sửa nữa!");
        }
        order.get().setAddress(request.getAddress());
        order.get().setCityName(request.getCityName());
        order.get().setDistrictName(request.getDistrictName());
        order.get().setFreight(request.getFreight());
        order.get().setIdCity(request.getIdCity());
        order.get().setIdDistrict(request.getIdDistrict());
        order.get().setIdWard(request.getIdWard());
        order.get().setNote(request.getNote());
        order.get().setShipName(request.getShipName());
        order.get().setShipPhone(request.getShipPhone());
        order.get().setUpdateTime(Timestamp.from(Instant.now()));
        String fullName = employeeRepository.getFullNameByEmail(EmailUser.getEmailUser());
        order.get().setUpdateBy(fullName);
        order.get().setWardName(request.getWardName());
        List<OrderDetail> orderDetailListUpdate = new ArrayList<>();
        List<OrderDetail> lstOrderDetailDelete = new ArrayList<>();
        List<ProductDetailOderRequetAdmin> lstOrderDetailCreate = new ArrayList<>();
        Integer indexRequets = 0;
        for (OrderDetail x : order.get().getOrderDetail()) {
            indexRequets = 0;
            for (ProductDetailOderRequetAdmin y : request.getLstProductDetail()) {
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
                if (indexRequets == request.getLstProductDetail().size()) {
                    lstOrderDetailDelete.add(x);
                    productDetailRepository.updateCongQuantity(x.getQuantity(),
                            x.getProductDetail().getId());
                }
            }
        }
        for (ProductDetailOderRequetAdmin x : request.getLstProductDetail()) {
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
        List<OderDetailResponseAdmin> lstNew = new ArrayList<>();
        if (!lstOrderDetailCreate.isEmpty()) {
            for (ProductDetailOderRequetAdmin x : lstOrderDetailCreate) {

            }
            lstNew = oderDetailService.create(lstOrderDetailCreate, order.get().getId());
            for (OderDetailResponseAdmin x : lstNew) {
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
        if (checkId(request.getVoucher())) {
            order.get().setVoucher(null);
        } else {
            Optional<Voucher> optionalVoucher = voucherRepository.findById(request.getVoucher());
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
        gia = gia + request.getFreight();
        order.get().setTotal(gia);
        Order o = oderRepository.save(order.get());
        return ObjectMapperUtils.map(o, OrderResponeAdmin.class);
    }
}
