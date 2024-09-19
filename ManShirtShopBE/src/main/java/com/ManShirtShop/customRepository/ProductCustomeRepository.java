package com.ManShirtShop.customRepository;

import java.util.List;

import com.ManShirtShop.dto.client.product.SearchProductClient;
import com.ManShirtShop.entities.Product;
import com.ManShirtShop.service.client.product_client.ProductFilterClientRequest;

public interface ProductCustomeRepository {
    List<SearchProductClient> getAllClients(ProductFilterClientRequest filter);
}
