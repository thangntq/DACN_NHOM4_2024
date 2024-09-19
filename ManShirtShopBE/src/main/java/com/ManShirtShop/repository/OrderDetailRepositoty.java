package com.ManShirtShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ManShirtShop.entities.OrderDetail;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepositoty extends JpaRepository<OrderDetail,Integer>{
    @Query(value = "SELECT * FROM order_details WHERE return_id = :returnId", nativeQuery = true)
    List<OrderDetail> findByReturnId(Integer returnId);
}
