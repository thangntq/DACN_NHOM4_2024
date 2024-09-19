package com.ManShirtShop.repository;

import com.ManShirtShop.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ManShirtShop.entities.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating,Integer> {


    @Query(value = "SELECT * FROM defaultdb.rating where rating.customer_id= :cus and rating.order_id= :order and rating.product_id = :prd", nativeQuery = true)
    Optional<Rating> findByOrderAndCustomAndProductId(@Param("order") Integer order,@Param("cus") Integer cus,@Param("prd") Integer prd);

    @Query(value = "SELECT r FROM Rating r where r.product.id = :prd and r.status = 1 ORDER BY r.createTime")
    List<Rating> findByProductId(@Param("prd") Integer prd);

    @Query(value = "SELECT r FROM Rating r ORDER BY r.createTime desc")
    List<Rating> findAll();

    @Query(value = "SELECT rating.id FROM defaultdb.rating where rating.customer_id= :cus and rating.order_id= :order and rating.product_id = :prd", nativeQuery = true)
    Optional<Integer> findIdByOrderAndCustomAndProductId(@Param("order") Integer order,@Param("cus") Integer cus,@Param("prd") Integer prd);

}
