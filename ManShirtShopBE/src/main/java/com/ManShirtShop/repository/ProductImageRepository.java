package com.ManShirtShop.repository;

import java.util.List;

import com.ManShirtShop.dto.client.product_detail_client.IProductImageSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ManShirtShop.entities.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
        // @Query(value = "SELECT new
        // com.ManShirtShop.dto.client.product.ProductImageResponseClient(i.mainImage,i.urlImage)
        // FROM ProductImage i")
        // List<ProductImageResponseClient> findProductImageclientByCodeProduct();

        List<ProductImage> findByProductId(Integer lstProductId);

        @Query(value = "SELECT product_image.url_image FROM defaultdb.product_image where product_image.product_id = :idPrd \r\n"
                        + //
                        "and product_image.color_id = :idColor and product_image.main_image = true and product_image.status = 0", nativeQuery = true)
        String findByProductIdAndColorId(@Param("idPrd") Integer productId,
                        @Param("idColor") Integer idColor);

        @Query(value = "SELECT product_image.url_image FROM defaultdb.product_image where product_image.product_id = :idPrd\n" + //
                        "and product_image.color_id = :idColor and product_image.status = 0;", nativeQuery = true)
        List<String> findByProductIdAndColorIdMainImg(@Param("idPrd") Integer productId,
                        @Param("idColor") Integer idColor);


        @Query(value = "SELECT product_image.id,product_image.url_image,\n" +
                "product_image.main_image,product_image.product_id,product_image.status,color.id as colorId,color.name,color.status as colorStaus\n" +
                "FROM defaultdb.product_image \n" +
                "join defaultdb.color on product_image.color_id = color.id\n" +
                "where product_image.product_id = :idPrd and color.status = 0;", nativeQuery = true)
        List<IProductImageSearch> findproductImageClientSearchByProductId(@Param("idPrd") Integer productId);
}
