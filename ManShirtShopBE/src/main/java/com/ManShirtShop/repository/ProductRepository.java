package com.ManShirtShop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ManShirtShop.customRepository.ProductCustomeRepository;
import com.ManShirtShop.dto.client.product.IProductResponseClient;
import com.ManShirtShop.dto.client.product_detail_client.IProductFullResponseClient;
import com.ManShirtShop.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>, ProductCustomeRepository {
        @Query(value = "SELECT * FROM product WHERE product.status = 0 ORDER BY product.create_time DESC", nativeQuery = true)
        List<Product> getAllByStatus();

        @Query(value = "Select product.* from product\n" + //
                        "join product_detail on product.id = product_detail.product_id\n" + //
                        "where product.category_id in (:category)\n" + //
                        "and product.collar_id in (:collar) and product.design_id in (:design)\n" + //
                        "and product.form_id in (:form) and product.material_id in (:material)\n" + //
                        "and product.sleeve_id in (:sleeve) and product_detail.size_id in (:size) and product_detail.color_id in (:color)\n"
                        + //
                        "and product.status = :status\n" + //
                        "and product.price >= :low and product.price <= :high group by product.id", nativeQuery = true)
        List<Product> getAllByAll(@Param("category") List<Integer> Category,
                        @Param("collar") List<Integer> Collar,
                        @Param("design") List<Integer> Design,
                        @Param("form") List<Integer> Form,
                        @Param("material") List<Integer> Material,
                        @Param("sleeve") List<Integer> Sleeve,
                        @Param("size") List<Integer> Size,
                        @Param("color") List<Integer> Color,
                        @Param("status") Integer Status,
                        @Param("low") Double Low,
                        @Param("high") Double High);

        @Query(value = "SELECT product.price FROM product WHERE product.status = 0 ORDER BY product.price ASC Limit 1", nativeQuery = true)
        Double getLow();

        @Query(value = "SELECT product.price FROM product WHERE product.status = 0 ORDER BY product.price DESC Limit 1", nativeQuery = true)
        Double getHigh();

        @Query(value = "Select product.* from product\n" + //
                        "join product_discount on prouct.id = product_discount.product_id\n" + //
                        "join discount on product_discount.discount_id = discount.id \n" + //
                        "join product_detail on product.id = product_detail.product_id\n" + //
                        "where product.category_id in (:category)\n" + //
                        "and product.collar_id in (:collar) and product.design_id in (:design)\n" + //
                        "and product.form_id in (:form) and product.material_id in (:material)\n" + //
                        "and product.sleeve_id in (:sleeve) and product_detail.size_id in (:size) and product_detail.color_id in (:color)\n"
                        + //
                        "and product.status = :status\n" + //
                        "and product.price >= :low and product.price <= :high\n" + //
                        "and product_discount.percent >= :discount group by product.id", nativeQuery = true)
        List<Product> getAllByAllAndDiscount(@Param("category") List<Integer> Category,
                        @Param("collar") List<Integer> Collar,
                        @Param("design") List<Integer> Design,
                        @Param("form") List<Integer> Form,
                        @Param("material") List<Integer> Material,
                        @Param("sleeve") List<Integer> Sleeve,
                        @Param("size") List<Integer> Size,
                        @Param("color") List<Integer> Color,    
                        @Param("status") Integer Status,
                        @Param("low") Double Low,
                        @Param("high") Double High,
                        @Param("discount") Integer Discount);

        @Query(value = "SELECT prd.id,prd.status,prd.name,prd.description,prd.price,prd.weight,prd.code,prd.laster,\n" + //
                        "CASE WHEN prd.rating IS NULL THEN 5 ELSE prd.rating END AS 'rating',\n" + //
                        "CASE WHEN prd.soluongdaban IS NULL THEN 0 ELSE prd.soluongdaban END AS 'soluongdaban',\n" + //
                        "CASE WHEN dis.percent IS NULL THEN 0 ELSE dis.percent END AS 'discount',\n" + //
                        "CASE WHEN DATE(dis.start_date) <= NOW() and DATE(dis.end_date) >= NOW()  THEN true ELSE false END AS 'sale'\n" + //
                        "from (SELECT product.*,rt.rating as 'rating', cus.soluongdaban,\n" + //
                        "CASE WHEN DATEDIFF(NOW(), product.create_time) <= 30 THEN true ELSE false END AS 'laster'\n" + //
                        "FROM defaultdb.product\n" + //
                        "LEFT JOIN (SELECT product.id, AVG(rating.rating) AS 'rating' from defaultdb.rating\n" +
                        "INNER JOIN defaultdb.product ON rating.product_id = product.id\n" +
                        "where rating.status = 1\n" +
                        "GROUP BY product.id HAVING product.id) as rt on rt.id = product.id\n" + //
                        "LEFT JOIN (SELECT  product.id, sum(order_details.quantity) as 'soluongdaban'\n" + //
                        "FROM defaultdb.order_details\n" + //
                        "INNER JOIN defaultdb.product_detail ON order_details.product_detail_id = product_detail.id\n" + //
                        "INNER JOIN defaultdb.product ON product_detail.product_id = product.id\n" + //
                        "GROUP BY product.id HAVING product.id ) as cus ON product.id = cus.id\n" + //
                        "WHERE product.status = 0\n" + //
                        "GROUP BY product.id HAVING product.id) as prd\n" + //
                        "left join(SELECT  product_discount.product_id, product_discount.percent, discount.start_date,discount.end_date\n" + //
                        "FROM defaultdb.product_discount\n" + //
                        "left JOIN defaultdb.discount ON product_discount.discount_id = discount.id\n" + //
                        "where product_discount.status = 0 and discount.status = 0 and  DATE(discount.start_date) <= NOW()\n" + //
                        "and DATE(discount.end_date) >= NOW()) as dis on prd.id = dis.product_id",nativeQuery = true)
        List<IProductResponseClient> findProductWithCodeNamePriceAndImages();
  
        // @Query("SELECT p.code, p.name, p.price, p.description, p.weight FROM Product p")
        // List<ProductResponseClient> findProductclient();

        @Query("SELECT p FROM Product p WHERE (:status IS NULL OR p.status = :status) ORDER BY p.createTime DESC")
        List<Product> findAllByProductIdAndStatusOrderByCreateTimeDesc(@Param("status") Integer status);

        @Query("SELECT p FROM Product p WHERE p.code = :productCode")
        Product findByCode(String productCode);
        

        @Query(value = "SELECT product.id, product.status, product.name, product.description, product.price, product.weight, product.code, product.laster,\n" + //
                        "product.end_date, product.sale,product.discount,product.rating,product.soluongdaban,\n" + //
                        "category.name as 'category',collar.name as 'collar',\n" + //
                        "design.name as 'design',form.name as 'form',material.name as 'material' , sleeve.name as 'sleeve' FROM\n" + //
                        "(SELECT prd.id,prd.status,prd.name,prd.description,prd.price,prd.weight,prd.code,prd.laster,dis.end_date,\n" + //
                        "prd.category_id,prd.collar_id,prd.design_id,prd.form_id,prd.material_id,prd.sleeve_id,\n" + //
                        "CASE WHEN prd.rating IS NULL THEN 5 ELSE prd.rating END AS 'rating',\n" + //
                        "CASE WHEN prd.soluongdaban IS NULL THEN 0 ELSE prd.soluongdaban END AS 'soluongdaban',\n" + //
                        "CASE WHEN dis.percent IS NULL THEN 0 ELSE dis.percent END AS 'discount',\n" + //
                        "CASE WHEN DATE(dis.start_date) <= NOW() and DATE(dis.end_date) >= NOW()  THEN true ELSE false END AS 'sale'\n" + //
                        "from (SELECT product.*,rt.rating as 'rating', cus.soluongdaban,\n" + //
                        "CASE WHEN DATEDIFF(NOW(), product.create_time) <= 30 THEN true ELSE false END AS 'laster'\n" + //
                        "FROM defaultdb.product\n" + //
                        "LEFT JOIN (SELECT product.id, AVG(rating.rating) AS 'rating' from defaultdb.rating\n" +
                        "INNER JOIN defaultdb.product ON rating.product_id = product.id\n" +
                        "where rating.status = 1 GROUP BY product.id HAVING product.id) \n" +
                        "as rt on rt.id = product.id\n" + //
                        "LEFT JOIN (SELECT  product.id, sum(order_details.quantity) as 'soluongdaban'\n" + //
                        "FROM defaultdb.order_details\n" + //
                        "INNER JOIN defaultdb.product_detail ON order_details.product_detail_id = product_detail.id\n" + //
                        "INNER JOIN defaultdb.product ON product_detail.product_id = product.id where product.id = :id and product.status = 0) as cus ON product.id = cus.id\n" + //
                        "where product.id = :id\n" + //
                        "GROUP BY cus.soluongdaban) as prd\n" + //
                        "left join(SELECT product_discount.product_id, product_discount.percent, discount.start_date,discount.end_date\n" + //
                        "FROM defaultdb.product_discount\n" + //
                        "left JOIN defaultdb.discount ON product_discount.discount_id = discount.id\n" + //
                        "where product_discount.status = 0 and discount.status = 0 and  DATE(discount.start_date) <= NOW()\n" + //
                        "and DATE(discount.end_date) >= NOW()) as dis on prd.id = dis.product_id) as product\n" + //
                        "join defaultdb.category on product.category_id = category.id\n" + //
                        "join defaultdb.collar on product.collar_id = collar.id\n" + //
                        "join defaultdb.design on product.design_id = design.id\n" + //
                        "join defaultdb.form on product.form_id = form.id\n" + //
                        "join defaultdb.material on product.material_id = material.id\n" + //
                        "join defaultdb.sleeve on product.sleeve_id = sleeve.id;",nativeQuery = true)
        IProductFullResponseClient getOneProductClient(@Param("id") Integer ProductDetailId);
        @Query(value = "Select * from product\n" + //
                        "left join product_discount on product.id = product_discount.product_id\n" + //
                        "left join discount on product_discount.discount_id = discount.id\n" + //
                        "where product.status = 0 and product.id not in (Select product.id from product\n" + //
                        "left join product_discount on product.id = product_discount.product_id\n" + //
                        "left join discount on product_discount.discount_id = discount.id \n" + //
                        "where DATE(discount.start_date)<=DATE(now()) and DATE(discount.end_date)>=DATE(now())\n" + //
                        "and discount.status = 0 and product_discount.status = 0)",nativeQuery = true)
        List<Product> getAllByDiscount();


        @Query("SELECT c FROM Product c WHERE c.name = :name")
        Product findByName(String name);

        @Modifying
        @Query(value = "UPDATE defaultdb.product SET product.status = :status WHERE product.id = :id", nativeQuery = true)
        void updateStatusProduct(@Param("status") Integer status,@Param("id") Integer id);

        @Query(value = "SELECT prd.id,prd.status,prd.name,prd.description,prd.price,prd.weight,prd.code,prd.laster,\n" +
                "CASE WHEN prd.rating IS NULL THEN 5 ELSE prd.rating END AS 'rating',\n" +
                "CASE WHEN prd.soluongdaban IS NULL THEN 0 ELSE prd.soluongdaban END AS 'soluongdaban',\n" +
                "CASE WHEN dis.percent IS NULL THEN 0 ELSE dis.percent END AS 'discount',\n" +
                "CASE WHEN DATE(dis.start_date) <= NOW() and DATE(dis.end_date) >= NOW()  THEN true ELSE false END AS 'sale'\n" +
                "from (SELECT product.*,rt.rating as 'rating', cus.soluongdaban,\n" +
                "CASE WHEN DATEDIFF(NOW(), product.create_time) <= 30 THEN true ELSE false END AS 'laster'\n" +
                "FROM defaultdb.product\n" +
                "LEFT JOIN (SELECT product.id, AVG(rating.rating) AS 'rating' from defaultdb.rating\n" +
                "INNER JOIN defaultdb.product ON rating.product_id = product.id\n" +
                "where rating.status = 1 GROUP BY product.id HAVING product.id) \n" +
                "as rt on rt.id = product.id\n" +
                "LEFT JOIN (Select product.id, cus.soluongdaban from defaultdb.product \n" +
                "join (SELECT product.id, sum(order_details.quantity) as 'soluongdaban'\n" +
                "FROM defaultdb.order_details\n" +
                "INNER JOIN defaultdb.product_detail ON order_details.product_detail_id = product_detail.id\n" +
                "INNER JOIN defaultdb.product ON product_detail.product_id = product.id \n" +
                "where product.id GROUP BY product.id HAVING product.id) as cus on product.id = cus.id\n" +
                "limit 10) as cus ON product.id = cus.id\n" +
                "WHERE product.status = 0 and cus.id\n" +
                "GROUP BY product.id HAVING product.id) as prd\n" +
                "left join(SELECT  product_discount.product_id, product_discount.percent, discount.start_date,discount.end_date\n" +
                "FROM defaultdb.product_discount\n" +
                "left JOIN defaultdb.discount ON product_discount.discount_id = discount.id\n" +
                "where product_discount.status = 0 and discount.status = 0 and  DATE(discount.start_date) <= NOW()\n" +
                "and DATE(discount.end_date) >= NOW()) as dis on prd.id = dis.product_id order by prd.soluongdaban desc",nativeQuery = true)
        List<IProductResponseClient> getTopProduct();

        @Query(value = "SELECT prd.id,prd.status,prd.name,prd.description,prd.price,prd.weight,prd.code,prd.laster,\n" +
                "CASE WHEN prd.rating IS NULL THEN 5 ELSE prd.rating END AS 'rating',\n" +
                "CASE WHEN prd.soluongdaban IS NULL THEN 0 ELSE prd.soluongdaban END AS 'soluongdaban',\n" +
                "CASE WHEN dis.percent IS NULL THEN 0 ELSE dis.percent END AS 'discount',\n" +
                "CASE WHEN DATE(dis.start_date) <= NOW() and DATE(dis.end_date) >= NOW()  THEN true ELSE false END AS 'sale'\n" +
                "from (SELECT product.*,rt.rating as 'rating', cus.soluongdaban,\n" +
                "CASE WHEN DATEDIFF(NOW(), product.create_time) <= 30 THEN true ELSE false END AS 'laster'\n" +
                "FROM defaultdb.product\n" +
                "LEFT JOIN (SELECT product.id, AVG(rating.rating) AS 'rating' from defaultdb.rating\n" +
                "INNER JOIN defaultdb.product ON rating.product_id = product.id\n" +
                "where rating.status = 1 GROUP BY product.id HAVING product.id) \n" +
                "as rt on rt.id = product.id\n" +
                "LEFT JOIN (SELECT product.id, sum(order_details.quantity) as 'soluongdaban'\n" +
                "FROM defaultdb.order_details\n" +
                "INNER JOIN defaultdb.product_detail ON order_details.product_detail_id = product_detail.id\n" +
                "INNER JOIN defaultdb.product ON product_detail.product_id = product.id \n" +
                "where product.id GROUP BY product.id HAVING product.id order by product.create_time desc limit 10) as cus ON product.id = cus.id\n" +
                "WHERE product.status = 0\n" +
                "GROUP BY product.id HAVING product.id order by product.create_time desc limit 10) as prd\n" +
                "left join(SELECT  product_discount.product_id, product_discount.percent, discount.start_date,discount.end_date\n" +
                "FROM defaultdb.product_discount\n" +
                "left JOIN defaultdb.discount ON product_discount.discount_id = discount.id\n" +
                "where product_discount.status = 0 and discount.status = 0 and  DATE(discount.start_date) <= NOW()\n" +
                "and DATE(discount.end_date) >= NOW()) as dis on prd.id = dis.product_id;",nativeQuery = true)
        List<IProductResponseClient> getTopNewProduct();

        @Query(value = "SELECT prd.id,prd.status,prd.name,prd.description,prd.price,prd.weight,prd.code,prd.laster,\n" +
                "CASE WHEN prd.rating IS NULL THEN 5 ELSE prd.rating END AS 'rating',\n" +
                "CASE WHEN prd.soluongdaban IS NULL THEN 0 ELSE prd.soluongdaban END AS 'soluongdaban',\n" +
                "CASE WHEN dis.percent IS NULL THEN 0 ELSE dis.percent END AS 'discount',\n" +
                "CASE WHEN DATE(dis.start_date) <= NOW() and DATE(dis.end_date) >= NOW()  THEN true ELSE false END AS 'sale'\n" +
                "from (SELECT product.*,rt.rating as 'rating', cus.soluongdaban,\n" +
                "CASE WHEN DATEDIFF(NOW(), product.create_time) <= 30 THEN true ELSE false END AS 'laster'\n" +
                "FROM defaultdb.product\n" +
                "LEFT JOIN (SELECT product.id, AVG(rating.rating) AS 'rating' from defaultdb.rating\n" +
                "INNER JOIN defaultdb.product ON rating.product_id = product.id\n" +
                "where rating.status = 1\n" +
                "GROUP BY product.id HAVING product.id) as rt on rt.id = product.id\n" +
                "LEFT JOIN (SELECT product.id, sum(order_details.quantity) as 'soluongdaban'\n" +
                "FROM defaultdb.order_details\n" +
                "INNER JOIN defaultdb.product_detail ON order_details.product_detail_id = product_detail.id\n" +
                "INNER JOIN defaultdb.product ON product_detail.product_id = product.id \n" +
                "where product.id GROUP BY product.id HAVING product.id) as cus ON product.id = cus.id\n" +
                "WHERE product.status = 0\n" +
                "GROUP BY product.id HAVING product.id) as prd\n" +
                "left join(SELECT product_discount.product_id, product_discount.percent, discount.start_date,discount.end_date\n" +
                "FROM defaultdb.product_discount\n" +
                "left JOIN defaultdb.discount ON product_discount.discount_id = discount.id\n" +
                "where product_discount.status = 0 and discount.status = 0 and  DATE(discount.start_date) <= NOW()\n" +
                "and DATE(discount.end_date) >= NOW() order by product_discount.percent desc limit 10) as dis on prd.id = dis.product_id where dis.percent is not null\n" +
                "order by dis.percent desc limit 10;",nativeQuery = true)
        List<IProductResponseClient> getTopDiscountProduct();

        @Query(value = "SELECT prd.id,prd.status,prd.name,prd.description,prd.price,prd.weight,prd.code,prd.laster,\n" +
                "CASE WHEN prd.rating IS NULL THEN 5 ELSE prd.rating END AS 'rating',\n" +
                "CASE WHEN prd.soluongdaban IS NULL THEN 0 ELSE prd.soluongdaban END AS 'soluongdaban',\n" +
                "CASE WHEN dis.percent IS NULL THEN 0 ELSE dis.percent END AS 'discount',\n" +
                "CASE WHEN DATE(dis.start_date) <= NOW() and DATE(dis.end_date) >= NOW()  THEN true ELSE false END AS 'sale'\n" +
                "from (SELECT product.*,rt.rating as 'rating', cus.soluongdaban,\n" +
                "CASE WHEN DATEDIFF(NOW(), product.create_time) <= 30 THEN true ELSE false END AS 'laster'\n" +
                "FROM defaultdb.product\n" +
                "LEFT JOIN (SELECT product.id, AVG(rating.rating) AS 'rating' from defaultdb.rating\n" +
                "INNER JOIN defaultdb.product ON rating.product_id = product.id\n" +
                "where rating.status = 1\n" +
                "GROUP BY product.id HAVING product.id) as rt on rt.id = product.id\n" +
                "LEFT JOIN (SELECT product.id, sum(order_details.quantity) as 'soluongdaban'\n" +
                "FROM defaultdb.order_details\n" +
                "INNER JOIN defaultdb.product_detail ON order_details.product_detail_id = product_detail.id\n" +
                "INNER JOIN defaultdb.product ON product_detail.product_id = product.id \n" +
                "where product.id GROUP BY product.id HAVING product.id) as cus ON product.id = cus.id\n" +
                "WHERE product.status = 0\n" +
                "GROUP BY product.id HAVING product.id) as prd\n" +
                "left join(SELECT product_discount.product_id, product_discount.percent, discount.start_date,discount.end_date\n" +
                "FROM defaultdb.product_discount\n" +
                "left JOIN defaultdb.discount ON product_discount.discount_id = discount.id\n" +
                "where product_discount.status = 0 and discount.status = 0 and  DATE(discount.start_date) <= NOW()\n" +
                "and DATE(discount.end_date) >= NOW() and discount.name = :name) as dis on prd.id = dis.product_id where prd.id = dis.product_id;",nativeQuery = true)
        List<IProductResponseClient> getProductClientByDiscountId(@Param("name") String name);
}
