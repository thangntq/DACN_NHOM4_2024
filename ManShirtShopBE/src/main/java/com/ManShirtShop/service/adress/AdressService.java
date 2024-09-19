package com.ManShirtShop.service.adress;

import com.ManShirtShop.dto.adress.AdressRequest;
import com.ManShirtShop.dto.adress.AdressResponse;
import com.ManShirtShop.dto.faultProduct.FaultProductRequest;
import com.ManShirtShop.dto.faultProduct.FaultProductResponse;
import java.util.List;

public interface AdressService {
    AdressResponse addAdress(AdressRequest adressRequest);

    AdressResponse updateAdress(AdressRequest adressRequest);

    List<AdressResponse> getAllAdressByCustomerIdAndStatus(Integer customerId, Integer status);

    AdressResponse deleteAddress(Integer addressId);
}
