package com.ManShirtShop.service.Statistic.Impl;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.Statistic.StatisticRequest;
import com.ManShirtShop.dto.Statistic.TotalByDateResponse;
import com.ManShirtShop.dto.Statistic.TotalResponsee;
import com.ManShirtShop.dto.Statistic.Customer.CustomerDto;
import com.ManShirtShop.dto.Statistic.Customer.TopCustomerResponse;
import com.ManShirtShop.dto.Statistic.Order.CountorderDto;
import com.ManShirtShop.dto.Statistic.Order.OrderDto;
import com.ManShirtShop.dto.Statistic.Order.OrderRequest;
import com.ManShirtShop.dto.Statistic.Order.ProductMonthDto;
import com.ManShirtShop.dto.Statistic.Order.TotalMonthResponse;
import com.ManShirtShop.dto.Statistic.Product.ProductDto;
import com.ManShirtShop.dto.Statistic.Product.TopProductResponse;
import com.ManShirtShop.dto.Statistic.ProductDetail.ProductDetail;
import com.ManShirtShop.dto.Statistic.ProductDetail.ProductDetailCResponse;
import com.ManShirtShop.dto.Statistic.ProductDetail.ProductDetailCustomer;
import com.ManShirtShop.dto.Statistic.ProductDetail.ProductDetailDto;
import com.ManShirtShop.dto.Statistic.Total.OrderResponse;
import com.ManShirtShop.dto.Statistic.Total.TotalDto;
import com.ManShirtShop.dto.client.product.ProductImageResponseClient;
import com.ManShirtShop.dto.client.product.ProductResponseClient;
import com.ManShirtShop.dto.client.product_detail_client.IProductImageSearch;
import com.ManShirtShop.dto.customer.CustomerResponse;
import com.ManShirtShop.dto.product.ProductFilterRequest;
import com.ManShirtShop.dto.product.ProductReponse;
import com.ManShirtShop.entities.Product;
import com.ManShirtShop.repository.OderRepository;
import com.ManShirtShop.repository.ProductImageRepository;
import com.ManShirtShop.service.Statistic.StatisticService;
import com.ManShirtShop.service.customer.CustomerService;
import com.ManShirtShop.service.product.ProductService;
import com.ManShirtShop.service.productDetail.ProductDetailService;

@Service
public class StatisticServiceImpl implements StatisticService{

    @Autowired
    OderRepository orderRepository;

    @Autowired
    ProductService productService;

    @Autowired
    CustomerService customerService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    ProductDetailService productDetailService;

    @Autowired
    ProductImageRepository productImageRepository;

    public Double getTotalBy(List<Object> resultList) {
        List<Double> listD  = new ArrayList<Double>();
        for (Object result : resultList) {
            if (result instanceof Object[]) {
                Object[] row = (Object[]) result;
                int orderId = (int) row[0];
                Double orderTotal = row[1] != null ? (Double) row[1] : 0;
                Double returnTotal = row[2] != null ? (Double) row[2] : 0;
                Double tongTien2 = row[3] != null ? (Double) row[3] : 0; 
                Double total3 = row[4] != null ? (Double) row[4] : 0;
                Double freight = total3 - tongTien2;
                Double total = orderTotal + Math.max(0, tongTien2 - returnTotal) + freight;
                listD.add(total);
            }
        }
        Double sum = 0.0;
        for(Double d : listD){
            sum += d;
        }
        return sum;
    }
    public Integer getOrderInProgress(){
        return orderRepository.getOrderInProgress();
    }
    public Integer getOrderSuccess(){
        return orderRepository.getOrderSuccess();
    }
    @Override
    public TotalResponsee getTotal() {
        TotalResponsee totalResponse = new TotalResponsee();
        totalResponse.setTotalByDay(getTotalBy(orderRepository.getTotalByDay()));
        totalResponse.setTotalByMonth(getOrderSuccess());
        totalResponse.setTotalByYear(getTotalProduct());
        totalResponse.setOrderInProgress(getOrderInProgress());
        return totalResponse;
    }
    public Long getTotalProduct(){
        List<ProductDto> resultList = orderRepository.getProduct();
        Long totalProduct = 0L;
        for(ProductDto productDto : resultList){
            Long total = productDto.getQuantityOrder()+productDto.getQuantityExchange()-productDto.getQuantityReturn();
            totalProduct = totalProduct + total;
        }
        return totalProduct;
    }
    @Override
    public List<TopProductResponse> getProductByDate(){ 
        List<ProductDto> resultList = orderRepository.getTopProduct2();
        List<TopProductResponse> listProduct = new ArrayList<TopProductResponse>();
        for (ProductDto productDto : resultList) {
            TopProductResponse topProduct = new TopProductResponse();
            topProduct.setId(productDto.getId());
            topProduct.setFullName(productDto.getName());
            topProduct.setNameC(productDto.getNameC());
            topProduct.setNameCa(productDto.getNameCa());
            topProduct.setNameDe(productDto.getNameDe());
            topProduct.setNameF(productDto.getNameF());
            topProduct.setNameM(productDto.getNameM());
            topProduct.setNameSl(productDto.getNameSl());
            topProduct.setQuantity(productDto.getQuantityOrder()+productDto.getQuantityExchange()-productDto.getQuantityReturn());
            topProduct.setTotal(productDto.getTotalOrder()+productDto.getTotalExchange()-productDto.getTotalReturn());
            topProduct.setProductImage(ObjectMapperUtils.mapAll(productImageRepository.findproductImageClientSearchByProductId(productDto.getId())
                            , ProductImageResponseClient.class));
            listProduct.add(topProduct);
        }
        Collections.sort(listProduct, new Comparator<TopProductResponse>() {
            @Override
            public int compare(TopProductResponse tp1, TopProductResponse tp2) {
                return tp2.getTotal().compareTo(tp1.getTotal());
            }
        });
        List<TopProductResponse> list2 = listProduct.subList(0,Math.min(listProduct.size(), 5));
        return list2;
    }
    @Override
    public List<TopCustomerResponse> getTopCustomer(){
        List<CustomerDto> resultList = orderRepository.getTopCustomer();
        List<TopCustomerResponse> listProduct = new ArrayList<TopCustomerResponse>();
        for (CustomerDto productDto : resultList) {
            TopCustomerResponse topProduct = new TopCustomerResponse();
 
            topProduct.setId(productDto.getId());
            topProduct.setFullName(productDto.getFullName());
            topProduct.setEmail(productDto.getEmail());
            topProduct.setPhone(productDto.getPhone());
            topProduct.setPhoto(productDto.getPhoto());
            topProduct.setCountProduct(productDto.getQuantityOrder()+productDto.getQuantityExchange()-productDto.getQuantityReturn());
            listProduct.add(topProduct);
        }
        Collections.sort(listProduct, new Comparator<TopCustomerResponse>() {
            @Override
            public int compare(TopCustomerResponse tp1, TopCustomerResponse tp2) {
                return tp2.getCountProduct().compareTo(tp1.getCountProduct());
            }
        });
        List<TopCustomerResponse> list2 = listProduct.subList(0,Math.min(10,listProduct.size()));
        return list2;
    }
    @Override
    public TotalByDateResponse getTotalByDate(StatisticRequest filter) {
        if (filter.getStartDate() == null || filter.getEndDate() == null){
            filter.setStartDate(orderRepository.getFirstDay());
            filter.setEndDate(orderRepository.getLastDay());
        }
        List<TotalDto> resultList = orderRepository.getTotalByDate(filter.getStartDate(), filter.getEndDate());
        Double total = 0.0;
        List<Integer> listO = new ArrayList<Integer>();
        List<OrderResponse> list = new ArrayList<>();
        for (TotalDto result : resultList) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(result.getId());
            orderResponse.setName(result.getFullName());
            orderResponse.setUpdateTime(result.getUpdateTime());
            total = total + result.getTotalOrder() + Math.max(0, result.getExchange() - result.getTotalReturn() ) + result.getTotalExchange() - result.getExchange();
            listO.add(result.getId());
            list.add(orderResponse);
        }
        TotalByDateResponse totalByDateResponse = new TotalByDateResponse();
        totalByDateResponse.setOrder(list);
        totalByDateResponse.setOrderNumber(listO.size());
        totalByDateResponse.setTotal(total);
        return totalByDateResponse;
    }
    @Override
    public TotalByDateResponse getTotalByDiscount(Integer id) {
        List<TotalDto> resultList = orderRepository.getTotalByDiscount(id);
        Double total = 0.0;
        List<Integer> listO = new ArrayList<Integer>();
        List<OrderResponse> list = new ArrayList<>();
        for (TotalDto result : resultList) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(result.getId());
            orderResponse.setName(result.getFullName());
            orderResponse.setUpdateTime(result.getUpdateTime());
            total = total + result.getTotalOrder() + Math.max(0, result.getExchange() - result.getTotalReturn() ) + result.getTotalExchange() - result.getExchange();
            listO.add(result.getId());
            list.add(orderResponse);
        }
        TotalByDateResponse totalByDateResponse = new TotalByDateResponse();
        totalByDateResponse.setOrder(list);
        totalByDateResponse.setOrderNumber(listO.size());
        totalByDateResponse.setTotal(total);
        return totalByDateResponse;
    }
    @Override
    public List<TopProductResponse> getTopProduct(Integer id){
        List<ProductDto> resultList = orderRepository.getTopProduct(id);
        List<TopProductResponse> listProduct = new ArrayList<TopProductResponse>();
        for (ProductDto productDto : resultList) {
            TopProductResponse topProduct = new TopProductResponse();
            topProduct.setId(productDto.getId());
            topProduct.setFullName(productDto.getName());
            topProduct.setNameC(productDto.getNameC());
            topProduct.setNameCa(productDto.getNameCa());
            topProduct.setNameDe(productDto.getNameDe());
            topProduct.setNameF(productDto.getNameF());
            topProduct.setNameM(productDto.getNameM());
            topProduct.setNameSl(productDto.getNameSl());
            topProduct.setQuantity(productDto.getQuantityOrder()+productDto.getQuantityExchange()-productDto.getQuantityReturn());
            topProduct.setTotal(productDto.getTotalOrder()+productDto.getTotalExchange()-productDto.getTotalReturn());
            topProduct.setProductImage(ObjectMapperUtils.mapAll(productImageRepository.findproductImageClientSearchByProductId(productDto.getId())
                            , ProductImageResponseClient.class));
            listProduct.add(topProduct);
        }
        Collections.sort(listProduct, new Comparator<TopProductResponse>() {
            @Override
            public int compare(TopProductResponse tp1, TopProductResponse tp2) {
                return tp2.getTotal().compareTo(tp1.getTotal());
            }
        });
        return listProduct;
    }
    @Override
    public ProductDetailCResponse getProductDetailByCustomer(Integer id) {
        List<ProductDetailDto> resulList = orderRepository.getProductDetailByCustomer(id);
        List<ProductDetailCustomer> list2 = new ArrayList<>();
        ProductDetailCResponse productDetailCResponse = new ProductDetailCResponse();
        for (ProductDetailDto productDetailDto : resulList) {
            ProductDetailCustomer productDetail2 = new ProductDetailCustomer();
            ProductDetail productDetail = new ProductDetail();
            productDetail.setId(productDetailDto.getId());
            productDetail.setColorName(productDetailDto.getCName());
            productDetail.setSizeName(productDetailDto.getSname());
            productDetail.setIdProduct(productDetailDto.getId1());
            productDetail.setNameProduct(productDetailDto.getName());
            productDetail.setPrice(productDetailDto.getPrice());
            productDetail2.setProductDetail(productDetail);
            productDetail2.setCountProductDetail(productDetailDto.getQuantityOrder()+productDetailDto.getQuantityExchange()-productDetailDto.getQuantityReturn());
            list2.add(productDetail2);
        }
        productDetailCResponse.setProductDetailCustomer(list2);
        productDetailCResponse.setCountOrder(orderRepository.getCountOrderbyCustomer(id));
        return productDetailCResponse; 
    }
    @Override
    public List<TotalMonthResponse> getTotalByMonth(Integer year) {
        List<TotalMonthResponse> list = new ArrayList<>();
        List<OrderDto> listD  = orderRepository.getTotalByMonth(year);
        List<ProductMonthDto> listP = orderRepository.getProductByMonth(year);
        List<CountorderDto> listO = orderRepository.getOrderSuccessByMonth(year);
        for(int i = 1; i <= 12; i++){
            Double total = 0.0;
            Long totalP = 0L;
            Integer totalO = 0;
            TotalMonthResponse totalMonthResponse = new TotalMonthResponse();
            totalMonthResponse.setMonth(i);
            for (OrderDto orderDto : listD) {
                if(orderDto.getMonth() == i){
                    Double freight = orderDto.getTotalExchange() - orderDto.getExchange();
                    Double totalMoney = orderDto.getTotalOrder() + Math.max(0, orderDto.getExchange() - orderDto.getTotalReturn()) + freight;
                    total = total + totalMoney;
                }
            }
            totalMonthResponse.setTotal(total);
            for (ProductMonthDto productM : listP) {
                if(productM.getMonth() == i){
                    Long totalProduct = productM.getQuantityOrder() + productM.getQuantityExchange() - productM.getQuantityReturn();
                    totalP = totalP + totalProduct;
                }
            }
            totalMonthResponse.setProductCount(totalP);
            for (CountorderDto count : listO) {
                if(count.getMonth() == i){
                    totalO = totalO + count.getCount();
                }
            }
            totalMonthResponse.setOrderSucsses(totalO);
            list.add(totalMonthResponse);
        }
        return list;
    }
    @Override
    public List<TotalMonthResponse> getTotalByQuarter(Integer year) {
        List<TotalMonthResponse> list = new ArrayList<>();
        List<OrderDto> listD  = orderRepository.getTotalByQuarter(year);
        List<ProductMonthDto> listP = orderRepository.getProductByQuarter(year);
        List<CountorderDto> listO = orderRepository.getOrderSuccessByQuarter(year);
        for(int i = 1; i <= 4; i++){
            Double total = 0.0;
            Long totalP = 0L;
            Integer totalO = 0;
            TotalMonthResponse totalMonthResponse = new TotalMonthResponse();
            totalMonthResponse.setMonth(i);
            for (OrderDto orderDto : listD) {
                if(orderDto.getMonth() == i){
                    Double freight = orderDto.getTotalExchange() - orderDto.getExchange();
                    Double totalMoney = orderDto.getTotalOrder() + Math.max(0, orderDto.getExchange() - orderDto.getTotalReturn()) + freight;
                    total = total + totalMoney;
                }
            }
            totalMonthResponse.setTotal(total);
            for (ProductMonthDto productM : listP) {
                if(productM.getMonth() == i){
                    Long totalProduct = productM.getQuantityOrder() + productM.getQuantityExchange() - productM.getQuantityReturn();
                    totalP = totalP + totalProduct;
                }
            }
            totalMonthResponse.setProductCount(totalP);
            for (CountorderDto count : listO) {
                if(count.getMonth() == i){
                    totalO = totalO + count.getCount();
                }
            }
            totalMonthResponse.setOrderSucsses(totalO);
            list.add(totalMonthResponse);
        }
        return list;
    }
    @Override
    public List<TotalMonthResponse> getTotalByYear(OrderRequest orderRequest) {
        List<TotalMonthResponse> list = new ArrayList<>();
        List<OrderDto> listD  = orderRepository.getTotalByyYear(orderRequest.getStartYear(),orderRequest.getEndYear());
        List<ProductMonthDto> listP = orderRepository.getProductByyYear(orderRequest.getStartYear(),orderRequest.getEndYear());
        List<CountorderDto> listO = orderRepository.getOrderSuccessByyYear(orderRequest.getStartYear(),orderRequest.getEndYear());
        for(Integer i=orderRequest.getStartYear();i <= orderRequest.getEndYear() ;i++){
            Double total = 0.0;
            Long totalP = 0L;
            Integer totalO = 0;
            TotalMonthResponse totalMonthResponse = new TotalMonthResponse();
            totalMonthResponse.setMonth(i);
            for (OrderDto orderDto : listD) {
                if(orderDto.getMonth().equals(i)){
                    Double freight = orderDto.getTotalExchange() - orderDto.getExchange();
                    Double totalMoney = orderDto.getTotalOrder() + Math.max(0, orderDto.getExchange() - orderDto.getTotalReturn()) + freight;
                    total = total + totalMoney;
                }
            }
            totalMonthResponse.setTotal(total);
            for (ProductMonthDto productM : listP) {
                if(productM.getMonth().equals(i)){
                    Long totalProduct = productM.getQuantityOrder() + productM.getQuantityExchange() - productM.getQuantityReturn();
                    totalP = totalP + totalProduct;
                }
            }
            totalMonthResponse.setProductCount(totalP);
            for (CountorderDto count : listO) {
                if(count.getMonth().equals(i)){
                    totalO = totalO + count.getCount();
                }
            }
            totalMonthResponse.setOrderSucsses(totalO);
            list.add(totalMonthResponse);
        }
        return list;
    }
    @Override
    public List<TopProductResponse> getTopProductByTime(StatisticRequest filter){
        if (filter.getStartDate() == null || filter.getEndDate() == null){
            filter.setStartDate(orderRepository.getFirstDay());
            filter.setEndDate(orderRepository.getLastDay());
        }
        List<ProductDto> resultList = orderRepository.getProductByTime(filter.getStartDate(), filter.getEndDate());
        List<TopProductResponse> listProduct = new ArrayList<TopProductResponse>();
        for (ProductDto productDto : resultList) {
            TopProductResponse topProduct = new TopProductResponse();
            topProduct.setId(productDto.getId());
            topProduct.setFullName(productDto.getName());
            topProduct.setNameC(productDto.getNameC());
            topProduct.setNameCa(productDto.getNameCa());
            topProduct.setNameDe(productDto.getNameDe());
            topProduct.setNameF(productDto.getNameF());
            topProduct.setNameM(productDto.getNameM());
            topProduct.setNameSl(productDto.getNameSl());
            topProduct.setQuantity(productDto.getQuantityOrder()+productDto.getQuantityExchange()-productDto.getQuantityReturn());
            topProduct.setTotal(productDto.getTotalOrder()+productDto.getTotalExchange()-productDto.getTotalReturn());
            topProduct.setProductImage(ObjectMapperUtils.mapAll(productImageRepository.findproductImageClientSearchByProductId(productDto.getId())
                            , ProductImageResponseClient.class));
            listProduct.add(topProduct);
        }
        Collections.sort(listProduct, new Comparator<TopProductResponse>() {
            @Override
            public int compare(TopProductResponse tp1, TopProductResponse tp2) {
                return tp2.getTotal().compareTo(tp1.getTotal());
            }
        });
        return listProduct;
    }
}
