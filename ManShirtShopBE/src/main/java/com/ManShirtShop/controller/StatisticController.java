package com.ManShirtShop.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.ManShirtShop.service.Statistic.StatisticService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "api/statistic")
@Tag(name = "Statistic api")
public class StatisticController {
    @Autowired
    StatisticService statisticService;

    @GetMapping(value = "getAllTotal")
    public ResponseEntity<TotalResponsee> getALl() {
        return ResponseEntity.ok().body(statisticService.getTotal());
    }
    @PostMapping(value = "findAllProductByDiscount")
    public ResponseEntity<List<TopProductResponse>> getAll(@RequestParam Integer id) {
        return ResponseEntity.ok().body(statisticService.getTopProduct(id));
    }
    @PostMapping(value = "getAllTotalByDate", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<TotalByDateResponse> getAllC(@RequestBody StatisticRequest filter) {
        return ResponseEntity.ok().body(statisticService.getTotalByDate(filter));
    }
    @GetMapping(value = "findtopProduct")
    public ResponseEntity<List<TopProductResponse>> getAllDate() {
        return ResponseEntity.ok().body(statisticService.getProductByDate());
    }
    @GetMapping(value = "findTopCustomer")
    public ResponseEntity<List<TopCustomerResponse>> getCustomerByDate() {
        return ResponseEntity.ok().body(statisticService.getTopCustomer());
    }
    @PostMapping(value = "findAllProductDetailByCustomer")
    public ResponseEntity<ProductDetailCResponse> getByCustomer(@RequestParam Integer id) {
        return ResponseEntity.ok().body(statisticService.getProductDetailByCustomer(id));
    }
    @PostMapping(value = "findTotalByMonth")
    public ResponseEntity<List<TotalMonthResponse>> getAllByMonth(@RequestParam Integer year) {
        return ResponseEntity.ok().body(statisticService.getTotalByMonth(year));
    }
    @PostMapping(value = "findTotalByQuarter")
    public ResponseEntity<List<TotalMonthResponse>> getAllByQuarter(@RequestParam Integer year) {
        return ResponseEntity.ok().body(statisticService.getTotalByQuarter(year));
    }
    @PostMapping(value = "findTotalByYear", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<TotalMonthResponse>> getAllByYear(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok().body(statisticService.getTotalByYear(orderRequest));
    }
    @PostMapping(value = "getAllTotalByDiscount")
    public ResponseEntity<TotalByDateResponse> getAllByDiscount(@RequestParam Integer id) {
        return ResponseEntity.ok().body(statisticService.getTotalByDiscount(id));
    }
    @PostMapping(value = "findAllProductByTime", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<TopProductResponse>> getAllProductByTime(@RequestBody StatisticRequest filter) {
        return ResponseEntity.ok().body(statisticService.getTopProductByTime(filter));
    }
}
