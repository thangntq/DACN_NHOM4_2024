package com.ManShirtShop.repository;

import java.util.List;

import com.ManShirtShop.entities.Sleeve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ManShirtShop.entities.Material;
import org.springframework.data.repository.query.Param;

public interface MaterialRepository extends JpaRepository<Material,Integer> {
    @Query(value = "SELECT * FROM material WHERE material.status = 0 ORDER BY material.create_time DESC", nativeQuery = true)
    List<Material> getAllByStatus();

    @Query(value = "SELECT * FROM material WHERE material.status = 0 and material.name LIKE %:keyword%", nativeQuery = true)
    List<Material> findBySimilarName(@Param("keyword") String keyword);

    @Query(value = "SELECT material.id FROM material WHERE material.status = 0", nativeQuery = true)
    List<Integer> findAllId();

    @Query("SELECT m FROM Material m WHERE m.name = :name")
    Material findByName(String name);
}
