package com.ManShirtShop.controller.client;

import com.ManShirtShop.controller.ApiResponse.ApiResponseCustom;
import com.ManShirtShop.dto.client.returnDetail.ReturnDetailCLientAddRequest;
import com.ManShirtShop.dto.client.returnDetail.ReturnDetailResponseClient;
import com.ManShirtShop.dto.client.returns.ReturnRequestClient;
import com.ManShirtShop.dto.client.returns.ReturnResponseClient;
import com.ManShirtShop.dto.client.returns.ReturnResponseClientGetAll;
import com.ManShirtShop.dto.returnDetail.ReturnDetailAdminAddRequest;
import com.ManShirtShop.dto.returnDetail.ReturnDetailResponseAdmin;
import com.ManShirtShop.service.client.returns.ReturnServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(value = "/client/api/return")
@Tag(name = "Return API Client")
public class ReturnClientController {

    private final ReturnServiceClient returnServiceClient;
    @Autowired
    public ReturnClientController(ReturnServiceClient returnServiceClient) {
        this.returnServiceClient = returnServiceClient;
    }


    @Operation(summary = "Thêm List OrderDetail_Client cần Trả !")
    @ApiResponse(responseCode  = "200")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseCustom<ReturnResponseClient>> addReturn(@RequestBody ReturnRequestClient returnRequestClient) {
        try {

            ReturnResponseClient returnResponseClient = returnServiceClient.addReturns(returnRequestClient);
            ApiResponseCustom<ReturnResponseClient> response = new ApiResponseCustom<>(HttpStatus.CREATED.value(), "Thành công", returnResponseClient);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ApiResponseCustom<ReturnResponseClient> response = new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Thêm Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            // Handle other exceptions, log the error, etc.
            ApiResponseCustom<ReturnResponseClient> response = new ApiResponseCustom<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Thêm Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Operation(summary = "Cập nhật trạng thái Xác nhận / Đang Giao Hàng cho Return")
    @PostMapping(value = "/updateDangGiao/{returnId}")
    @ApiResponse(responseCode  = "200")
    public ResponseEntity<ApiResponseCustom<String>> updateDangGiao(@PathVariable Integer returnId) {
        try {
            returnServiceClient.updateDangGiao(returnId);
            ApiResponseCustom<String> response = new ApiResponseCustom<>(HttpStatus.OK.value(), "Thành công", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ApiResponseCustom<String> response = new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ApiResponseCustom<String> response = new ApiResponseCustom<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Cập nhật Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }


    @Operation(summary = "Cập nhật trạng thái THÀNH CÔNG cho Return")
    @PostMapping(value = "/updateSuccess/{returnId}")
    @ApiResponse(responseCode  = "200")
    public ResponseEntity<ApiResponseCustom<String>> updateSuccess(@PathVariable Integer returnId) {
        try {
            returnServiceClient.updateSuccess(returnId);
            ApiResponseCustom<String> response = new ApiResponseCustom<>(HttpStatus.OK.value(), "Thành công", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ApiResponseCustom<String> response = new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ApiResponseCustom<String> response = new ApiResponseCustom<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Cập nhật Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }


    @Operation(summary = "Cập nhật trạng thái TỪ CHỐI TRẢ HÀNG cho Return")
    @PostMapping(value = "/updateTuChoi/{returnId}")
    @ApiResponse(responseCode  = "200")
    public ResponseEntity<ApiResponseCustom<String>> updateTuChoi(@PathVariable Integer returnId) {
        try {
            returnServiceClient.updateTuChoi(returnId);
            ApiResponseCustom<String> response = new ApiResponseCustom<>(HttpStatus.OK.value(), "Thành công", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ApiResponseCustom<String> response = new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ApiResponseCustom<String> response = new ApiResponseCustom<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Cập nhật Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Operation(summary = "Cập nhật trạng thái TẠO NHẦM (ĐƠN LỖI) cho Return")
    @PostMapping(value = "/updateRefuse/{returnId}")
    @ApiResponse(responseCode  = "200")
    public ResponseEntity<ApiResponseCustom<String>> updateRefuse(@PathVariable Integer returnId) {
        try {
            returnServiceClient.updateRefuse(returnId);
            ApiResponseCustom<String> response = new ApiResponseCustom<>(HttpStatus.OK.value(), "Thành công", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ApiResponseCustom<String> response = new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ApiResponseCustom<String> response = new ApiResponseCustom<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Cập nhật Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Operation(summary = "Thêm ReturnDetail ")
    @ApiResponse(responseCode = "200")
    @PostMapping(value = "/addDetail", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseCustom<ReturnDetailResponseClient>> addReturnDetail(@RequestBody ReturnDetailCLientAddRequest returnDetailCLientAddRequest) {
        try {
            ReturnDetailResponseClient returnDetailResponseClient = returnServiceClient.addReturnsDetail(returnDetailCLientAddRequest);
            ApiResponseCustom<ReturnDetailResponseClient> response = new ApiResponseCustom<>(HttpStatus.CREATED.value(), "Thành công", returnDetailResponseClient);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ApiResponseCustom<ReturnDetailResponseClient> response = new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Thêm Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ApiResponseCustom<ReturnDetailResponseClient> response = new ApiResponseCustom<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Thêm Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Operation(summary = "Lấy thông tin Return và ReturnDetail theo mã")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/{code}")
    public ResponseEntity<ApiResponseCustom<ReturnResponseClientGetAll>> getReturnAndDetailByCode(@PathVariable String code) {
        try {
            ReturnResponseClientGetAll returnResponseClient = returnServiceClient.findReturnAndDetailByCode(code);
            ApiResponseCustom<ReturnResponseClientGetAll> response = new ApiResponseCustom<>(HttpStatus.OK.value(), "Thành công", returnResponseClient);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ApiResponseCustom<ReturnResponseClientGetAll> response = new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ApiResponseCustom<ReturnResponseClientGetAll> response = new ApiResponseCustom<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lấy thông tin Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

}
