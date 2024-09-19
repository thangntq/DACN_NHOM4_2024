package com.ManShirtShop.repository;

import com.ManShirtShop.entities.Discount;
import com.ManShirtShop.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    @Query(value = "SELECT * FROM voucher WHERE voucher.status = 0", nativeQuery = true)
    List<Voucher> getAllByStatus();

    boolean existsByCode(String code);

    @Query(value = "SELECT v FROM Voucher v LEFT JOIN FETCH v.order WHERE v.id = :voucherId")
    Voucher findByIdWithOrder(Integer voucherId);

    List<Voucher> findByStatusAndEndDateGreaterThan(Integer status, LocalDateTime endDate);

    List<Voucher> findByStatusAndStartDateGreaterThanAndEndDateGreaterThan(Integer status,
            LocalDateTime currentDateTime, LocalDateTime currentDateTime1);

    @Query(value = "SELECT * FROM defaultdb.voucher where voucher.status = 0 and voucher.code = :code and voucher.start_date <= NOW() and voucher.end_date >= NOW();", nativeQuery = true)
    Optional<Voucher> findByStatusAndCode(@Param("code") String code);

    @Query(value = "SELECT * FROM defaultdb.voucher\n" + //
            "where voucher.min <= :total and voucher.start_date <= NOW() and voucher.end_date >= NOW() and voucher.status = 0", nativeQuery = true)
    List<Voucher> findClientByTotal(@Param("total") Double total);
}
