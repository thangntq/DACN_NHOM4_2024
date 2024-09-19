package com.ManShirtShop.controller;

import com.ManShirtShop.dto.adress.AdressRequest;
import com.ManShirtShop.dto.adress.AdressResponse;
import com.ManShirtShop.service.adress.AdressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/adresses")
@Tag(name = "Address API")
public class AdressController {
    private final AdressService adressService;

    @Autowired
    public AdressController(AdressService adressService) {
        this.adressService = adressService;
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add an address")
    public ResponseEntity<AdressResponse> addAddress(@RequestBody AdressRequest adressRequest) {
        AdressResponse addedAddress = adressService.addAdress(adressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedAddress);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an address")
    public ResponseEntity<AdressResponse> updateAddress(@RequestBody AdressRequest addressRequest) {
        AdressResponse updatedAddress = adressService.updateAdress(addressRequest);
        return ResponseEntity.ok(updatedAddress);
    }

    @GetMapping("/customer/{customerId}/status")
    @Operation(summary = "Get all addresses by customer ID and status")
    public ResponseEntity<List<AdressResponse>> getAllAddressesByCustomerIdAndStatus(
            @PathVariable("customerId") Integer customerId,
            @RequestParam(value = "status", required = false) Integer status
    ) {
        List<AdressResponse> addressResponses = adressService.getAllAdressByCustomerIdAndStatus(customerId, status);
        return ResponseEntity.ok(addressResponses);
    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete an address by ID")
    public ResponseEntity<String> deleteAddress(@PathVariable("id") Integer addressId) {
        adressService.deleteAddress(addressId);
        return ResponseEntity.ok("Address deleted successfully.");
    }

}
