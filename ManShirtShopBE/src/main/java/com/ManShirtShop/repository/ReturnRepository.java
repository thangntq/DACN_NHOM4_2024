package com.ManShirtShop.repository;

import com.ManShirtShop.dto.returns.IReturnFullAdmin;
import com.ManShirtShop.entities.ReturnDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ManShirtShop.entities.Return;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReturnRepository extends JpaRepository<Return,Integer> {
    @Query(value = "SELECT Case when COUNT(r.id) > 0 then true else false end FROM returns r " +
            "JOIN return_detail rd ON r.id = rd.return_id " +
            "JOIN order_details od ON rd.order_detail_id = od.id " +
            "JOIN orders o ON od.order_id = o.id " +
            "WHERE r.status IN (0, 1, 2) AND o.id = ?1 ", nativeQuery = true)
    Integer checkIfReturnExistsByStatusAndOrderId(Integer orderId);

    @Query("SELECT r FROM Return r " +
            "JOIN FETCH r.returnDetail rd " +
            "WHERE r.code = ?1")
    Return findReturnAndDetailByCode(String code);
    Return findReturnByCode(String code);

    @Query(value = "SELECT returns.code FROM defaultdb.returns \n" +
            "join defaultdb.return_detail on returns.id = return_detail.return_id\n" +
            "join defaultdb.order_details on return_detail.order_detail_id = order_details.id\n" +
            "where order_details.order_id = :id", nativeQuery = true)
    Optional<String> getCodeByIdOrder(Integer id);

    @Query(value = "SELECT returns.id,returns.status,returns.create_by,returns.create_time,\n" +
            "returns.update_by,returns.update_time,returns.reason,returns.code\n" +
            "FROM defaultdb.returns where returns.status = :status order by returns.update_by desc" , nativeQuery = true)
    List<IReturnFullAdmin> findAllByAdmin(Integer status);
}
