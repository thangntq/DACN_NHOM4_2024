package com.ManShirtShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ManShirtShop.entities.Customer;

public interface CustomerRepolsitory extends JpaRepository<Customer,Integer> {
    
}
