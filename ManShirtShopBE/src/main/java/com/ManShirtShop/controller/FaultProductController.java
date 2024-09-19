package com.ManShirtShop.controller;

import com.ManShirtShop.dto.faultProduct.FaultProductRequest;
import com.ManShirtShop.dto.faultProduct.FaultProductResponse;
import com.ManShirtShop.service.faultProduct.FaultProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/fault-product")
@Tag(name = "Fault Product API")
public class FaultProductController {

    private final FaultProductService faultProductService;

    @Autowired
    public FaultProductController(FaultProductService faultProductService) {
        this.faultProductService = faultProductService;
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add a fault product")
    public ResponseEntity<FaultProductResponse> addFaultProduct(@RequestBody FaultProductRequest faultProductRequest) {
        FaultProductResponse addedFaultProduct = faultProductService.addFaultProduct(faultProductRequest);
        return ResponseEntity.ok(addedFaultProduct);
    }

    @PutMapping(value = "/revert", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Revert a fault product")
    public ResponseEntity<FaultProductResponse> revertFaultProduct(@RequestBody FaultProductRequest faultProductRequest) {
        FaultProductResponse revertedFaultProduct = faultProductService.revertFaultProduct(faultProductRequest);
        if (revertedFaultProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(revertedFaultProduct);
    }

    @PutMapping(value="/update",consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a fault product")
    public ResponseEntity<FaultProductResponse> updateFaultProduct(@RequestBody FaultProductRequest faultProductRequest) {
        FaultProductResponse updatedFaultProduct = faultProductService.updateFaultProduct(faultProductRequest);
        return ResponseEntity.ok(updatedFaultProduct);
    }

@GetMapping("/product-details/{productDetailId}/fault-products")
@Operation(summary = "Get all fault products by product details ID and status")
public ResponseEntity<List<FaultProductResponse>> getAllFaultProductByProductDetailsIdAndStatus(
        @PathVariable("productDetailId") Integer productDetailId,
        @RequestParam(value = "status", required = false) Integer status
) {
    List<FaultProductResponse> faultProducts;
    if (status != null) {
        faultProducts = faultProductService.getAllFaultProductByProductDetailsIdAndStatus(productDetailId, status);
    } else {
        faultProducts = faultProductService.getAllFaultProductByProductDetailsIdAndStatus(productDetailId,null);
    }
    return ResponseEntity.ok(faultProducts);
}
    // Other endpoints and operations

}
