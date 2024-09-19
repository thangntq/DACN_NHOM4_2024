package com.ManShirtShop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ManShirtShop.entities.Category;
import com.ManShirtShop.entities.Sleeve;
import org.springframework.data.repository.query.Param;

public interface SleeveRepository extends JpaRepository<Sleeve, Integer> {
    @Query(value = "SELECT * FROM sleeve WHERE sleeve.status = 0", nativeQuery = true)
    List<Sleeve> getAllByStatus();

    @Query(value = "SELECT * FROM sleeve WHERE sleeve.status = 0 and sleeve.name LIKE %:keyword%", nativeQuery = true)
    List<Sleeve> findBySimilarName(@Param("keyword") String keyword);

    @Query(value = "SELECT sleeve.id FROM sleeve WHERE sleeve.status = 0", nativeQuery = true)
    List<Integer> findAllId();

    @Query("SELECT s FROM Sleeve s WHERE s.name = :name")
    Sleeve findByName(String name);
}
