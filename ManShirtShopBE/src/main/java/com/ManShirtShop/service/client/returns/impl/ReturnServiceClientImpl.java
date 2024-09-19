package com.ManShirtShop.service.client.returns.impl;

import com.ManShirtShop.Authentication.dto.user.EmailUser;
import com.ManShirtShop.common.contans.ReturnsContact;
import com.ManShirtShop.common.genCode.GenCode;
import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClient;
import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClientTemp;
import com.ManShirtShop.dto.client.oderDto.OderDetailResponseClientGet;
import com.ManShirtShop.dto.client.product_detail_client.ProductDetailResponseGetClient;
import com.ManShirtShop.dto.client.returnDetail.ReturnDetailCLientAddRequest;
import com.ManShirtShop.dto.client.returnDetail.ReturnDetailResponseClient;
import com.ManShirtShop.dto.client.returnDetail.ReturnDetailResponseClientGet;
import com.ManShirtShop.dto.client.returns.ReturnRequestClient;
import com.ManShirtShop.dto.client.returns.ReturnResponseClient;
import com.ManShirtShop.dto.client.returns.ReturnResponseClientGetAll;
import com.ManShirtShop.dto.order_the_store.OderDetailResponseAdmin;
import com.ManShirtShop.dto.order_the_store.OrderDetailResponseAdminTemp;
import com.ManShirtShop.dto.returnDetail.ReturnDetailResponseAdmin;
import com.ManShirtShop.dto.returns.ReturnResponseAdmin;
import com.ManShirtShop.entities.OrderDetail;
import com.ManShirtShop.entities.ProductDetail;
import com.ManShirtShop.entities.Return;
import com.ManShirtShop.entities.ReturnDetail;
import com.ManShirtShop.repository.*;
import com.ManShirtShop.service.client.returns.ReturnServiceClient;
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
public class ReturnServiceClientImpl implements ReturnServiceClient {
    private final ReturnDetailRepository returnDetailRepository;
    private OrderDetailRepositoty orderDetailRepositoty;
    private ReturnRepository returnRepository;

    private final CustomerRepository customerRepository;
    @Autowired
    OderRepository orderRepository;

    @Autowired
    public ReturnServiceClientImpl(OrderDetailRepositoty orderDetailRepositoty, ReturnRepository returnRepository,
                                   ReturnDetailRepository returnDetailRepository,CustomerRepository customerRepository) {
        this.orderDetailRepositoty = orderDetailRepositoty;
        this.returnRepository = returnRepository;
        this.returnDetailRepository = returnDetailRepository;
        this.customerRepository = customerRepository;
    }

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
    public ReturnResponseClient addReturns(ReturnRequestClient returnRequestClient) {
        // Check nếu Order_Id truyền vào có Return_ID thành công rồi thì sẽ không cho thêm nữa !
        Integer status1 = returnRepository.checkIfReturnExistsByStatusAndOrderId(returnRequestClient.getOrderId());
        Integer status2 = orderRepository.checkIfOrderUpdatedMoreThan7DaysAgo(returnRequestClient.getOrderId());
        System.out.println(status2);
        System.out.println(status1);
        if (status2 < 0 || status2 > 7 || status1 == 1){
            throw new IllegalArgumentException("Order with id " + returnRequestClient.getOrderId() + " has already been processed.");
        }

        String fullNameByEmail = customerRepository.findFullNameByEmail(EmailUser.getEmailUser());
        returnRequestClient.setId(null);
        Return aReturn = ObjectMapperUtils.map(returnRequestClient, Return.class);
        aReturn.setCreateBy(fullNameByEmail);
        aReturn.setCreateTime(Timestamp.from(Instant.now()));
        aReturn.setUpdateTime(Timestamp.from(Instant.now()));
        aReturn.setStatus(ReturnsContact.STATUS_DEFAULT); // Mặc định status yêu cầu trả hàng
        aReturn.setReturnDetail(null);
        aReturn = returnRepository.save(aReturn);
        aReturn.setCode("HDT" + GenCode.code(aReturn.getId()));
        aReturn = returnRepository.save(aReturn);

        // Calculate TotalReturn and check return_quantity against quantity
        double totalReturn = 0.0;
        boolean hasError = false; // Biến để kiểm tra nếu có lỗi xảy ra
        String codeOrder = "";
        try {
            List<OrderDetailResponseClientTemp> orderDetails = returnRequestClient.getOrderDetailResponseClientTemps();
            if (orderDetails != null && !orderDetails.isEmpty()) {
                List<OrderDetail> orderDetailSaveOne = new ArrayList<>();
                for (OrderDetailResponseClientTemp x : orderDetails) {
                    Optional<OrderDetail> orderDetail1 = orderDetailRepositoty.findById(x.getId());
                    int returnQuantity = x.getReturnQuantity();// Số lượng tr truyền vào
                    int availableQuantity = orderDetail1.get().getQuantity();// Số lượng sản phẩm trong DB
                    codeOrder = orderDetail1.get().getOrder().getCode();
                    if (returnQuantity <= 0 || returnQuantity > availableQuantity) {
                        hasError = true;
                        break; // Thoát khỏi vòng lặp ngay nếu có lỗi
                    }
                    // if (orderDetail1.get().getReturnId() != null) {
                    //     hasError = true;
                    //     break; // Thoát khỏi vòng lặp ngay nếu có lỗi
                    // }
                    totalReturn += (returnQuantity * orderDetail1.get().getUnitprice()) - (returnQuantity * orderDetail1.get().getUnitprice()*orderDetail1.get().getDisCount()/100);
                    // orderDetail1.get().setReturnId(aReturn);
                    // Gán id của bảng Return vào trường return_id của order_detail
                    //orderDetail1.get().setReturnQuantity(returnQuantity);
                    //orderDetailSaveOne.add(orderDetail1.get());

                    ReturnDetail returnDetail = new ReturnDetail();
                    returnDetail.setCreateBy("Nhân viên");
                    returnDetail.setCreateTime(Timestamp.from(Instant.now()));
                    returnDetail.setStatus(0);
                    returnDetail.setQuantity(returnQuantity);
                    returnDetail.setOrderDetail(orderDetail1.get());
                    returnDetail.setReturns(aReturn);
                    returnDetail = returnDetailRepository.save(returnDetail);
                }
                List<OrderDetail> savedOrderDetailsAll = orderDetailRepositoty.saveAll(orderDetailSaveOne);
            } else {
                hasError = true;
            }
            if (hasError) {
                throw new IllegalArgumentException("Invalid return request. " +
                        "Please check return quantity or return_id for order details.");
            }
            aReturn.setTotal(totalReturn);
            aReturn = returnRepository.save(aReturn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Prepare the ReturnResponseClient object to return
        ReturnResponseClient returnResponseClient = ObjectMapperUtils.map(aReturn, ReturnResponseClient.class);
       return returnResponseClient;
    }



    @Override
    public ReturnResponseClient updateDangGiao(Integer returnId) {
        Return aReturn = returnRepository.findById(returnId)
                .orElseThrow(() -> new IllegalArgumentException("Return with id " + returnId + " not found."));

        if ((aReturn.getStatus() == ReturnsContact.STATUS_DANG_GIAO) ||
                (aReturn.getStatus() == ReturnsContact.STATUS_THANH_CONG) ||
                (aReturn.getStatus() == ReturnsContact.STATUS_DA_HUY) ||
                (aReturn.getStatus() == ReturnsContact.STATUS_TAO_DON_LOI)) {
            throw new IllegalArgumentException("Return with id " + returnId + " has already been processed.");
        }

        // Cập nhật trạng thái của return thành STATUS_DANG_GIAO=1
        aReturn.setStatus(ReturnsContact.STATUS_DANG_GIAO);
        aReturn.setUpdateBy("NewMemStore Nhân viên");
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
        ReturnResponseClient returnResponseClient = ObjectMapperUtils.map(aReturn, ReturnResponseClient.class);
        return returnResponseClient;
    }


    @Override
    public ReturnResponseClient updateSuccess(Integer returnId) {
        Return aReturn = returnRepository.findById(returnId)
                .orElseThrow(() -> new IllegalArgumentException("Return with id " + returnId + " not found."));

        if ((aReturn.getStatus() == ReturnsContact.STATUS_DEFAULT) ||
                (aReturn.getStatus() == ReturnsContact.STATUS_THANH_CONG) ||
                (aReturn.getStatus() == ReturnsContact.STATUS_DA_HUY) ||
                (aReturn.getStatus() == ReturnsContact.STATUS_TAO_DON_LOI)) {
            throw new IllegalArgumentException("Return with id " + returnId + " has already been processed.");
        }

        Integer statusResult = returnDetailRepository.checkStatusForReturnId(returnId);
        if (statusResult == 1) {
            throw new IllegalArgumentException("Không thể cập nhật trạng thái thành công cho Return vì không tất cả các ReturnDetail có status = 1.");
        }

        // Cập nhật trạng thái của return thành STATUS_THANH_CONG=2
        aReturn.setStatus(ReturnsContact.STATUS_THANH_CONG);
        aReturn.setUpdateBy("NewMemStore Nhân viên");
        aReturn.setUpdateTime(Timestamp.from(Instant.now()));
        Double totalReturn = returnDetailRepository.calculateTotalReturnByReturnId(returnId);
        if (totalReturn == null) {
            totalReturn = 0.0;
        }
        aReturn.setTotal(totalReturn);
        returnRepository.save(aReturn);

        ReturnResponseClient returnResponseClient = ObjectMapperUtils.map(aReturn, ReturnResponseClient.class);
        return returnResponseClient;
    }



    @Override
    public ReturnResponseClient updateTuChoi(Integer returnId) {
        Return aReturn = returnRepository.findById(returnId)
                .orElseThrow(() -> new IllegalArgumentException("Return with id " + returnId + " not found."));

        if ((aReturn.getStatus() == ReturnsContact.STATUS_DANG_GIAO) ||
                (aReturn.getStatus() == ReturnsContact.STATUS_THANH_CONG) ||
                (aReturn.getStatus() == ReturnsContact.STATUS_DA_HUY) ||
                (aReturn.getStatus() == ReturnsContact.STATUS_TAO_DON_LOI)) {
            throw new IllegalArgumentException("Return with id " + returnId + " has already been processed.");
        }

        // Cập nhật trạng thái của return thành STATUS_DA_HUY = 3 ( Từ chối trả hàng)
        aReturn.setStatus(ReturnsContact.STATUS_DA_HUY);
        aReturn.setUpdateBy("NewMemStore Nhân viên");
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
        ReturnResponseClient returnResponseClient = ObjectMapperUtils.map(aReturn, ReturnResponseClient.class);
        return returnResponseClient;
    }

    @Override
    public ReturnResponseClient updateRefuse(Integer returnId) {


        Return aReturn = returnRepository.findById(returnId)
                .orElseThrow(() -> new IllegalArgumentException("Return with id " + returnId + " not found."));

        if ((aReturn.getStatus() == ReturnsContact.STATUS_DANG_GIAO) ||
                (aReturn.getStatus() == ReturnsContact.STATUS_THANH_CONG) ||
                (aReturn.getStatus() == ReturnsContact.STATUS_DA_HUY) ||
                (aReturn.getStatus() == ReturnsContact.STATUS_TAO_DON_LOI)) {
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
        ReturnResponseClient returnResponseClient = ObjectMapperUtils.map(aReturn, ReturnResponseClient.class);
        return returnResponseClient;
    }






    @Override
    public ReturnDetailResponseClient addReturnsDetail(ReturnDetailCLientAddRequest returnDetailCLientAddRequest) {
        Integer status2 = orderRepository.checkIfOrderUpdatedMoreThan7DaysAgo(returnDetailCLientAddRequest.getOrderId());
        if (status2 < 0 || status2 > 7){
            throw new IllegalArgumentException("Order with id " + returnDetailCLientAddRequest.getOrderId()+ " has already been processed.");
        }


        Integer returnId = returnDetailCLientAddRequest.getReturnId();
        Integer orderId = returnDetailCLientAddRequest.getOrderId();

        // Kiểm tra nếu ReturnId truyền vào có Return thành công rồi thì sẽ không cho thêm nữa
        Return aReturn = returnRepository.findById(returnId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng trả hàng có id " + returnId));
        if (aReturn.getStatus() == ReturnsContact.STATUS_THANH_CONG) {
            throw new IllegalArgumentException("Đơn hàng trả hàng có id " + returnId + " đã được xử lý thành công.");
        }

        // Kiểm tra số lượng ReturnDetail theo từng OrderDetail
        List<OrderDetailResponseClientTemp> orderDetailResponseClientTemps = returnDetailCLientAddRequest.getOrderDetailResponseClientTemps();
        if (orderDetailResponseClientTemps == null || orderDetailResponseClientTemps.isEmpty()) {
            throw new IllegalArgumentException("Yêu cầu trả hàng không hợp lệ. Thiếu thông tin chi tiết đơn hàng.");
        }

        // Tạo mới danh sách ReturnDetail
        List<ReturnDetail> returnDetails = new ArrayList<>();

        for (OrderDetailResponseClientTemp temp : orderDetailResponseClientTemps) {
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
        ReturnDetailResponseClient returnDetailResponseClient = ObjectMapperUtils.map(aReturn, ReturnDetailResponseClient.class);
        returnDetailResponseClient.setOrderDetailResponseClient(ObjectMapperUtils.map(orderDetailResponseClientTemps, OrderDetailResponseClient.class));
        return returnDetailResponseClient;
    }

    @Override
    public ReturnResponseClientGetAll findReturnAndDetailByCode(String code) {
        Return areturn = returnRepository.findReturnAndDetailByCode(code);
        if (areturn == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Return", null);
        }
        List<ReturnDetail> returnDetailList = returnDetailRepository.findByReturnId(areturn.getId());
        ReturnResponseClientGetAll returnResponseClientGetAll = ObjectMapperUtils.map(areturn, ReturnResponseClientGetAll.class);
        List<ReturnDetailResponseClientGet> returnDetailResponses = new ArrayList<>();
        String codeOrder = "";
        for (ReturnDetail returnDetail : returnDetailList) {
            ReturnDetailResponseClientGet returnDetailResponseClient = ObjectMapperUtils.map(returnDetail, ReturnDetailResponseClientGet.class);
            // Kiểm tra xem Return_Detail có Order_Detail không
            if (returnDetail.getOrderDetail() != null) {
                OrderDetail orderDetail = returnDetail.getOrderDetail();
                OderDetailResponseClientGet oderDetailResponseClientGet = ObjectMapperUtils.map(orderDetail, OderDetailResponseClientGet.class);
                codeOrder = returnDetail.getOrderDetail().getOrder().getCode();
                // Kiểm tra xem Order_Detail có Product_Detail không
                if (orderDetail.getProductDetail() != null) {
                    ProductDetail orderProductDetail = orderDetail.getProductDetail();
                    ProductDetailResponseGetClient productDetailResponseGetClient = ObjectMapperUtils.map(orderProductDetail, ProductDetailResponseGetClient.class);
                    oderDetailResponseClientGet.setProductDetailId(productDetailResponseGetClient);
                }

                returnDetailResponseClient.setOderDetailResponseAdmin(oderDetailResponseClientGet);
            }


            returnDetailResponseClient.setReturnResponseAdmin(ObjectMapperUtils.map(returnDetail.getReturns(), ReturnResponseClient.class));
            returnDetailResponses.add(returnDetailResponseClient);
        }
        returnResponseClientGetAll.setReturnDetail(returnDetailResponses);
        returnResponseClientGetAll.setCodeOrder(codeOrder);
        return returnResponseClientGetAll;
    }

}

