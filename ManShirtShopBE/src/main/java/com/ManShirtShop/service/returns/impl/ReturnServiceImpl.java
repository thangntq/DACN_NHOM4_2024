package com.ManShirtShop.service.returns.impl;

import com.ManShirtShop.common.contans.ReturnsContact;
import com.ManShirtShop.common.genCode.GenCode;
import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponseGet;
import com.ManShirtShop.dto.order_the_store.OderDetailResponseAdmin;
import com.ManShirtShop.dto.order_the_store.OderDetailResponseAdminGet;
import com.ManShirtShop.dto.order_the_store.OrderDetailResponseAdminTemp;
import com.ManShirtShop.dto.product.ProductReponse;
import com.ManShirtShop.dto.product.ProductReponseGetAll;
import com.ManShirtShop.dto.returnDetail.ReturnDetailAdminUpdateRequest;
import com.ManShirtShop.dto.returnDetail.ReturnDetailResponseAdmin;
import com.ManShirtShop.dto.returnDetail.ReturnDetailAdminAddRequest;
import com.ManShirtShop.dto.returnDetail.ReturnDetailResponseAdminGet;
import com.ManShirtShop.dto.returns.*;
import com.ManShirtShop.entities.*;
import com.ManShirtShop.repository.*;
import com.ManShirtShop.service.returns.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReturnServiceImpl implements ReturnService {
    // private final ReturnDetailRepository returnDetailRepository;
    // private OrderDetailRepositoty orderDetailRepositoty;
    // private ReturnRepository returnRepository;

    // private OderRepository orderRepository;

    // @Autowired
    // public ReturnServiceImpl(OrderDetailRepositoty orderDetailRepositoty, ReturnRepository returnRepository, ReturnDetailRepository returnDetailRepository) {
    //     this.orderDetailRepositoty = orderDetailRepositoty;
    //     this.returnRepository = returnRepository;
    //     this.returnDetailRepository = returnDetailRepository;
    // }
    @Autowired
    ReturnDetailRepository returnDetailRepository;

    @Autowired
    OrderDetailRepositoty orderDetailRepositoty;

    @Autowired
    ReturnRepository returnRepository;

    @Autowired
    OderRepository orderRepository;

    @Autowired
    ProductDiscountRepository productDiscountRepository;

    public Boolean checkIb(Integer id) {
        if (id == 0 || id == null) {
            return false;
        }
        if (!returnRepository.existsById(id)) { // check id db
            return false;
        }
        return true;
    }

    @Override
    public ReturnResponseAdmin addReturns(ReturnRequestAdmin returnRequestAdmin) {
        // Check nếu Order_Id truyền vào có Return_ID thành công rồi thì sẽ không cho thêm nữa !
        Integer status1 = returnRepository.checkIfReturnExistsByStatusAndOrderId(returnRequestAdmin.getOrderId());
        Integer status2 = orderRepository.checkIfOrderUpdatedMoreThan7DaysAgo(returnRequestAdmin.getOrderId());
        System.out.println(status2);
        System.out.println(status1);
        if (status2 < 0 || status2 > 7 || status1 == 1) {
            throw new IllegalArgumentException("Order with id " + returnRequestAdmin.getOrderId() + " has already been processed.");
        }

        //    Check nếu trang thái thành công của UpdateTime Order > 7 thì không cho tạo đơn
        //    if (orderRepository.checkIfOrderUpdatedMoreThan7DaysAgo(returnRequestAdmin.getOrderId()) == false){
        //        throw new IllegalArgumentException("Order with id " + returnRequestAdmin.getOrderId() + " has already been processed.");
        //    }

        returnRequestAdmin.setId(null);
        Return aReturn = ObjectMapperUtils.map(returnRequestAdmin, Return.class);
        aReturn.setCreateBy("Nhân viên");
        aReturn.setCreateTime(Timestamp.from(Instant.now()));
        aReturn.setStatus(ReturnsContact.STATUS_DEFAULT); // Mặc định status yêu cầu trả hàng
        aReturn.setReturnDetail(null);
        aReturn = returnRepository.save(aReturn);
        aReturn.setCode("HDT" + GenCode.code(aReturn.getId()));
        aReturn = returnRepository.save(aReturn);
        double totalReturn = 0.0;
        boolean hasError = false; // Biến để kiểm tra nếu có lỗi xảy ra
        try {
            List<OrderDetailResponseAdminTemp> orderDetails = returnRequestAdmin.getOrderDetailResponseAdminTemps();
            if (orderDetails != null && !orderDetails.isEmpty()) {
                // List<OrderDetail> orderDetailSaveOne = new ArrayList<>();
                List<ReturnDetail> returnDetails = new ArrayList<>();
                for (OrderDetailResponseAdminTemp x : orderDetails) {
                    Optional<OrderDetail> orderDetail1 = orderDetailRepositoty.findById(x.getId());
                    int returnQuantity = x.getReturnQuantity();// Số lượng tr truyền vào
                    int availableQuantity = orderDetail1.get().getQuantity();// Số lượng sản phẩm trong DB

                    if (returnQuantity <= 0 || returnQuantity > availableQuantity) {
                        hasError = true;
                        break; // Thoát khỏi vòng lặp ngay nếu có lỗi
                    }
                    totalReturn += (returnQuantity * orderDetail1.get().getUnitprice()) - (returnQuantity * orderDetail1.get().getUnitprice()*orderDetail1.get().getDisCount()/100);

                    ReturnDetail returnDetail = new ReturnDetail();
                    returnDetail.setCreateBy("Nhân viên");
                    returnDetail.setCreateTime(Timestamp.from(Instant.now()));
                    returnDetail.setStatus(0);
                    returnDetail.setQuantity(returnQuantity);
                    returnDetail.setOrderDetail(orderDetail1.get());
                    returnDetail.setReturns(aReturn);
                    returnDetails.add(returnDetail);
                    // returnDetail = returnDetailRepository.save(returnDetail);
                }
                returnDetails = returnDetailRepository.saveAll(returnDetails);
                // List<OrderDetail> savedOrderDetailsAll = orderDetailRepositoty.saveAll(orderDetailSaveOne);
            } else {
                hasError = true;
            }
            if (hasError) {
                throw new IllegalArgumentException("Invalid return request. Please check return quantity or return_id for order details.");
            }
            aReturn.setTotal(totalReturn);
            aReturn = returnRepository.save(aReturn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Prepare the ReturnResponseClient object to return
        ReturnResponseAdmin returnResponseAdmin = ObjectMapperUtils.map(aReturn, ReturnResponseAdmin.class);
        return null;
    }

    @Override
    public ReturnResponseAdmin updateSuccess(Integer returnId) {
        Return aReturn = returnRepository.findById(returnId).orElseThrow(() -> new IllegalArgumentException("Return with id " + returnId + " not found."));

        if ((aReturn.getStatus() == ReturnsContact.STATUS_THANH_CONG) || (aReturn.getStatus() == ReturnsContact.STATUS_DA_HUY) || (aReturn.getStatus() == ReturnsContact.STATUS_TAO_DON_LOI)) {
            throw new IllegalArgumentException("Return with id " + returnId + " has already been processed.");
        }
        Integer statusResult = returnDetailRepository.checkStatusForReturnId(returnId);
        if (statusResult == 1) {
            throw new IllegalArgumentException("Không thể cập nhật trạng thái thành công cho Return vì không tất cả các ReturnDetail có status = 1.");
        }

        // Cập nhật trạng thái của return thành STATUS_THANH_CONG
        aReturn.setStatus(ReturnsContact.STATUS_THANH_CONG);
        aReturn.setUpdateBy("NewMemStore Nhân viên");
        aReturn.setUpdateTime(Timestamp.from(Instant.now()));

        Double totalReturn = returnDetailRepository.calculateTotalReturnByReturnId(returnId);
        if (totalReturn == null) {
            totalReturn = 0.0;
        }
        aReturn.setTotal(totalReturn);
        returnRepository.save(aReturn);

        // Prepare the updated ReturnResponseClient object to return
        ReturnResponseAdmin returnResponseAdmin = ObjectMapperUtils.map(aReturn, ReturnResponseAdmin.class);
        return returnResponseAdmin;
    }

    @Override
    public ReturnResponseAdmin updateRefuse(Integer returnId) {


        Return aReturn = returnRepository.findById(returnId).orElseThrow(() -> new IllegalArgumentException("Return with id " + returnId + " not found."));

        if ((aReturn.getStatus() == ReturnsContact.STATUS_DANG_GIAO) || (aReturn.getStatus() == ReturnsContact.STATUS_THANH_CONG) || (aReturn.getStatus() == ReturnsContact.STATUS_DA_HUY) || (aReturn.getStatus() == ReturnsContact.STATUS_TAO_DON_LOI)) {
            throw new IllegalArgumentException("Return with id " + returnId + " has already been processed.");
        }

        // Cập nhật trạng thái của return thành STATUS_DA_HUY
        aReturn.setStatus(ReturnsContact.STATUS_TAO_DON_LOI);
        aReturn.setUpdateBy("NewMemStore Nhân Viên");
        aReturn.setUpdateTime(Timestamp.from(Instant.now()));
        returnRepository.save(aReturn);
        try {
            List<ReturnDetail> returnDetails = returnDetailRepository.findByReturnId(returnId);
            if (returnDetails != null && !returnDetails.isEmpty()) {
                for (ReturnDetail x : returnDetails) {
                    x.setUpdateBy("NewMemStore Nhân viên");
                    x.setUpdateTime(Timestamp.from(Instant.now()));
                }
                returnDetailRepository.saveAll(returnDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Prepare the updated ReturnResponseClient object to return
        ReturnResponseAdmin returnResponseAdmin = ObjectMapperUtils.map(aReturn, ReturnResponseAdmin.class);
        return returnResponseAdmin;
    }

//    @Override
//    public List<ReturnDetailResponseAdmin> findAllReturnDetailsByStatusOfReturn(Integer returnId, Integer status) {
//        List<ReturnDetail> returnDetails;
//        returnDetails = returnDetailRepository.findReturnDetailsByReturnIdAndStatus(returnId, status);
//        List<ReturnDetailResponseAdmin> returnDetailResponsAdmins = new ArrayList<>();
//        for (ReturnDetail x : returnDetails) {
//            ReturnDetailResponseAdmin returnDetailResponseAdmin = ObjectMapperUtils.map(x, ReturnDetailResponseAdmin.class);
//            returnDetailResponseAdmin.setReturnResponseAdmin(ObjectMapperUtils.map(x.getReturns(), ReturnResponseAdmin.class));
//            returnDetailResponseAdmin.setOderDetailResponseAdmin(ObjectMapperUtils.map(x.getOrderDetail(), OderDetailResponseAdmin.class));
//            returnDetailResponsAdmins.add(returnDetailResponseAdmin);
//        }
//        return returnDetailResponsAdmins;
//    }

    @Override
    public ReturnDetailResponseAdmin updateReturnsDeatail(ReturnDetailAdminUpdateRequest returnDetailAdminUpdateRequest) {

        Integer returnDetailId = returnDetailAdminUpdateRequest.getReturnDetailId();
        Integer quantity = returnDetailAdminUpdateRequest.getQuantity();

        // Bước 1: Tìm kiếm đối tượng ReturnDetail dựa trên returnDetailId
        ReturnDetail returnDetail = returnDetailRepository.findById(returnDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ReturnDetail có id " + returnDetailId));

        // Bước 2: Kiểm tra xem quantity trong ReturnDetailAdminUpdateRequest có hợp lệ hay không
        int availableQuantity = returnDetailRepository.getQuantityByReturnDetailId(returnDetailId);
        if (quantity > availableQuantity) {
            throw new IllegalArgumentException("Yêu cầu cập nhật không hợp lệ. Số lượng cập nhật vượt quá số lượng có sẵn cho chi tiết đơn hàng.");
        }

        returnDetail.setQuantity(quantity);
        returnDetail.setStatus(1);
        returnDetail.setUpdateBy("NewMenStore Nhân viên");
        returnDetail.setUpdateTime(Timestamp.from(Instant.now()));
        returnDetailRepository.save(returnDetail);

        Integer returnId = returnDetail.getReturns().getId();
        Double totalReturn = returnDetailRepository.calculateTotalReturnByReturnId(returnId);
        if (totalReturn == null) {
            totalReturn = 0.0;
        }

        Return aReturn = returnRepository.findById(returnId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Return có id " + returnId));
        aReturn.setTotal(totalReturn);
        returnRepository.save(aReturn);

        ReturnDetailResponseAdmin responseAdmin = ObjectMapperUtils.map(returnDetail, ReturnDetailResponseAdmin.class);
        return responseAdmin;
    }

    @Override
    public ReturnDetailResponseAdmin addReturnsDetail(ReturnDetailAdminAddRequest returnDetailAdminAddRequest) {
        Integer status2 = orderRepository.checkIfOrderUpdatedMoreThan7DaysAgo(returnDetailAdminAddRequest.getOrderId());
        if (status2 < 0 || status2 > 7) {
            throw new IllegalArgumentException("Order with id " + returnDetailAdminAddRequest.getOrderId() + " has already been processed.");
        }

        Integer returnId = returnDetailAdminAddRequest.getReturnId();
        Integer orderId = returnDetailAdminAddRequest.getOrderId();

        // Kiểm tra nếu ReturnId truyền vào có Return thành công rồi thì sẽ không cho thêm nữa
        Return aReturn = returnRepository.findById(returnId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng trả hàng có id " + returnId));
        if (aReturn.getStatus() == ReturnsContact.STATUS_THANH_CONG) {
            throw new IllegalArgumentException("Đơn hàng trả hàng có id " + returnId + " đã được xử lý thành công.");
        }

        // Kiểm tra số lượng ReturnDetail theo từng OrderDetail
        List<OrderDetailResponseAdminTemp> orderDetailResponseAdminTemps = returnDetailAdminAddRequest.getOrderDetailResponseAdminTemps();
        if (orderDetailResponseAdminTemps == null || orderDetailResponseAdminTemps.isEmpty()) {
            throw new IllegalArgumentException("Yêu cầu trả hàng không hợp lệ. Thiếu thông tin chi tiết đơn hàng.");
        }

        // Tạo mới danh sách ReturnDetail
        List<ReturnDetail> returnDetails = new ArrayList<>();

        for (OrderDetailResponseAdminTemp temp : orderDetailResponseAdminTemps) {
            Integer orderDetailId = temp.getId();
            int returnQuantity = temp.getReturnQuantity();

            // Kiểm tra số lượng OrderDetail trong DB
            int availableQuantity = orderDetailRepositoty.findById(orderDetailId).map(OrderDetail::getQuantity).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết đơn hàng có id " + orderDetailId));

            // Lấy số lượng đã trả hàng từ các bản ghi ReturnDetail cũ
            int totalReturnedQuantity = returnDetailRepository.findTotalReturnedQuantityByOrderDetailId(orderDetailId);

            int remainingQuantity = availableQuantity - totalReturnedQuantity;

            // Kiểm tra xem số lượng trả hàng mới cần thêm vào có vượt quá số lượng còn lại không
            if (returnQuantity > remainingQuantity) {
                throw new IllegalArgumentException("Yêu cầu trả hàng không hợp lệ. Số lượng trả hàng vượt quá số lượng có sẵn cho chi tiết đơn hàng có id " + orderDetailId);
            }

            // Thêm mới ReturnDetail
            OrderDetail orderDetail = orderDetailRepositoty.findById(orderDetailId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết đơn hàng có id " + orderDetailId));

            ReturnDetail newReturnDetail = new ReturnDetail();
            newReturnDetail.setCreateBy("Nhân viên");
            newReturnDetail.setCreateTime(Timestamp.from(Instant.now()));
            newReturnDetail.setStatus(ReturnsContact.STATUS_DEFAULT);
            newReturnDetail.setQuantity(returnQuantity);
            newReturnDetail.setOrderDetail(orderDetail);
            newReturnDetail.setReturns(aReturn);
            returnDetails.add(newReturnDetail);
        }

        // Lưu danh sách ReturnDetail vào database
        returnDetailRepository.saveAll(returnDetails);

        // Cập nhật lại tổng tiền trong Return
        Double totalReturn = returnDetailRepository.calculateTotalReturnByReturnId(returnId);
        if (totalReturn == null) {
            totalReturn = 0.0;
        }
        aReturn.setTotal(totalReturn);
        returnRepository.save(aReturn);


        // Chuẩn bị dữ liệu trả về
        ReturnDetailResponseAdmin returnDetailResponseAdmin = ObjectMapperUtils.map(aReturn, ReturnDetailResponseAdmin.class);
        returnDetailResponseAdmin.setOderDetailResponseAdmin(ObjectMapperUtils.map(orderDetailResponseAdminTemps, OderDetailResponseAdmin.class));
        return returnDetailResponseAdmin;
    }

    @Override
    public ReturnResponseAdminGetAll findReturnAndDetailByCode(String code) {
        Return areturn = returnRepository.findReturnAndDetailByCode(code);
        if (areturn == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Return", null);
        }
        List<ReturnDetail> returnDetailList = returnDetailRepository.findByReturnId(areturn.getId());

        ReturnResponseAdminGetAll returnResponseAdminGetAll = ObjectMapperUtils.map(areturn, ReturnResponseAdminGetAll.class);

        List<ReturnDetailResponseAdminGet> returnDetailResponses = new ArrayList<>();
        for (ReturnDetail returnDetail : returnDetailList) {
            ReturnDetailResponseAdminGet returnDetailResponseAdmin = ObjectMapperUtils.map(returnDetail, ReturnDetailResponseAdminGet.class);
            // Kiểm tra xem Return_Detail có Order_Detail không
            if (returnDetail.getOrderDetail() != null) {
                OrderDetail orderDetail = returnDetail.getOrderDetail();
                OderDetailResponseAdminGet orderDetailResponseAdmin = ObjectMapperUtils.map(orderDetail, OderDetailResponseAdminGet.class);

                // Kiểm tra xem Order_Detail có Product_Detail không
                if (orderDetail.getProductDetail() != null) {
                    ProductDetail orderProductDetail = orderDetail.getProductDetail();
                    Optional<ProductDiscount> discount = productDiscountRepository
                            .getByProductI2dAndStatus(orderProductDetail.getProduct().getId());

                    ProductDetailResponseGet productDetailResponseAdmin = ObjectMapperUtils.map(orderProductDetail, ProductDetailResponseGet.class);
                     if (discount.isPresent()) {
                         productDetailResponseAdmin.setDiscount(discount.get().getPercent());
                    } else {
                         productDetailResponseAdmin.setDiscount(0);
                    }
                    orderDetailResponseAdmin.setProductDetailId(productDetailResponseAdmin);
                }

                returnDetailResponseAdmin.setOderDetailResponseAdmin(orderDetailResponseAdmin);
            }


            returnDetailResponseAdmin.setReturnResponseAdmin(ObjectMapperUtils.map(returnDetail.getReturns(), ReturnResponseAdmin.class));
            returnDetailResponses.add(returnDetailResponseAdmin);
        }
        returnResponseAdminGetAll.setReturnDetail(returnDetailResponses);

        return returnResponseAdminGetAll;


    }

    @Override
    public List<ReturnFullAdmin> findAllByAdmin(Integer status) {
        List<IReturnFullAdmin> lst = returnRepository.findAllByAdmin(status);
        List<ReturnFullAdmin> response = lst.stream().map(x -> returnFullAdmin(x)).toList();
        return response;
    }

    private ReturnFullAdmin returnFullAdmin(IReturnFullAdmin i) {
        return ReturnFullAdmin.builder().
                id(i.getId()).code(i.getCode()).createBy(i.getCreate_by())
                .updateBy(i.getUpdate_by()).createTime(i.getCreate_time())
                .updateTime(i.getUpdate_time()).reason(i.getReason()).status(i.getStatus()).
                build();
    }


}

