package com.ManShirtShop.controller.client;

import java.util.List;

import com.ManShirtShop.dto.client.product.ProductResponseClient;
import com.ManShirtShop.service.client.product_client.ProductFilterClientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ManShirtShop.dto.product.ProductReponse;
import com.ManShirtShop.entities.Product;
import com.ManShirtShop.service.product.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
// @SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "client/api/product")
@Tag(name = "Product Client api")
public class ProuductClientController {
    @Autowired
    ProductService productService;

    @Operation(summary = "Lấy danh sách Product Client ")
    @GetMapping(value = "getAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(productService.getAllClient());
    }

    @GetMapping(value = "getById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return ResponseEntity.ok(productService.getProductClientById(id));
    }
    @Operation(summary = "Fillter Product Client")
    @PostMapping(value = "getFillter",consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<List<ProductResponseClient> > getFillter(@RequestBody ProductFilterClientRequest productFilterClientRequest) {
        return ResponseEntity.ok().body(productService.fillterProductClient(productFilterClientRequest));
    }

    @Operation(summary = "Get top Product Client")
    @GetMapping(value = "getTopProduct")
    public ResponseEntity<?> getTopProduct() {
        return ResponseEntity.ok().body(productService.getTopProduct());
    }

    @Operation(summary = "Get top new Product Client")
    @GetMapping(value = "getTopNewProduct")
    public ResponseEntity<?> getTopNewProduct() {
        return ResponseEntity.ok().body(productService.getTopNewProduct());
    }

    @Operation(summary = "Get top discount Product Client")
    @GetMapping(value = "getTopDiscountProduct")
    public ResponseEntity<?> getTopDiscountProduct() {
        return ResponseEntity.ok().body(productService.getTopDiscountProduct());
    }

    @Operation(summary = "Get Product Client by discount name ")
    @GetMapping(value = "getProductByDiscountName")
    public ResponseEntity<?> getProductByDiscountName(@RequestParam String name) {
        return ResponseEntity.ok().body(productService.getProductClientByDiscountId(name));
    }


}
