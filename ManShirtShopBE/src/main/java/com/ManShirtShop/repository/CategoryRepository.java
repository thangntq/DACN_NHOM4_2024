package com.ManShirtShop.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ManShirtShop.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>{

    @Query(value = "SELECT * FROM category WHERE category.status = 0 ORDER BY category.create_time DESC",nativeQuery = true)
    List<Category> getByIdChaNull();

    @Query(value = "SELECT category.id FROM category WHERE category.status = 0", nativeQuery = true)
    List<Integer> findAllId();


    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Category findByName(String name);
}
