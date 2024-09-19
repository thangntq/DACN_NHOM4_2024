package com.ManShirtShop.service.client.adress;

import com.ManShirtShop.dto.adress.AdressRequest;
import com.ManShirtShop.dto.adress.AdressResponse;
import com.ManShirtShop.dto.client.adress.AdressResponseClient;

import java.util.List;

public interface AdressServiceClient {
    List<AdressResponseClient> getAllAdressClient();

    AdressResponseClient create(AdressRequest adressRequest);

    AdressResponseClient delete(Integer id);
}
