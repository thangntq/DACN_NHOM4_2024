package com.ManShirtShop.repository;

import com.ManShirtShop.entities.OrderDetail;
import com.ManShirtShop.entities.ReturnDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReturnDetailRepository extends JpaRepository<ReturnDetail,Integer> {

    @Query(value = "SELECT * FROM return_detail WHERE return_id = :returnId ORDER BY create_time DESC", nativeQuery = true)
    List<ReturnDetail> findByReturnId(Integer returnId);

    @Query("SELECT COALESCE(SUM(rd.quantity), 0) FROM ReturnDetail rd WHERE rd.orderDetail.id = :orderDetailId")
    Integer findTotalReturnedQuantityByOrderDetailId(int orderDetailId);

    @Query(value = "SELECT COALESCE(SUM((rd.quantity * od.unitprice)-(rd.quantity * od.unitprice * od.dis_count / 100)), 0) AS total_return "
            + "FROM return_detail rd "
            + "INNER JOIN order_details od ON rd.order_detail_id = od.id "
            + "WHERE rd.return_id = :returnId AND rd.status = 1", nativeQuery = true)
    Double calculateTotalReturnByReturnId(@Param("returnId") Integer returnId);

    @Query(value = "SELECT quantity FROM return_detail WHERE id = :returnDetailId", nativeQuery = true)
    Integer getQuantityByReturnDetailId(@Param("returnDetailId") Integer returnDetailId);

    @Query(value = "SELECT CASE WHEN " +
            "(SELECT COUNT(*) FROM return_detail WHERE return_id = :returnId) = " +
            "(SELECT COUNT(*) FROM return_detail WHERE return_id = :returnId AND status = 1) " +
            "THEN 2 ELSE 1 END", nativeQuery = true)
    Integer checkStatusForReturnId(@Param("returnId") Integer returnId);

}
