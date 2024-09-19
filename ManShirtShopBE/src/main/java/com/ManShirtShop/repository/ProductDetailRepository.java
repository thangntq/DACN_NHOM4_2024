package com.ManShirtShop.repository;

import java.util.List;
import java.util.Optional;

import com.ManShirtShop.dto.client.product_detail_client.IProductDetailClientSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ManShirtShop.entities.Color;
import com.ManShirtShop.entities.Product;
import com.ManShirtShop.entities.ProductDetail;
import com.ManShirtShop.entities.Size;
import org.springframework.data.repository.query.Param;

public interface ProductDetailRepository extends JpaRepository<ProductDetail,Integer>{
    @Query(value = "SELECT * FROM product_detail WHERE product_detail.status = 0 ORDER BY product_detail.create_time DESC", nativeQuery = true)
    List<ProductDetail> getAllByStatus();

    Optional<ProductDetail> findByBarCode(String barCode);

    ProductDetail findByProductAndColorAndSize(Product product, Color color, Size size);

    List<ProductDetail> findByProductId(Integer lstProductId);


    @Modifying
    @Query(value = "UPDATE defaultdb.product_detail SET product_detail.quantity = product_detail.quantity + :soluong WHERE product_detail.id = :id", nativeQuery = true)
    void updateCongQuantity(@Param("soluong") Integer soluong,@Param("id") Integer id);

    @Modifying
    @Query(value = "UPDATE defaultdb.product_detail SET product_detail.quantity = product_detail.quantity - :soluong WHERE product_detail.id = :id", nativeQuery = true)
    void updateTruQuantity(@Param("soluong") Integer soluong,@Param("id") Integer id);
  
    @Query("SELECT p FROM ProductDetail p WHERE (:status IS NULL OR p.status = :status) ORDER BY p.createTime DESC")
    List<ProductDetail> findAllByProductDetailIdAndStatusOrderByCreateTimeDesc(@Param("status") Integer status);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ProductDetail pd SET pd.status = :status WHERE pd.id = :id")
    void updateStatus(@Param("status") Integer status,@Param("id") Integer id);

    @Query(value = "SELECT product_detail.id,product_detail.bar_code,product_detail.quantity,\n" +
            "product_detail.product_id,product_detail.status,color.name,color.id as colorId, size.code,size.id as sizeId \n" +
            "FROM defaultdb.product_detail \n" +
            "join defaultdb.color on product_detail.color_id = color.id\n" +
            "join defaultdb.size on product_detail.size_id = size.id\n" +
            "where product_detail.product_id = :idPrd and color.status = 0 and size.status = 0;",nativeQuery = true)
    List<IProductDetailClientSearch> findProductDetailClientByProductId(@Param("idPrd") Integer idPrd);
}
