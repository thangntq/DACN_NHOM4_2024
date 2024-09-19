package com.ManShirtShop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ManShirtShop.entities.OriginalCategory;

public interface OriginalCategoryRepository extends JpaRepository<OriginalCategory, Integer>{
    @Query(value = "SELECT * FROM original_category WHERE original_category.status = 0 ORDER BY original_category.create_time DESC",nativeQuery = true)
    List<OriginalCategory> getByIdChaNull();
}
