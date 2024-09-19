package com.ManShirtShop.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ManShirtShop.service.product.ProductExcelService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ManShirtShop.dto.product.ProductFilterRequest;
import com.ManShirtShop.dto.product.ProductReponse;
import com.ManShirtShop.dto.product.ProductRequest;
import com.ManShirtShop.dto.product_Detail_Image_Dto.ProductAllRequest;
import com.ManShirtShop.service.product.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/product")
@Tag(name = "Product api")
public class Productcontroller {
    @Autowired
    ProductService productService;

    private final ProductExcelService productExcelService;

    @Autowired
    public Productcontroller(ProductExcelService productExcelService) {
        this.productExcelService = productExcelService;
    }


    @Operation(summary = "Lấy danh sách ")
    @GetMapping(value = "getAll")
    public List<ProductReponse> getAll() {
        return productService.getAll();
    }

    @Operation(summary = "Lấy ra 1 sản phẩm theo id sản phẩm ")
    @GetMapping(value = "findById")
    public ProductReponse findById(@Parameter(description = "id của sản phẩm") @RequestParam Integer id) {
        return productService.findById(id);
    }

    @PostMapping(value = "create", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ProductReponse> create(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok().body(productService.create(productRequest));
    }

    @PostMapping(value = "update", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ProductReponse> update(@RequestBody ProductAllRequest productRequest) {
        return ResponseEntity.ok().body(productService.update(productRequest));
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<Map<String,Object>> delete(@RequestParam Integer id) {
        return ResponseEntity.ok().body(productService.delete(id));
    }

    @GetMapping(value = "updateStatusHoatDong")
    public ResponseEntity<Map<String,Object>> hoatDong(@RequestParam Integer id) {
        return ResponseEntity.ok().body(productService.hoatDong(id));
    }
    @GetMapping(value = "updateStatusXoaVinhVien")
    public ResponseEntity<Map<String,Object>> Xoa(@RequestParam Integer id) {
        return ResponseEntity.ok().body(productService.xoaMem(id));
    }

    @PostMapping(value = "savePDI", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ProductReponse> create(@RequestBody ProductAllRequest orderData) {
        System.out.println(orderData.toString());
        return ResponseEntity.ok().body(productService.createProductDetailImage(orderData));
    }

    @PostMapping(value = "getAllByFilter", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public List<ProductReponse> getAllByFilter(@RequestBody ProductFilterRequest filterData) {
        return productService.getAllByFilter(filterData);
    }

//    @Operation(summary = "Export File Excel Product Template")
//    @GetMapping("/export-template")
//    public ResponseEntity<byte[]> exportExcelTemplate() {
//        try {
//            MultipartFile excelFile = productExcelService.exportExcelTemplate();
//            byte[] excelData = excelFile.getBytes();
//            return ResponseEntity.ok()
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + excelFile.getOriginalFilename())
//                    .body(excelData);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @Operation(summary = "Export File Excel Product with Status")
    @GetMapping("/export/{status}")
    public ResponseEntity<Resource> exportProductData(@PathVariable Integer status) {
        try {
            MultipartFile excelFile = productExcelService.exportDataProduct(status);
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
    @Operation(summary = "Import ProductData From File Excel to Database ")
    @ApiResponse(responseCode  = "200", description = "Thành công")
    @RequestMapping(value = "/import/fileExcle", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importProducts(@RequestPart("file")  MultipartFile file) {
            return ResponseEntity.ok( productExcelService.importDataFromExcel(file));
    }

    @Operation(summary = "Export File Excel Product Template dành cho Import Data")
    @GetMapping("/export-template-generateExcel")
    public ResponseEntity<byte[]> generateExcel() {
        try {
            MultipartFile excelFile = productExcelService.generateExcel();
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
    @Operation(summary = "Lấy danh sách ")
    @GetMapping(value = "getAllByDiscount")
    public List<ProductReponse> getAllByDiscount() {
        return productService.getAllByDiscount();
    }
}
