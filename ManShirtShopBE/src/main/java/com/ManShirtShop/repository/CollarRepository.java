package com.ManShirtShop.repository;

import com.ManShirtShop.entities.Color;
import com.ManShirtShop.entities.Design;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ManShirtShop.entities.Collar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollarRepository extends JpaRepository<Collar,Integer> {
    @Query(value = "SELECT * FROM collar WHERE collar.status = 0 and collar.name LIKE %:keyword%", nativeQuery = true)
    List<Collar> findBySimilarName(@Param("keyword") String keyword);

    @Query(value = "SELECT collar.id FROM collar WHERE collar.status = 0", nativeQuery = true)
    List<Integer> findAllId();

    @Query(value = "SELECT * FROM collar WHERE collar.status = 0 ORDER BY collar.create_time DESC", nativeQuery = true)
    List<Collar> getAllByStatus();

    @Query("SELECT c FROM Collar c WHERE c.name = :name")
    Collar findByName(String name);
}
