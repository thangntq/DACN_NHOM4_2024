package com.ManShirtShop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ManShirtShop.entities.Discount;
import org.springframework.data.repository.query.Param;

public interface DiscountRepository extends JpaRepository<Discount,Integer> {
    @Query(value = "SELECT * FROM discount WHERE discount.status = 0 ORDER BY discount.create_time DESC", nativeQuery = true)
    List<Discount> getAllByStatus();
    @Query(value = "SELECT discount.id FROM discount WHERE discount.status = 0", nativeQuery = true)
    List<Integer> findAllId();

    @Query(value = "SELECT * FROM defaultdb.discount where discount.name  = :name and discount.status = 0;", nativeQuery = true)
    Optional<Discount> getByname(@Param("name") String name);
}
