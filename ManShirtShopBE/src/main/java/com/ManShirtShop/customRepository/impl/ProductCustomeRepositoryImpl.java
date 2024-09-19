package com.ManShirtShop.customRepository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ManShirtShop.customRepository.ProductCustomeRepository;
import com.ManShirtShop.dto.client.product.SearchProductClient;
import com.ManShirtShop.service.client.product_client.ProductFilterClientRequest;
import org.springframework.stereotype.Repository;

@Repository
public class ProductCustomeRepositoryImpl implements ProductCustomeRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<SearchProductClient> getAllClients(ProductFilterClientRequest filter) {
        StringBuilder query = new StringBuilder();
        StringBuilder where = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        query.append(" JOIN product_detail product_detail on product_detail.product_id = p.id");
        query.append(" JOIN collar collar on p.collar_id = collar.id");
        query.append(" JOIN color color on product_detail.color_id = color.id");
        query.append(" JOIN design design on p.design_id = design.id");
        query.append(" JOIN form form on p.form_id = form.id");
        query.append(" JOIN material material on p.material_id = material.id");
        query.append(" JOIN sleeve sleeve on p.sleeve_id = sleeve.id");
        query.append(" JOIN size size on product_detail.size_id = size.id");
        query.append(" JOIN category category on p.category_id = category.id");
        query.append(" JOIN original_category original_category on category.original_id = original_category.id");
        if (filter.getCollar() != null) {
            if (!filter.getCollar().isEmpty()) {
                where.append(" AND collar.name IN :collar");
                params.put("collar", filter.getCollar());
            }
        }
        if (filter.getColor() != null) {
            if (!filter.getColor().isEmpty()) {
                where.append(" AND color.name IN :color");
                params.put("color", filter.getColor());
            }
        }
        if (filter.getDesign() != null) {
            if (!filter.getDesign().isEmpty()) {
                where.append(" AND design.name IN :design");
                params.put("design", filter.getDesign());
            }
        }
        if (filter.getForm() != null) {
            if (!filter.getForm().isEmpty()) {
                where.append(" AND form.name IN :form");
                params.put("form", filter.getForm());
            }
        }
        if (filter.getMaterial() != null) {
            if (!filter.getMaterial().isEmpty()) {
                where.append(" AND material.name IN :material");
                params.put("material", filter.getMaterial());
            }
        }
        if ( filter.getSleeve() != null) {
            if (!filter.getSleeve().isEmpty()) {
                where.append(" AND sleeve.name IN :sleeve");
                params.put("sleeve", filter.getSleeve());
            }
        }
        if ( filter.getSize() != null) {
            if (!filter.getSize().isEmpty()) {
                where.append(" AND size.code IN :size");
                params.put("size", filter.getSize());
            }
        }
        if (filter.getOriCategory() != null) {
            if (!filter.getOriCategory().isEmpty()) {
                where.append(" AND original_category.name IN :category");
                params.put("category", filter.getOriCategory());
            }
        }
        if ( filter.getCategory() != null) {
            if (!filter.getCategory().isEmpty()) {
                where.append(" AND category.name IN :conCate");
                params.put("conCate", filter.getCategory());
            }
        }
        if (filter.getIdDiscount() > 0) {
            query.append(" JOIN product_discount product_discount on p.id = product_discount.product_id");
            query.append(" JOIN discount discount on product_discount.discount_id = discount.id");
            where.append(" AND ( discount.id = :discount ) AND (DATE(discount.end_date) >= DATE(now())) AND (DATE(discount.start_date) <= DATE(now()))");
            params.put("discount", filter.getIdDiscount());
        }
        if (filter.getLow() >= 0 && filter.getLow() > 0) {
            if (filter.getLow() <= filter.getHigh()) {
                where.append(" AND p.price >= :low AND p.price <= :high");
                params.put("low", filter.getLow());
                params.put("high", filter.getHigh());
            }
        }
        query.append(" WHERE p.status = 0");
        String qry = "SELECT DISTINCT p.id FROM product p " + query + where;

        String qryFull = "SELECT prd.id,prd.status,prd.name,prd.description,prd.price,prd.weight,prd.code,prd.laster,\n" +
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
                "INNER JOIN defaultdb.product ON product_detail.product_id = product.id where product.id IN (" + qry + ")\n" +
                "GROUP BY product.id HAVING product.id ) as cus ON product.id = cus.id\n" +
                "WHERE product.status = 0 and product.id IN (" + qry + ")\n" +
                "GROUP BY product.id HAVING product.id) as prd\n" +
                "left join(SELECT  product_discount.product_id, product_discount.percent, discount.start_date,discount.end_date\n" +
                "FROM defaultdb.product_discount\n" +
                "left JOIN defaultdb.discount ON product_discount.discount_id = discount.id\n" +
                "where product_discount.status = 0 and discount.status = 0 and  DATE(discount.start_date) <= NOW()\n" +
                "and DATE(discount.end_date) >= NOW()) as dis on prd.id = dis.product_id";

        Query nativeQuery = em.createNativeQuery(qryFull, SearchProductClient.class);
        params.forEach(nativeQuery::setParameter);
        List<SearchProductClient> getAllByFilterPrd = nativeQuery.getResultList();
        for (SearchProductClient x : getAllByFilterPrd) {
            System.out.println(x.getId());
            System.out.println(x.getCode());
        }
        return getAllByFilterPrd;
    }

}
