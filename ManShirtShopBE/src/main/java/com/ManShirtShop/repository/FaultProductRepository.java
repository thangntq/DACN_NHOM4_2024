package com.ManShirtShop.repository;

import com.ManShirtShop.entities.FaultProduct;
import com.ManShirtShop.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FaultProductRepository extends JpaRepository<FaultProduct,Integer> {
    @Query("SELECT fp FROM FaultProduct fp WHERE fp.status = :status ORDER BY create_time DESC")
    List<FaultProduct> getAllByStatus(@Param("status") int status);

    @Query("SELECT fp FROM FaultProduct fp WHERE fp.productDetail.id = :productDetailId AND (:status IS NULL OR fp.status = :status) ORDER BY create_time DESC")
    List<FaultProduct> findAllByProductDetailIdAndStatus(@Param("productDetailId") Integer productDetailId, @Param("status") Integer status);
}
