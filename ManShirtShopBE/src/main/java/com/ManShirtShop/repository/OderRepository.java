package com.ManShirtShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ManShirtShop.dto.Statistic.TotalResponsee;
import com.ManShirtShop.dto.Statistic.Customer.CustomerDto;
import com.ManShirtShop.dto.Statistic.Order.CountorderDto;
import com.ManShirtShop.dto.Statistic.Order.OrderDto;
import com.ManShirtShop.dto.Statistic.Order.ProductMonthDto;
import com.ManShirtShop.dto.Statistic.Product.ProductDto;
import com.ManShirtShop.dto.Statistic.Product.TopProductResponse;
import com.ManShirtShop.dto.Statistic.ProductDetail.ProductDetailDto;
import com.ManShirtShop.dto.Statistic.Total.TotalDto;
import com.ManShirtShop.dto.client.oderDto.IOrderClient;
import com.ManShirtShop.dto.order_the_store.IOrderAllAdmin;
import com.ManShirtShop.entities.Form;
import com.ManShirtShop.entities.Order;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder.Case;

public interface OderRepository extends JpaRepository<Order, Integer> {

        @Query(value = "SELECT o FROM Order o WHERE o.status = :status", nativeQuery = false)
        List<Order> getByStatus(Integer status);

         Optional<Order> findByCode(String code);

        @Query(value = "SELECT count(*) FROM defaultdb.orders where orders.status = :status", nativeQuery = true)
        Integer sumAllOrderByStatus(Integer status);
    
    
         //thong ke
        @Query(value = "select orders.id,orders.total as total1,sum(Case when returns.status = 2 then returns.total else 0 end)\n" + //
            ",sum(CASE when exchange.status = 0 then exchange.total - exchange.freight else 0 END),sum(Case when exchange.status = 0 then exchange.total else 0 end)\n" + //
            "                    from orders left join order_details on orders.id = order_details.order_id\n" + //
            "\t\t\t\t\t          left join return_detail on order_details.id = return_detail.order_detail_id\n" + //
            "                    left join returns on return_detail.return_id = returns.id\n" + //
            "                    left join exchange on returns.id = exchange.return_id\n" + //
            "                    left join exchange_detail on exchange.id = exchange_detail.exchange_id\n" + //
            "                    where orders.status = 3\n" + //
            "                    group by orders.id,orders.total"
        , nativeQuery = true)
        List<Object> getTotalByDay();
        @Query(value = "Select p.id,p.name, sum(Case when o.status = 3 then od.quantity else 0 end) as 'quantityOrder',sum(Case when r.status = 2 then rd.quantity else 0 end) as 'quantityReturn',sum(case when e.status = 0 then ex.quantity else 0 end) as 'quantityExchange'\n" + //
        ",sum(Case when o.status = 3 then od.quantity * (od.unitprice - (od.unitprice * od.dis_count / 100)) else 0 end) as 'totalOrder',sum(case when e.status = 0 then ex.quantity * (ex.unitprice - (ex.unitprice * ex.discount / 100)) else 0 end) as 'totalExchange',sum(Case when r.status = 2 then rd.quantity*od.unitprice else 0 end) as 'totalReturn'\n" + //
        "from product p left join product_detail pd on p.id  = pd.product_id\n" + //
        "\t\t\tleft join exchange_detail ex on pd.id = ex.product_detail_id\n" + //
        "\t\t\tleft join exchange e on ex.exchange_id = e.id\n" + //
        "            left join order_details od on pd.id = od.product_detail_id\n" + //
        "            left join return_detail rd on od.id = rd.order_detail_id\n" + //
        "\t\t\t\t\t  left join returns r on rd.return_id = r.id\n" + //
        "            left join orders o on od.order_id = o.id\n" + //
        "            where o.status = 3 group by p.id,p.name order by p.id ASC"
        , nativeQuery = true)
        List<ProductDto> getProduct();
        @Query(value = "select orders.id,orders.total as 'totalOrder',sum(Case when returns.status = 2 then returns.total else 0 end) as 'totalReturn'\n" + //
            ",sum(CASE when exchange.status = 0 then exchange.total - exchange.freight else 0 END) as 'exchange',sum(Case when exchange.status = 0 then exchange.total else 0 end) as 'totalExchange'\n" + //
            ",customer.fullname as 'fullName', orders.update_time as 'updateTime'\n" + //
            "                    from orders left join order_details on orders.id = order_details.order_id\n" + //
            "\t\t\t\t\t          left join return_detail on order_details.id = return_detail.order_detail_id\n" + //
            "                    left join returns on return_detail.return_id = returns.id\n" + //
            "                    left join exchange on returns.id = exchange.return_id\n" + //
            "                    left join exchange_detail on exchange.id = exchange_detail.exchange_id\n" + //
            "                    left join customer on orders.customer_id = customer.id\n" + //
            "                    where orders.status = 3 and (Date(orders.update_time) between Date(:sDate) and Date(:eDate))\n" + //
            "                    group by orders.id,orders.total"
        , nativeQuery = true)
        List<TotalDto> getTotalByDate(@Param("sDate") String startDate, @Param("eDate") String endDate); 
        @Query(value = "Select p.id,p.name, sum(Case when o.status = 3 then od.quantity else 0 end) as 'quantityOrder', sum(Case when r.status = 2 then rd.quantity else 0 end) as 'quantityReturn',sum(case when e.status = 0 then ex.quantity else 0 end) as 'quantityExchange'\n" + //
        ",sum(Case when o.status = 3 then od.quantity * (od.unitprice - (od.unitprice * od.dis_count / 100)) else 0 end) as 'totalOrder',sum(case when e.status = 0 then ex.quantity * (ex.unitprice - (ex.unitprice * ex.discount / 100)) else 0 end) as 'totalExchange',sum(Case when r.status = 2 then rd.quantity*od.unitprice else 0 end) as 'totalReturn'\n" + //
        ",ca.name as 'nameCa',de.name as 'nameDe',f.name as 'nameF',m.name as 'nameM',sl.name as 'nameSl',c.name as 'nameC'\n" + //
        "from product p left join product_detail pd on p.id  = pd.product_id\n" + //
        "\t\t\tleft join exchange_detail ex on pd.id = ex.product_detail_id\n" + //
        "\t\t\tleft join exchange e on ex.exchange_id = e.id\n" + //
        "            left join order_details od on pd.id = od.product_detail_id\n" + //
        "            left join return_detail rd on od.id = rd.order_detail_id\n" + //
        "\t\t\t\t\t  left join returns r on rd.return_id = r.id\n" + //
        "            left join orders o on od.order_id = o.id\n" + //
        "            left join collar c on p.collar_id = c.id\n" + //
        "            left join design de on p.design_id = de.id\n" + //
        "            left join form f on p.form_id = f.id\n" + //
        "            left join material m on p.material_id = m.id\n" + //
        "            left join sleeve sl on p.sleeve_id = sl.id\n" + //
        "            left join category ca on p.category_id = ca.id\n" + //
        "\t\t\t\t\t  left join product_discount pdd on p.id = pdd.product_id left join discount d on pdd.discount_id = d.id\n" + //
        "            where p.status = 0 and o.status = 3 and d.id = :id group by p.id,p.name,ca.name,de.name,f.name,m.name,sl.name,c.name order by p.id ASC",nativeQuery = true)
        List<ProductDto> getTopProduct(Integer id);
        @Query(value = "Select p.id,p.name, sum(Case when o.status = 3 then od.quantity else 0 end) as 'quantityOrder',sum(Case when r.status = 2 then rd.quantity else 0 end) as 'quantityReturn',sum(case when e.status = 0 then ex.quantity else 0 end) as 'quantityExchange'\n" + //
        ",sum(Case when o.status = 3 then od.quantity * (od.unitprice - (od.unitprice * od.dis_count / 100)) else 0 end) as 'totalOrder',sum(case when e.status = 0 then ex.quantity * (ex.unitprice - (ex.unitprice * ex.discount / 100)) else 0 end) as 'totalExchange',sum(Case when r.status = 2 then rd.quantity*od.unitprice else 0 end) as 'totalReturn'\n" + //
        ",ca.name as 'nameCa',de.name as 'nameDe',f.name as 'nameF',m.name as 'nameM',sl.name as 'nameSl',c.name as 'nameC'\n" + //
        "from product p left join product_detail pd on p.id  = pd.product_id\n" + //
        "\t\t\tleft join exchange_detail ex on pd.id = ex.product_detail_id\n" + //
        "\t\t\tleft join exchange e on ex.exchange_id = e.id\n" + //
        "            left join order_details od on pd.id = od.product_detail_id\n" + //
        "            left join return_detail rd on od.id = rd.order_detail_id\n" + //
        "\t\t\t\t\t  left join returns r on rd.return_id = r.id\n" + //
        "            left join orders o on od.order_id = o.id\n" + //
        "            left join collar c on p.collar_id = c.id\n" + //
        "            left join design de on p.design_id = de.id\n" + //
        "            left join form f on p.form_id = f.id\n" + //
        "            left join material m on p.material_id = m.id\n" + //
        "            left join sleeve sl on p.sleeve_id = sl.id\n" + //
        "            left join category ca on p.category_id = ca.id\n" + //
        "            where p.status = 0 and o.status = 3 group by p.id,p.name,ca.name,de.name,f.name,m.name,sl.name,c.name", nativeQuery = true)
        List<ProductDto> getTopProduct2();
        @Query(value ="select Date(orders.update_time) from orders  where orders.status = 3 order by orders.update_time ASC limit 1", nativeQuery = true)
        String getFirstDay();
        @Query(value ="select Date(orders.update_time) from orders  where orders.status = 3 order by orders.update_time DESC limit 1", nativeQuery = true)
        String getLastDay();
        @Query(value = "select count(orders.id) from \n" + //
                    "\torders where orders.status = 0"
        , nativeQuery = true)
        Integer getOrderInProgress();
        @Query(value = "select count(orders.id) from \n" + //
                    "\torders where orders.status = 3"
        , nativeQuery = true)
        Integer getOrderSuccess(); 
        @Query(value = "select c.id,c.fullname,sum(Case when o.status = 3 then od.quantity else 0 end) as 'quantityOrder',\n" + //
                   "sum(Case when r.status = 2 then rd.quantity else 0 end) as 'quantityReturn',sum(case when e.status = 0 then ex.quantity else 0 end) as 'quantityExchange' \n"+//
                   ",c.photo,c.email,c.phone\n"+//
                   " from customer c left join orders o on c.id = o.customer_id\n" +//
                   "left join order_details od on o.id = od.order_id\n" + //
                   "left join return_detail rd on od.id = rd.order_detail_id\n" + //
                   "left join returns r on rd.return_id = r.id\n" + //
                   "left join exchange e on r.id = e.return_id\n" + //
                   "left join exchange_detail ex on e.id = ex.exchange_id\n" + //
                   "where o.status = 3 group by c.id,c.fullname,c.photo,c.email,c.phone order by c.id ASC"
                    , nativeQuery = true)
        List<CustomerDto> getTopCustomer();

        @Query(value = "SELECT * FROM orders ORDER BY orders.create_time DESC", nativeQuery = true)
        List<Order> getAllByStatus();
 
        @Query(value = "SELECT orders.code,orders.code_ghn,orders.status,orders.create_time,orders.city_name,\n" + //
            "orders.district_name,orders.ward_name,orders.address,orders.total,orders.status_pay\n" + //
            " FROM defaultdb.orders where orders.customer_id = :idCustomer ORDER BY orders.create_time DESC;" ,nativeQuery = true)
        List<IOrderClient> getAllByCustomerId(@Param("idCustomer")Integer idCustomer);

        @Query("SELECT new com.ManShirtShop.entities.Order(o.code,o.paymentType,o.statusPay) FROM Order o where o.code = :code and o.customer.id = :idCus")
        Optional<Order> findByCodeInSucsses(String code,Integer idCus);

        @Query(value = "SELECT orders.id,orders.code,orders.code_ghn,orders.sale_form,\n" +
                "CASE WHEN orders.ship_name IS NULL THEN orders.ship_name ELSE customer.fullname END as fullname,\n" +
                "orders.create_time,orders.total,orders.status_pay\n" +
                ",orders.status,orders.note\n" +
                "FROM defaultdb.orders join defaultdb.customer on orders.customer_id = customer.id\n" +
                "and orders.status = :status ORDER BY orders.update_time DESC" ,nativeQuery = true)
        List<IOrderAllAdmin> getAllByAdmin(@Param("status")Integer status);


        @Query(value = "SELECT CASE WHEN orders.update_time is not null THEN datediff(Date(now()),Date(orders.update_time)) ELSE -1 END " +
            "FROM orders " +
            "WHERE orders.id = ?1", nativeQuery = true)
        Integer checkIfOrderUpdatedMoreThan7DaysAgo(Integer orderId);

        @Query(value = "Select pd.id as 'id', sum(Case when o.status = 3 then od.quantity else 0 end) as 'quantityOrder',sum(Case when r.status = 2 then rd.quantity else 0 end) as 'quantityReturn',sum(case when e.status = 0 then ex.quantity else 0 end) as 'quantityExchange'\n" + //
        ",p.id as 'id1',p.name,p.price,c.name as 'cName',s.code as 'sName'\n" + //
        "from product_detail pd left join product p on pd.product_id  = p.id\n" + //
        "\t\t\tleft join exchange_detail ex on pd.id = ex.product_detail_id\n" + //
        "\t\t\tleft join exchange e on ex.exchange_id = e.id\n" + //
        "            left join order_details od on pd.id = od.product_detail_id\n" + //
        "            left join return_detail rd on od.id = rd.order_detail_id\n" + //
        "\t\t\t\t\t  left join returns r on rd.return_id = r.id\n" + //
        "            left join orders o on od.order_id = o.id\n" + //
        "            left join color c on pd.color_id = c.id\n" + //
        "            left join size s on pd.size_id = s.id\n" + //
        "            where o.id in (select o.id from customer c left join orders o on c.id = o.customer_id where o.status = 3 and c.id = :id) group by pd.id,p.id,p.name,p.price,c.name,s.code\n" + //
        "            order by pd.id ASC"
        , nativeQuery = true)
        List<ProductDetailDto> getProductDetailByCustomer(Integer id);

        @Query(value = "SELECT orders.id FROM defaultdb.orders \n" +
                "join defaultdb.order_details on orders.id = order_details.order_id\n" +
                "join defaultdb.product_detail on product_detail.id = order_details.product_detail_id\n" +
                "where product_detail.product_id = :id and (orders.id = 0 or orders.id = 1 or orders.id = 2)", nativeQuery = true)
        List<Integer> checkStatusOrderByIdProduct(@Param("id")Integer id);

        @Query(value = "SELECT orders.id FROM defaultdb.orders \n" +
                "join defaultdb.order_details on orders.id = order_details.order_id\n" +
                "join defaultdb.product_detail on product_detail.id = order_details.product_detail_id\n" +
                "where product_detail.id = :id and (orders.id = 0 or orders.id = 1 or orders.id = 2)", nativeQuery = true)
        List<Integer> checkStatusOrderByIdProductDetail(@Param("id")Integer id);
        @Query(value = "select Month(orders.update_time) as 'month',orders.id,orders.total as totalOrder,sum(Case when returns.status = 2 then returns.total else 0 end) as 'totalReturn'\n" + //
            ",sum(CASE when exchange.status = 0 then exchange.total - exchange.freight else 0 END) as 'exchange',sum(Case when exchange.status = 0 then exchange.total else 0 end) as 'totalExchange' \n" + //
            "                    from orders left join order_details on orders.id = order_details.order_id\n" + //
            "\t\t\t\t\t          left join return_detail on order_details.id = return_detail.order_detail_id\n" + //
            "                    left join returns on return_detail.return_id = returns.id\n" + //
            "                    left join exchange on returns.id = exchange.return_id\n" + //
            "                    left join exchange_detail on exchange.id = exchange_detail.exchange_id\n" + //
            "                    where orders.status = 3 and YEAR(orders.update_time) = :year\n" + //
            "                    group by orders.id,orders.total,Month(orders.update_time)"
        , nativeQuery = true)
        List<OrderDto> getTotalByMonth(Integer year);
        @Query(value = "Select Month(o.update_time) as 'month',sum(Case when o.status = 3 then od.quantity else 0 end) as 'quantityOrder'\n" + //
                        ",sum(Case when r.status = 2 then rd.quantity else 0 end) as 'quantityReturn'\n" + //
                        ",sum(case when e.status = 0 then ex.quantity else 0 end) as 'quantityExchange'\n" + //
                        "from product p left join product_detail pd on p.id  = pd.product_id\n" + //
                        "left join exchange_detail ex on pd.id = ex.product_detail_id\n" + //
                        "left join exchange e on ex.exchange_id = e.id\n" + //
                        "left join order_details od on pd.id = od.product_detail_id\n" + //
                        "left join return_detail rd on od.id = rd.order_detail_id\n" + //
                        "left join returns r on rd.return_id = r.id\n" + //
                        "left join orders o on od.order_id = o.id \n" + //
                        "where o.status = 3 and year(o.update_time) = :year group by Month(o.update_time)"
        , nativeQuery = true)
        List<ProductMonthDto> getProductByMonth(Integer year);
        @Query(value = "select Month(orders.update_time) as 'month',count(orders.id) as 'count' from orders\n" + //
                        "where orders.status = 3 and year(orders.update_time) = :year group by Month(orders.update_time)"
        , nativeQuery = true)
        List<CountorderDto> getOrderSuccessByMonth(Integer year);
        @Query(value = "select Quarter(orders.update_time) as 'month',orders.id,orders.total as totalOrder,sum(Case when returns.status = 2 then returns.total else 0 end) as 'totalReturn'\n" + //
            ",sum(CASE when exchange.status = 0 then exchange.total - exchange.freight else 0 END) as 'exchange',sum(Case when exchange.status = 0 then exchange.total else 0 end) as 'totalExchange' \n" + //
            "                    from orders left join order_details on orders.id = order_details.order_id\n" + //
            "\t\t\t\t\t          left join return_detail on order_details.id = return_detail.order_detail_id\n" + //
            "                    left join returns on return_detail.return_id = returns.id\n" + //
            "                    left join exchange on returns.id = exchange.return_id\n" + //
            "                    left join exchange_detail on exchange.id = exchange_detail.exchange_id\n" + //
            "                    where orders.status = 3 and YEAR(orders.update_time) = :year\n" + //
            "                    group by orders.id,orders.total,Quarter(orders.update_time)"
        , nativeQuery = true)
        List<OrderDto> getTotalByQuarter(Integer year);
        @Query(value = "Select Quarter(o.update_time) as 'month',sum(Case when o.status = 3 then od.quantity else 0 end) as 'quantityOrder'\n" + //
                        ",sum(Case when r.status = 2 then rd.quantity else 0 end) as 'quantityReturn'\n" + //
                        ",sum(case when e.status = 0 then ex.quantity else 0 end) as 'quantityExchange'\n" + //
                        "from product p left join product_detail pd on p.id  = pd.product_id\n" + //
                        "left join exchange_detail ex on pd.id = ex.product_detail_id\n" + //
                        "left join exchange e on ex.exchange_id = e.id\n" + //
                        "left join order_details od on pd.id = od.product_detail_id\n" + //
                        "left join return_detail rd on od.id = rd.order_detail_id\n" + //
                        "left join returns r on rd.return_id = r.id\n" + //
                        "left join orders o on od.order_id = o.id \n" + //
                        "where o.status = 3 and year(o.update_time) = :year group by Quarter(o.update_time)"
        , nativeQuery = true)
        List<ProductMonthDto> getProductByQuarter(Integer year);
        @Query(value = "select Quarter(orders.update_time) as 'month',count(orders.id) as 'count' from orders\n" + //
                        "where orders.status = 3 and year(orders.update_time) = :year group by Quarter(orders.update_time)"
        , nativeQuery = true)
        List<CountorderDto> getOrderSuccessByQuarter(Integer year);
        @Query(value = "select Year(orders.update_time) as 'month',orders.id,orders.total as totalOrder,sum(Case when returns.status = 2 then returns.total else 0 end) as 'totalReturn'\n" + //
            ",sum(CASE when exchange.status = 0 then exchange.total - exchange.freight else 0 END) as 'exchange',sum(Case when exchange.status = 0 then exchange.total else 0 end) as 'totalExchange' \n" + //
            "                    from orders left join order_details on orders.id = order_details.order_id\n" + //
            "\t\t\t\t\t          left join return_detail on order_details.id = return_detail.order_detail_id\n" + //
            "                    left join returns on return_detail.return_id = returns.id\n" + //
            "                    left join exchange on returns.id = exchange.return_id\n" + //
            "                    left join exchange_detail on exchange.id = exchange_detail.exchange_id\n" + //
            "                    where orders.status = 3 and year(orders.update_time) >= :sDate and year(orders.update_time) <= :eDate\n" + //
            "                    group by orders.id,orders.total,Year(orders.update_time)"
        , nativeQuery = true)
        List<OrderDto> getTotalByyYear(@Param("sDate") Integer starYear, @Param("eDate") Integer endYear);
        @Query(value = "Select Year(o.update_time) as 'month',sum(Case when o.status = 3 then od.quantity else 0 end) as 'quantityOrder'\n" + //
                        ",sum(Case when r.status = 2 then rd.quantity else 0 end) as 'quantityReturn'\n" + //
                        ",sum(case when e.status = 0 then ex.quantity else 0 end) as 'quantityExchange'\n" + //
                        "from product p left join product_detail pd on p.id  = pd.product_id\n" + //
                        "left join exchange_detail ex on pd.id = ex.product_detail_id\n" + //
                        "left join exchange e on ex.exchange_id = e.id\n" + //
                        "left join order_details od on pd.id = od.product_detail_id\n" + //
                        "left join return_detail rd on od.id = rd.order_detail_id\n" + //
                        "left join returns r on rd.return_id = r.id\n" + //
                        "left join orders o on od.order_id = o.id \n" + //
                        "where o.status = 3 and year(o.update_time) >= :sDate and year(o.update_time) <= :eDate group by Year(o.update_time)"
        , nativeQuery = true)
        List<ProductMonthDto> getProductByyYear(@Param("sDate") Integer startYear, @Param("eDate") Integer endYear);
        @Query(value = "select Year(orders.update_time) as 'month',count(orders.id) as 'count' from orders\n" + //
                "where orders.status = 3 and year(orders.update_time) >= :sDate and year(orders.update_time) <= :eDate group by Year(orders.update_time)"
        , nativeQuery = true)
        List<CountorderDto> getOrderSuccessByyYear(@Param("sDate") Integer startYear, @Param("eDate") Integer endYear);
        @Query(value = "select count(o.id) from customer c left join orders o on c.id = o.customer_id \n" + //
                "where o.status = 3 and c.id = :id"
        , nativeQuery = true)
        Integer getCountOrderbyCustomer(Integer id);
        @Query(value = "select orders.id,orders.total as 'totalOrder',sum(Case when returns.status = 2 then returns.total else 0 end) as 'totalReturn'\n" + //
            ",sum(CASE when exchange.status = 0 then exchange.total - exchange.freight else 0 END) as 'exchange',sum(Case when exchange.status = 0 then exchange.total else 0 end) as 'totalExchange'\n" + //
            ",customer.fullname as 'fullName', orders.update_time as 'updateTime'\n" + //
            "                    from orders left join order_details on orders.id = order_details.order_id\n" + //
            "\t\t\t\t\t          left join return_detail on order_details.id = return_detail.order_detail_id\n" + //
            "                    left join returns on return_detail.return_id = returns.id\n" + //
            "                    left join exchange on returns.id = exchange.return_id\n" + //
            "                    left join exchange_detail on exchange.id = exchange_detail.exchange_id\n" + //
            "                    left join customer on orders.customer_id = customer.id\n" + //
            "                    left join product_detail on order_details.product_detail_id = product_detail.id\n" + //
            "                    left join product on product_detail.product_id = product.id\n" + //
            "left join product_discount on product.id = product_discount.product_id left join discount on product_discount.discount_id = discount.id\n" + //
            "                    where orders.status = 3 and discount.id = :id\n" + //
            "                    group by orders.id,orders.total"
        , nativeQuery = true)
        List<TotalDto> getTotalByDiscount(Integer id); 
        @Query(value = "Select p.id,p.name, sum(Case when o.status = 3 then od.quantity else 0 end) as 'quantityOrder', sum(Case when r.status = 2 then rd.quantity else 0 end) as 'quantityReturn',sum(case when e.status = 0 then ex.quantity else 0 end) as 'quantityExchange'\n" + //
                ",sum(od.quantity*od.unitprice) as 'totalOrder',sum(case when e.status = 0 then e.total - e.freight else 0 end) as 'totalExchange',sum(Case when r.status = 2 then rd.quantity*od.unitprice else 0 end) as 'totalReturn'\n" + //
                ",ca.name as 'nameCa',de.name as 'nameDe',f.name as 'nameF',m.name as 'nameM',sl.name as 'nameSl',c.name as 'nameC'\n" + //
                "from product p left join product_detail pd on p.id  = pd.product_id\n" + //
                "\t\t\tleft join exchange_detail ex on pd.id = ex.product_detail_id\n" + //
                "\t\t\tleft join exchange e on ex.exchange_id = e.id\n" + //
                "            left join order_details od on pd.id = od.product_detail_id\n" + //
                "            left join return_detail rd on od.id = rd.order_detail_id\n" + //
                "\t\t\t\t\t  left join returns r on rd.return_id = r.id\n" + //
                "            left join orders o on od.order_id = o.id\n" + //
                "            left join collar c on p.collar_id = c.id\n" + //
                "            left join design de on p.design_id = de.id\n" + //
                "            left join form f on p.form_id = f.id\n" + //
                "            left join material m on p.material_id = m.id\n" + //
                "            left join sleeve sl on p.sleeve_id = sl.id\n" + //
                "            left join category ca on p.category_id = ca.id\n" + //
                "            where p.status = 0 and o.status = 3 and year(o.update_time) >= :sDate and year(o.update_time) <= :eDate group by p.id,p.name,ca.name,de.name,f.name,m.name,sl.name,c.name order by p.id ASC"
                ,nativeQuery = true)
        List<ProductDto> getProductByTime(@Param("sDate") String startDate, @Param("eDate") String endDate);
}
