package com.ManShirtShop.repository;

import java.util.List;

import com.ManShirtShop.entities.Color;
import com.ManShirtShop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ManShirtShop.entities.Size;

public interface SizeRepository extends JpaRepository<Size,Integer>{
    @Query(value = "SELECT size.id FROM size WHERE size.status = 0", nativeQuery = true)
    List<Integer> findAllId();

    @Query(value = "SELECT * FROM size WHERE size.status = 0 ORDER BY size.create_time DESC", nativeQuery = true)
    List<Size> getAllByStatus();

    @Query("SELECT c FROM Size c WHERE c.code = :name")
    Size findByName(String name);
}
