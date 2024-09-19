package com.ManShirtShop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ManShirtShop.entities.Form;
import com.ManShirtShop.entities.Sleeve;

import org.springframework.data.repository.query.Param;

public interface FormRepository extends JpaRepository<Form,Integer>{
    @Query(value = "SELECT * FROM form WHERE form.status = 0 ORDER BY form.create_time DESC", nativeQuery = true)
    List<Form> getAllByStatus();

    @Query(value = "SELECT * FROM form WHERE form.status = 0 and form.name LIKE %:keyword%", nativeQuery = true)
    List<Form> findBySimilarName(@Param("keyword") String keyword);

    @Query(value = "SELECT form.id FROM form WHERE form.status = 0", nativeQuery = true)
    List<Integer> findAllId();

    @Query("SELECT f FROM Form f WHERE f.name = :name")
    Form findByName(String name);
}

