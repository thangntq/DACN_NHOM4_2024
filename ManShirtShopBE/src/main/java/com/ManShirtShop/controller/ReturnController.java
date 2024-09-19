package com.ManShirtShop.controller;

import com.ManShirtShop.controller.ApiResponse.ApiResponseCustom;
import com.ManShirtShop.dto.returnDetail.ReturnDetailAdminAddRequest;
import com.ManShirtShop.dto.returnDetail.ReturnDetailAdminUpdateRequest;
import com.ManShirtShop.dto.returnDetail.ReturnDetailResponseAdmin;
import com.ManShirtShop.dto.returns.ReturnFullAdmin;
import com.ManShirtShop.dto.returns.ReturnRequestAdmin;
import com.ManShirtShop.dto.returns.ReturnResponseAdmin;
import com.ManShirtShop.dto.returns.ReturnResponseAdminGetAll;
import com.ManShirtShop.service.returns.ReturnService;
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
@RequestMapping(value = "/api/return")
@Tag(name = "Return API Admin")
public class ReturnController {

    private final ReturnService returnService;

    @Autowired
    public ReturnController(ReturnService returnService) {
        this.returnService = returnService;
    }


    @Operation(summary = "Thêm List OrderDetail_Admin cần Trả !")
    @ApiResponse(responseCode = "200")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseCustom<ReturnResponseAdmin>> addReturn(@RequestBody ReturnRequestAdmin returnRequestAdmin) {
        try {
            ReturnResponseAdmin returnResponseAdmin = returnService.addReturns(returnRequestAdmin);
            ApiResponseCustom<ReturnResponseAdmin> response = new ApiResponseCustom<>(HttpStatus.CREATED.value(), "Thành công", returnResponseAdmin);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ApiResponseCustom<ReturnResponseAdmin> response = new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Thêm Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            // Handle other exceptions, log the error, etc.
            ApiResponseCustom<ReturnResponseAdmin> response = new ApiResponseCustom<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Thêm Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }


    @Operation(summary = "Cập nhật trạng thái THÀNH CÔNG cho Return")
    @PostMapping(value = "/updateSuccess/{returnId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<ApiResponseCustom<String>> updateSuccess(@PathVariable Integer returnId) {
        try {
            returnService.updateSuccess(returnId);
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
    @PostMapping(value = "/updateRefuse/{returnId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<ApiResponseCustom<String>> updateRefuse(@PathVariable Integer returnId) {
        try {
            returnService.updateRefuse(returnId);
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

//    @Operation(summary = "Lấy danh sách các Return Detail theo Return ID và Status của bảng Return")
//    @GetMapping(value = "/returnDetailsByStatus/{returnId}/{status}")
//    @ApiResponse(responseCode = "200")
//    public ResponseEntity<ApiResponseCustom<List<ReturnDetailResponseAdmin>>> getReturnDetailsByStatus(@PathVariable Integer returnId, @PathVariable Integer status) {
//        try {
//            List<ReturnDetailResponseAdmin> returnDetails = returnService.findAllReturnDetailsByStatusOfReturn(returnId, status);
//            ApiResponseCustom<List<ReturnDetailResponseAdmin>> response = new ApiResponseCustom<>(HttpStatus.OK.value(), "Thành công", returnDetails);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (IllegalArgumentException ex) {
//            ApiResponseCustom<List<ReturnDetailResponseAdmin>> response = new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (Exception ex) {
//            ApiResponseCustom<List<ReturnDetailResponseAdmin>> response = new ApiResponseCustom<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
//    }

    @Operation(summary = "Thêm ReturnDetail ")
    @ApiResponse(responseCode = "200")
    @PostMapping(value = "/addDetail", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseCustom<ReturnDetailResponseAdmin>> addReturnDetail(@RequestBody ReturnDetailAdminAddRequest returnDetailAdminAddRequest) {
        try {
            ReturnDetailResponseAdmin returnDetailResponseAdmin = returnService.addReturnsDetail(returnDetailAdminAddRequest);
            ApiResponseCustom<ReturnDetailResponseAdmin> response = new ApiResponseCustom<>(HttpStatus.CREATED.value(), "Thành công", returnDetailResponseAdmin);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ApiResponseCustom<ReturnDetailResponseAdmin> response = new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Thêm Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ApiResponseCustom<ReturnDetailResponseAdmin> response = new ApiResponseCustom<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Thêm Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Operation(summary = "Cập nhật trạng thái TRẢ THÀNH CÔNG = 1 cho ReturnDetail")
    @ApiResponse(responseCode = "200")
    @PostMapping(value = "/updateReturnDetail", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseCustom<ReturnDetailResponseAdmin>> updateReturnDetail(@RequestBody ReturnDetailAdminUpdateRequest returnDetailAdminUpdateRequest) {
        try {
            ReturnDetailResponseAdmin returnDetailResponseAdmin = returnService.updateReturnsDeatail(returnDetailAdminUpdateRequest);
            ApiResponseCustom<ReturnDetailResponseAdmin> response = new ApiResponseCustom<>(HttpStatus.OK.value(), "Thành công", returnDetailResponseAdmin);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ApiResponseCustom<ReturnDetailResponseAdmin> response = new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ApiResponseCustom<ReturnDetailResponseAdmin> response = new ApiResponseCustom<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Cập nhật Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Operation(summary = "Lấy thông tin Return và ReturnDetail theo mã")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/{code}")
    public ResponseEntity<ApiResponseCustom<ReturnResponseAdminGetAll>> getReturnAndDetailByCode(@PathVariable String code) {
        try {
            ReturnResponseAdminGetAll returnResponseAdmin = returnService.findReturnAndDetailByCode(code);
            ApiResponseCustom<ReturnResponseAdminGetAll> response = new ApiResponseCustom<>(HttpStatus.OK.value(), "Thành công", returnResponseAdmin);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ApiResponseCustom<ReturnResponseAdminGetAll> response = new ApiResponseCustom<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ApiResponseCustom<ReturnResponseAdminGetAll> response = new ApiResponseCustom<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lấy thông tin Lỗi", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Operation(summary = "Get ALL admin")
    @GetMapping(value = "getAll")
    public ResponseEntity<List<ReturnFullAdmin>> getReturnAllStatus(@RequestParam Integer status) {
        return ResponseEntity.ok().body(returnService.findAllByAdmin(status));
    }
}
