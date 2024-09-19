package com.ManShirtShop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ManShirtShop.entities.Discount;
import com.ManShirtShop.entities.Product;
import com.ManShirtShop.entities.ProductDiscount;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Integer> {
    @Query(value = "SELECT * FROM product_discount WHERE product_discount.status = 0 ORDER BY product_discount.create_time DESC", nativeQuery = true)
    List<ProductDiscount> getAllByStatus();

    @Query(value = "SELECT a FROM ProductDiscount a WHERE a.productId = :productId AND a.discountId = :discountId")
    ProductDiscount getByProductIdAndDiscount(Product productId, Discount discountId);

    @Query(value = "SELECT a FROM ProductDiscount a WHERE a.productId = :productId AND a.status = 0")
    ProductDiscount getByProductIdAndStatus(Product productId);

    @Query(value = "SELECT product_discount.* FROM defaultdb.product_discount \n" + //
            "join defaultdb.discount on product_discount.discount_id = discount.id\n" + //
            "where product_discount.product_id = :productId and discount.status= 0 and product_discount.status=0 and\n"
            + //
            "discount.start_date <= NOW() and discount.end_date >= NOW()", nativeQuery = true)
    Optional<ProductDiscount> getByProductI2dAndStatus(@Param("productId") Integer productId);

    @Query(value = "SELECT a FROM ProductDiscount a WHERE a.discountId = :discountId AND a.status = 0")
    List<ProductDiscount> getAllByDiscountIdAnStatus(Discount discountId);


    @Query(value = "SELECT product_discount.percent FROM defaultdb.product_discount \r\n" + //
            "join defaultdb.discount on product_discount.discount_id = discount.id\r\n" + //
            "where discount.start_date <= NOW() and discount.end_date >= NOW() \r\n" + //
            "and product_discount.status = 0 and discount.status = 0 and product_discount.product_id = :productId"
            , nativeQuery = true)
    Integer getPersen(@Param("productId") Integer productId);

    @Query(value = "SELECT * FROM product_discount join discount on product_discount.discount_id = discount.id where product_discount.status = 0 and DATE(discount.end_date) <= DATE(NOW())", nativeQuery = true)
    List<ProductDiscount> getAll();

}
