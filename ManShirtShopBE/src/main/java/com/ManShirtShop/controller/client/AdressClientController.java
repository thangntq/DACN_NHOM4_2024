package com.ManShirtShop.controller.client;

import com.ManShirtShop.dto.adress.AdressRequest;
import com.ManShirtShop.dto.adress.AdressResponse;
import com.ManShirtShop.dto.client.adress.AdressResponseClient;
import com.ManShirtShop.service.client.adress.AdressServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "client/api/adress")
@Tag(name = "Adress Client API")
public class AdressClientController {

    private final AdressServiceClient adressServiceClient;

    @Autowired  
    public AdressClientController(AdressServiceClient adressServiceClient) {
        this.adressServiceClient = adressServiceClient;
    }

    @GetMapping("/getAllAdressClient")
    @Operation(summary = "Get all addresses by customer ID and status")
    public ResponseEntity<List<AdressResponseClient>> getAllAdressClient() {
        List<AdressResponseClient> addressResponses = adressServiceClient.getAllAdressClient();
        return ResponseEntity.ok(addressResponses);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add an address")
    public ResponseEntity<?> addAddress(@RequestBody AdressRequest adressRequest) {
        return ResponseEntity.ok(adressServiceClient.create(adressRequest));
    }

    @GetMapping("/delete")
    public ResponseEntity<AdressResponseClient> delete(@RequestParam Integer id) {
        AdressResponseClient addressResponses = adressServiceClient.delete(id);
        return ResponseEntity.ok(addressResponses);
    }
}
