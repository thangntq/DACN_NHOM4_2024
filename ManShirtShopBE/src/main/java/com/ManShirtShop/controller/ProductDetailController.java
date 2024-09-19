package com.ManShirtShop.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ManShirtShop.service.productDetail.ProductDetailExcelService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailRequest;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;
import com.ManShirtShop.service.productDetail.ProductDetailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.multipart.MultipartFile;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/productDetail")
@Tag(name = "Product detail api")
public class ProductDetailController {
    @Autowired
    ProductDetailService productDetailService;

    private final ProductDetailExcelService productDetailExcelService;
    @Autowired
    public ProductDetailController(ProductDetailExcelService productDetailExcelService) {
        this.productDetailExcelService = productDetailExcelService;
    }

    @Operation(summary = "Lấy danh sách ")
    @GetMapping(value = "getAll")
    public List<ProductDetailResponse> getAll() {
        return productDetailService.getAll();
    }

    @Operation(summary = "Lấy ra 1 sản phẩm chi tiết theo id sản phẩm chi tiết")
    @GetMapping(value = "findById")
    public ProductDetailResponse findById(
            @Parameter(description = "id của sản phẩm chi tiết") @RequestParam Integer id) {
        return productDetailService.findById(id);
    }

    @Operation(summary = "Tạo mới 1 sản phẩm chi tiết")
    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<ProductDetailResponse> create(@RequestBody ProductDetailRequest productDetailRequest) {
        return ResponseEntity.ok().body(productDetailService.Create(productDetailRequest));
    }

    @Operation(summary = "Sửa sản phẩm chi tiết")
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    @PostMapping(value = "update", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ProductDetailResponse> update(@RequestBody ProductDetailRequest productDetailRequest) {
        return ResponseEntity.ok().body(productDetailService.update(productDetailRequest));
    }

    @Operation(summary = "Ngưng sản phẩm chi tiết")
    @DeleteMapping(value = "delete")
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<Map<String,Object>> delete(@RequestParam Integer id) {
        return ResponseEntity.ok().body(productDetailService.delete(id));
    }

    @Operation(summary = "Hoạt Động sản phẩm chi tiết")
    @DeleteMapping(value = "updateStatusHoatDong")
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<Map<String,Object>> updateStatusHoatDong(@RequestParam Integer id) {
        return ResponseEntity.ok().body(productDetailService.updateHoatDong(id));
    }

    @Operation(summary = "Xoá mềm sản phẩm chi tiết")
    @DeleteMapping(value = "updateStatusXoaMem")
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<Map<String,Object>> updateStatusXoaMem(@RequestParam Integer id) {
        return ResponseEntity.ok().body(productDetailService.updateXoa(id));
    }

    @Operation(summary = "Tạo mới nhiều sản phẩm chi tiết")
    @PostMapping(value = "createAll", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @CacheEvict(value = { "product", "userclient" ,"product2"}, allEntries = true)
    public ResponseEntity<List<ProductDetailResponse>> create(
            @RequestBody List<ProductDetailRequest> productDetailRequest) {
        return ResponseEntity.ok().body(productDetailService.saveAll(productDetailRequest));
    }

    @GetMapping(value = "findByBarcode")
    public ProductDetailResponse findByBarcode(
            @Parameter(description = "id của sản phẩm chi tiết") @RequestParam String barcode) {
        return productDetailService.findByBarcode(barcode);
    }

    @GetMapping(value = "getBarcode", produces = IMAGE_PNG_VALUE)
    public ResponseEntity<?> getBarcode(@RequestParam String barCodeRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(productDetailService.getBarcode(barCodeRequest));
    }

    @Operation(summary = "Export File Excel ProductDetail Template dành cho Import Data")
    @GetMapping("/export-template-generateExcel")
    public ResponseEntity<byte[]> generateExcel() {
        try {
            MultipartFile excelFile = productDetailExcelService.generateExcel();
            byte[] excelData = excelFile.getBytes();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + excelFile.getOriginalFilename())
                    .body(excelData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Export File Excel ProductDetail with Status")
    @GetMapping("/export/{status}")
    public ResponseEntity<Resource> exportProductData(@PathVariable Integer status) {
        try {
            MultipartFile excelFile = productDetailExcelService.exportDataProduct(status);
            InputStreamResource resource = new InputStreamResource(excelFile.getInputStream());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + excelFile.getName());
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(excelFile.getContentType()))
                    .body(resource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Import ProductDetailData From File Excel to Database ")
    @ApiResponse(responseCode  = "200")
    @RequestMapping(value = "/import/fileExcle", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importProducts(@RequestPart("file")  MultipartFile file) {
        try {
            productDetailExcelService.importDataFromExcel(file);
            return new ResponseEntity<>("Data imported successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error importing data " + e.getMessage(), HttpStatus.OK);
        }
    }
}
