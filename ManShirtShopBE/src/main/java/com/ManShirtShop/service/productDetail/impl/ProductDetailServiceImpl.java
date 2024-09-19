package com.ManShirtShop.service.productDetail.impl;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.*;
import java.sql.Timestamp;
import java.time.Instant;

import com.ManShirtShop.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailRequest;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;
import com.ManShirtShop.dto.Statistic.ProductDetail.ProductDetailDto;
import com.ManShirtShop.dto.product_Image_dto.ProductImageRequest;
import com.ManShirtShop.entities.Color;
import com.ManShirtShop.entities.Product;
import com.ManShirtShop.entities.ProductDetail;
import com.ManShirtShop.entities.ProductImage;
import com.ManShirtShop.entities.Size;
import com.ManShirtShop.service.product.ProductService;
import com.ManShirtShop.service.product.impl.ProductServiceImpl;
import com.ManShirtShop.service.productDetail.ProductDetailService;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductImageRepository productImageRepository;

    @Autowired
    OderRepository oderRepository;

    public Boolean checkId(Integer id, Boolean checkDB) {
        if (id <= 0 || id == null) {
            return true;
        }
        if (!checkDB) { // check id db
            return true;
        }
        return false;
    }

    public List<ProductDetail> checkAndReturnListProductDetail(List<ProductDetailRequest> requet) {
        List<ProductDetail> list = new ArrayList<ProductDetail>();
        for (ProductDetailRequest entity : requet) {
            // entity.setId(0);
            if (checkId(entity.getProduct(), productRepository.existsById(entity.getProduct()))) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Sản phẩm", null);
            }
            if (checkId(entity.getColor(), colorRepository.existsById(entity.getColor()))) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy color", null);
            } // check color
            if (checkId(entity.getSize(), sizeRepository.existsById(entity.getSize()))) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy size ", null);
            } // check size
            ProductDetail entityDetail = ObjectMapperUtils.map(requet, ProductDetail.class);

            Product product = new Product();
            product.setId(entity.getProduct());
            entityDetail.setProduct(product);

            Size size = new Size();
            size.setId(entity.getSize());
            entityDetail.setSize(size);

            Color color = new Color();
            color.setId(entity.getColor());
            entityDetail.setColor(color);
            entityDetail.setQuantity(entity.getQuantity());
            entityDetail.setBarCode("abcd");
            list.add(entityDetail);
        }

        return list;
    }

    public ProductDetail checkAndReturnProductDetail(ProductDetailRequest requet) {
        if (checkId(requet.getProduct(), productRepository.existsById(requet.getProduct()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Sản phẩm ", null);
        }
        if (checkId(requet.getColor(), colorRepository.existsById(requet.getColor()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy color ", null);
        } // check color
        if (checkId(requet.getSize(), sizeRepository.existsById(requet.getSize()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Sản phẩm size ", null);
        } // check size
        ProductDetail entityDetail = ObjectMapperUtils.map(requet, ProductDetail.class);

        Product product = new Product();
        product.setId(requet.getProduct());
        entityDetail.setProduct(product);

        Size size = new Size();
        size.setId(requet.getSize());
        entityDetail.setSize(size);

        Color color = new Color();
        color.setId(requet.getColor());
        entityDetail.setColor(color);

        return entityDetail;
    }

    @Override
    public List<ProductDetailResponse> getAll() {
        List<ProductDetail> getAll = productDetailRepository.getAllByStatus();
        return ObjectMapperUtils.mapAll(getAll, ProductDetailResponse.class);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "userclient", "product2"}, allEntries = true)
    public List<ProductDetailResponse> saveAll(List<ProductDetailRequest> request) {
        List<ProductDetail> list = checkAndReturnListProductDetail(request);
        for (ProductDetail entityDetail : list) {
            entityDetail.setCreateBy("admin");
            entityDetail.setCreateTime(Timestamp.from(Instant.now()));
            System.out.println(entityDetail.getQuantity());
        }
        list = productDetailRepository.saveAll(list);
        for (ProductDetail x : list) {
            x.setBarCode(createBarCode(x.getId()));
        }
        list = productDetailRepository.saveAll(list);
        List<ProductImage> lstProductImages = new ArrayList<>();
        Product product = list.get(0).getProduct();
        for (ProductDetailRequest requests : request) {
            if (requests.getLstProductImage() != null) {
                for (ProductImageRequest x : requests.getLstProductImage()) {
                    ProductImage eImage = new ProductImage();
                    Color c = colorRepository.findById(x.getColorId()).get();
                    if (c.getId() != x.getColorId()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid color", null);
                    }
                    eImage.setUrlImage(x.getUrlImage());
                    eImage.setStatus(0);
                    eImage.setCreateBy("admin");
                    eImage.setColor(c);
                    eImage.setCreateTime(Timestamp.from(Instant.now()));
                    eImage.setProduct(product);
                    lstProductImages.add(eImage);
                }
            }
        }
        if (!lstProductImages.isEmpty()) {
            productImageRepository.saveAll(lstProductImages);
        }
        List<ProductDetailResponse> responses = ObjectMapperUtils.mapAll(list, ProductDetailResponse.class);
        return responses;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "userclient", "product2"}, allEntries = true)
    public ProductDetailResponse Create(ProductDetailRequest request) {
        request.setId(0);
        ProductDetail entity = checkAndReturnProductDetail(request);
        ProductDetail check = productDetailRepository.findByProductAndColorAndSize(entity.getProduct(),
                entity.getColor(), entity.getSize());
        if (check != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm chi tiết đã tồn tại ", null);
        }
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        entity = productDetailRepository.save(entity);
        entity.setBarCode(createBarCode(entity.getId()));
        entity = productDetailRepository.save(entity);
        List<ProductImage> lstProductImages = new ArrayList<>();
        for (ProductImageRequest x : request.getLstProductImage()) {
            ProductImage eImage = new ProductImage();
            Color c = colorRepository.findById(x.getColorId()).get();
            if (c.getId() != x.getColorId()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid color", null);
            }
            eImage.setUrlImage(x.getUrlImage());
            eImage.setStatus(0);
            eImage.setCreateBy("admin");
            eImage.setColor(c);
            eImage.setCreateTime(Timestamp.from(Instant.now()));
            eImage.setProduct(entity.getProduct());
            lstProductImages.add(eImage);
        }
        productImageRepository.saveAll(lstProductImages);
        return ObjectMapperUtils.map(entity, ProductDetailResponse.class);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "userclient", "product2"}, allEntries = true)
    public ProductDetailResponse update(ProductDetailRequest request) {
        if (checkId(request.getId(), productDetailRepository.existsById(request.getId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Sản phẩm chi tiết ", null);
        }
        ProductDetail entityDB = productDetailRepository.findById(request.getId()).get();
        entityDB.setQuantity(request.getQuantity());
        entityDB.setUpdateBy("amdin");
        entityDB.setUpdateTime(Timestamp.from(Instant.now()));
        entityDB = productDetailRepository.save(entityDB);
        return ObjectMapperUtils.map(entityDB, ProductDetailResponse.class);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "userclient", "product2"}, allEntries = true)
    public Map<String, Object> delete(Integer id) {
        if (checkId(id, productDetailRepository.existsById(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Sản phẩm chi tiết ", null);
        }
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("data", "OK");
            response.put("message", "Update Thành Công");
            productDetailRepository.updateStatus(1, id);
            return response;
        } catch (Exception e) {
            response.put("status", false);
            response.put("data", "ERROR");
            response.put("message", "Không thể update trạng thái sản phẩm chi tiết!!!");
            return response;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "userclient", "product2"}, allEntries = true)
    public Map<String, Object> updateHoatDong(Integer id) {
        if (checkId(id, productDetailRepository.existsById(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Sản phẩm chi tiết ", null);
        }
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("data", "OK");
            response.put("message", "Update Thành Công");
            productDetailRepository.updateStatus(0, id);
            return response;
        } catch (Exception e) {
            response.put("status", false);
            response.put("data", "ERROR");
            response.put("message", "Không thể update trạng thái sản phẩm chi tiết!!!");
            return response;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "userclient", "product2"}, allEntries = true)
    public Map<String, Object> updateXoa(Integer id) {
        if (checkId(id, productDetailRepository.existsById(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Sản phẩm chi tiết ", null);
        }
        Map<String, Object> response = new HashMap<>();
        List<Integer> lst = oderRepository.checkStatusOrderByIdProductDetail(id);
        if (!(lst == null) || !lst.isEmpty()) {
            response.put("status", false);
            response.put("data", "ERROR");
            response.put("message", "Sản phẩm chi tiết vẫn còn tồn tại trong hoá đơn chưa được xử lý!!!");
            return response;
        }
        response.put("status", true);
        response.put("data", "OK");
        response.put("message", "Update Thành Công");
        productDetailRepository.updateStatus(2, id);
        return response;
    }

    @Override
    public ProductDetailResponse findById(Integer id) {
        if (checkId(id, productDetailRepository.existsById(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Sản phẩm chi tiết ", null);
        }
        ProductDetail entity = productDetailRepository.findById(id).get();
        return ObjectMapperUtils.map(entity, ProductDetailResponse.class);
    }

    @Override
    public ProductDetailResponse findByBarcode(String barcode) {
        if (barcode == null || barcode.isBlank()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy barcode", null);
        }
        Optional<ProductDetail> entity = productDetailRepository.findByBarCode(barcode);
        if (!entity.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy barcode", null);
        }
        if (entity.get().getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Sản phẩm " + entity.get().getProduct().getName() + " " + entity.get().getColor().getName() + " "
                            + entity.get().getSize().getCode() + " Đã hết hàng", null);
        }
        return ObjectMapperUtils.map(entity, ProductDetailResponse.class);
    }

    public int getCharValue(char a) {
        return Character.getNumericValue(a);
    }

    public String createBarCode(Integer id) {// quy tắc:
        // https://vi.wikipedia.org/wiki/EAN-13#:~:text=V%C3%A9%2C%20phi%E1%BA%BFu-,Quy%20t%E1%BA%AFc%20t%C3%ADnh%20s%E1%BB%91%20ki%E1%BB%83m%20tra,8%2C10%2C12)
        final String MaQuocGia = "893";
        final String MaDoanhNghiep = "12345";
        String MaSanPham = null;
        if (id < 10) {
            MaSanPham = "000" + String.valueOf(id);
        } else if (id < 100) {
            MaSanPham = "00" + String.valueOf(id);
        } else if (id < 1000) {
            MaSanPham = "0" + String.valueOf(id);
        } else {
            MaSanPham = String.valueOf(id);
        }
        String ma = MaQuocGia + MaDoanhNghiep + MaSanPham;
        int sum = getCharValue(ma.charAt(0)) + getCharValue(ma.charAt(2)) + getCharValue(ma.charAt(4)) +
                getCharValue(ma.charAt(6)) + getCharValue(ma.charAt(8)) + getCharValue(ma.charAt(10));
        int sum2 = getCharValue(ma.charAt(1)) + getCharValue(ma.charAt(3)) + getCharValue(ma.charAt(5)) +
                getCharValue(ma.charAt(7)) + getCharValue(ma.charAt(9)) + getCharValue(ma.charAt(11));
        sum2 = sum2 * 3;
        sum2 = sum2 + sum;
        int sumfinal = sum2 % 10;
        if (sumfinal != 0) {
            sumfinal = 10 - sumfinal;
        }
        String x = ma + sumfinal;
        System.out.println(x);
        return x;
    }

    @Override
    public BufferedImage getBarcode(String barCodeRequest) {
        try {
            ProductDetailResponse productDetail = findByBarcode(barCodeRequest);
            String text = productDetail.getProduct().getName() + " - "
                    + productDetail.getColor().getName() + " - "
                    + productDetail.getSize().getCode();
            barCodeRequest = barCodeRequest.substring(0, barCodeRequest.length() - 1);
            // System.out.println(barCodeRequest);
            final Barcode barcode = BarcodeFactory
                    .createEAN13(barCodeRequest);
            barcode.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            barcode.setBarHeight(100);
            barcode.setBarWidth(4);
            BufferedImage image = BarcodeImageHandler.getImage(barcode);
            int totalTextLineToAdd = text.length();
            int newWidth = image.getWidth();
            int newHeight = image.getHeight() + 10 + totalTextLineToAdd;
            BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = newImage.getGraphics();
            graphics.setColor(java.awt.Color.WHITE);
            graphics.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
            graphics.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
            java.awt.Color textColor = java.awt.Color.BLACK;
            graphics.setColor(textColor);
            FontMetrics fontMetrics = graphics.getFontMetrics();
            int startingYPosition = image.getHeight() + 15;
            graphics.drawString(text, (newImage.getWidth() / 2) - (fontMetrics.stringWidth(text) / 2),
                    startingYPosition);
            image = newImage;
            return image;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @Override
    public com.ManShirtShop.dto.Statistic.ProductDetail.ProductDetail findById2(Integer id) {
        if (checkId(id, productDetailRepository.existsById(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Sản phẩm chi tiết ", null);
        }
        ProductDetail entity = productDetailRepository.findById(id).get();
        return ObjectMapperUtils.map(entity, com.ManShirtShop.dto.Statistic.ProductDetail.ProductDetail.class);
    }
}
