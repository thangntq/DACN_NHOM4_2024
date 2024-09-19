package com.ManShirtShop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ManShirtShop.entities.CheckOut;

public interface CheckOutRepository extends JpaRepository<CheckOut,Integer>{
    Optional<CheckOut> findByCode(String code);

    @Query(value = "SELECT o.code FROM CheckOut o WHERE o.codeOder = :code")
    String findByCodeOder(String code);
}
