package com.ManShirtShop.service.Statistic;

import java.util.List;

import com.ManShirtShop.dto.Statistic.StatisticRequest;
import com.ManShirtShop.dto.Statistic.TotalByDateResponse;
import com.ManShirtShop.dto.Statistic.TotalResponsee;
import com.ManShirtShop.dto.Statistic.Customer.TopCustomerResponse;
import com.ManShirtShop.dto.Statistic.Order.OrderRequest;
import com.ManShirtShop.dto.Statistic.Order.TotalMonthResponse;
import com.ManShirtShop.dto.Statistic.Product.ProductDto;
import com.ManShirtShop.dto.Statistic.Product.TopProductResponse;
import com.ManShirtShop.dto.Statistic.ProductDetail.ProductDetailCResponse;
import com.ManShirtShop.dto.Statistic.ProductDetail.ProductDetailCustomer;

public interface StatisticService {
    TotalResponsee getTotal();
    List<TopProductResponse> getTopProduct(Integer id);
    List<TopProductResponse> getProductByDate();
    List<TopCustomerResponse> getTopCustomer();
    ProductDetailCResponse getProductDetailByCustomer(Integer id);
    TotalByDateResponse getTotalByDate(StatisticRequest filter);
    List<TotalMonthResponse> getTotalByMonth(Integer year);
    List<TotalMonthResponse> getTotalByQuarter(Integer year);
    List<TotalMonthResponse> getTotalByYear(OrderRequest orderRequest);
    TotalByDateResponse getTotalByDiscount(Integer id);
    List<TopProductResponse> getTopProductByTime(StatisticRequest filter);
}
