package com.ManShirtShop.repository;

import com.ManShirtShop.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    @Query(value = "SELECT * FROM customer WHERE status = 0 ORDER BY customer.create_time DESC", nativeQuery = true)
    List<Customer> getAllActiveByStatus();

    @Query(value = "SELECT * FROM customer WHERE status = 1 ORDER BY customer.create_time DESC", nativeQuery = true)
    List<Customer> getAllDisActiveByStatus();

    Optional<Customer> findByEmail(String email);

    @Query(value = "SELECT id FROM customer WHERE id = :id", nativeQuery = true)
    List<Integer> findById(@Param("id") String id);


    @Query(value = "SELECT customer.id FROM defaultdb.customer where customer.email =  :email", nativeQuery = true)
    Integer findIdByEmail(@Param("email") String email);

    @Query(value = "SELECT customer.fullname FROM defaultdb.customer where customer.email =  :email", nativeQuery = true)
    String findFullNameByEmail(@Param("email") String email);
    Customer findCustomerByEmail(String email);
}
