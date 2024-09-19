package com.ManShirtShop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ManShirtShop.entities.CheckOut;
import com.ManShirtShop.entities.CheckOutDetail;

public interface CheckOutDetailRepository extends JpaRepository<CheckOutDetail, Integer> {

    @Query(value = "SELECT o FROM CheckOut o WHERE o.code = :code")
    Optional<CheckOut> findByCode(String code);
}
