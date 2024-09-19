package com.ManShirtShop.repository;

import com.ManShirtShop.dto.adress.AdressResponse;
import com.ManShirtShop.entities.Address;
import com.ManShirtShop.entities.FaultProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdressRepository extends JpaRepository<Address, Integer> {

    @Query("SELECT a FROM Address a WHERE a.customer.id = :customerId AND (:status IS NULL OR a.status = :status)")
    List<Address> findAllByCustomerIdAndStatus(@Param("customerId") Integer customerId,
            @Param("status") Integer status);
    //
    // @Query("SELECT a FROM Address a WHERE a.status = 0 AND a.isDefault = true AND
    // a.customer.id = :customerId")
    // List<Address> findAllDefaultAddressesByCustomerId(@Param("customerId")
    // Integer customerId);

    // Kiểm tra xem đã có địa chỉ mặc định cho customer_id nào đó hay chưa
    Boolean existsByCustomerIdAndIsDefaultTrue(@Param("customerId") Integer customerId);

    @Query("SELECT a FROM Address a WHERE a.customer.id = :customerId AND a.isDefault = true")
    Address findByCustomerIdAndIsDefaultTrue(@Param("customerId") Integer customerId);

    @Query("SELECT a FROM Address a WHERE a.customer.id = :customerId")
    List<Address> findAllByCustomerId(@Param("customerId") Integer customerId);

}
