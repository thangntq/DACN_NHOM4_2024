package com.ManShirtShop.repository;

import com.ManShirtShop.entities.Design;
import com.ManShirtShop.entities.Form;
import com.ManShirtShop.entities.Sleeve;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DesignRepository extends JpaRepository<Design,Integer> {

    @Query(value = "SELECT * FROM design WHERE design.status = 0 ORDER BY design.create_time DESC", nativeQuery = true)
    List<Design> getAllByStatus();

    @Query(value = "SELECT * FROM design WHERE design.status = 0 and design.name LIKE %:keyword%", nativeQuery = true)
    List<Design> findBySimilarName(@Param("keyword") String keyword);

    @Query(value = "SELECT design.id FROM design WHERE design.status = 0", nativeQuery = true)
    List<Integer> findAllId();

    @Query("SELECT d FROM Design d WHERE d.name = :name")
    Design findByName(String name);
}

