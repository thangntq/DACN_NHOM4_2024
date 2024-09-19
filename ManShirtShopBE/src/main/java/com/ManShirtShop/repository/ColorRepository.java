package com.ManShirtShop.repository;

import com.ManShirtShop.entities.Collar;
import com.ManShirtShop.entities.Design;
import com.ManShirtShop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ManShirtShop.entities.Color;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color,Integer> {
    @Query(value = "SELECT * FROM color WHERE color.status = 0 and color.name LIKE %:keyword%", nativeQuery = true)
    List<Color> findBySimilarName(@Param("keyword") String keyword);

    @Query(value = "SELECT color.id FROM color WHERE color.status = 0", nativeQuery = true)
    List<Integer> findAllId();

    @Query(value = "SELECT * FROM color WHERE color.status = 0 ORDER BY color.create_time DESC", nativeQuery = true)
    List<Color> getAllByStatus();

    @Query("SELECT c FROM Color c WHERE c.name = :name")
    Color findByName(String name);
}
