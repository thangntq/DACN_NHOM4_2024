package com.ManShirtShop.service.product.impl;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.InvalidClassException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import com.ManShirtShop.dto.client.product.*;
import com.ManShirtShop.dto.client.product_detail_client.IProductDetailClientSearch;
import com.ManShirtShop.dto.client.product_detail_client.IProductImageSearch;
import com.ManShirtShop.repository.*;
import com.ManShirtShop.service.client.product_client.ProductFilterClientRequest;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.s;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ManShirtShop.common.genCode.GenCode;
import com.ManShirtShop.common.mapperUtil.ObjectMapperUtils;
import com.ManShirtShop.common.mapperUtil.ResponseFormat;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailRequest;
import com.ManShirtShop.dto.product.ProductDiscountResponese2;
import com.ManShirtShop.dto.product.ProductFilterRequest;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;
import com.ManShirtShop.dto.client.product_detail_client.IProductFullResponseClient;
import com.ManShirtShop.dto.client.product_detail_client.ProductFullResponseClient;
import com.ManShirtShop.dto.product.ProductReponse;
import com.ManShirtShop.dto.product.ProductRequest;
import com.ManShirtShop.dto.product_Detail_Image_Dto.ProductAllRequest;
import com.ManShirtShop.dto.product_Image_dto.ProductImageRequest;
import com.ManShirtShop.dto.product_Image_dto.Status;
import com.ManShirtShop.entities.Category;
import com.ManShirtShop.entities.Collar;
import com.ManShirtShop.entities.Color;
import com.ManShirtShop.entities.Design;
import com.ManShirtShop.entities.Discount;
import com.ManShirtShop.entities.Form;
import com.ManShirtShop.entities.Material;
import com.ManShirtShop.entities.Product;
import com.ManShirtShop.entities.ProductDetail;
import com.ManShirtShop.entities.ProductImage;
import com.ManShirtShop.entities.Size;
import com.ManShirtShop.entities.Sleeve;
import com.ManShirtShop.service.discount.DiscountService;
import com.ManShirtShop.service.product.ProductService;
import com.ManShirtShop.service.productDetail.ProductDetailService;
import com.ManShirtShop.service.product_Image.ProductImageService;
import com.ManShirtShop.service.product_discount.ProductDiscountService;
import com.ManShirtShop.service.uploadImage.ImageUploadService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.joran.conditional.IfAction;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.MaskFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    ColorRepository colorRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductImageRepository productImageRepository;

    @Autowired
    DesignRepository designRepository;

    @Autowired
    FormRepository formRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    SleeveRepository sleeveRepository;

    @Autowired
    CollarRepository collarRepository;

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    ProductDetailService productDetailService;

    @Autowired
    ProductImageService productImageService;

    @Autowired
    ResponseFormat responseFormat;

    @Autowired
    ImageUploadService imageUploadService;

    @Autowired
    SizeRepository sizeRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    ProductDiscountService productDiscountService;

    @Autowired
    OderRepository oderRepository;

    public Boolean checkIdProduct(Integer id, Boolean checkDB) {
        if (id <= 0 || id == null) {
            return true;
        }
        if (!checkDB) { // check id db
            return true;
        }
        return false;
    }

    @Override
    @Cacheable("product2")
    public List<ProductReponse> getAll() {
        List<Product> getAll = productRepository.getAllByStatus();
        List<ProductReponse> lst = ObjectMapperUtils.mapAll(getAll, ProductReponse.class);
        for (ProductReponse x : lst) {
            Integer total = 0;
            for (com.ManShirtShop.dto.product.ProductDetailResponse y : x.getProductDetail()) {
                total = y.getQuantity() + total;
            }
            x.setTotal(total);
        }
        return lst;
    }

    @Override
    @CacheEvict(value = {"product", "userclient", "product2"}, allEntries = true)
    public ProductReponse create(ProductRequest requet) {
        requet.setId(-1);
        Product entity = checkAndReturnProduct(requet);
        if (entity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dữ liệu không hợp lệ", null);
        }
        entity.setCreateBy("admin");
        entity.setCreateTime(Timestamp.from(Instant.now()));
        entity.setStatus(0);
        entity = productRepository.save(entity);
        entity.setCode(GenCode.codeProduct(entity.getId()));
        entity = productRepository.save(entity);
        return ObjectMapperUtils.map(entity, ProductReponse.class);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "userclient", "product2"}, allEntries = true)
    public ProductReponse update(ProductAllRequest requet) {
        try {
            Product product = checkAllAndReturnProduct(requet);
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dữ liệu không hợp lệ", null);
            }
            if (product.getId() == null || product.getId() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy sản phẩm", null);
            }
            if (!productRepository.existsById(product.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy sản phẩm", null);
            }
            Product productOld = productRepository.findById(requet.getId()).get();
            product.setCreateBy(productOld.getCreateBy());
            product.setCreateTime(productOld.getCreateTime());
            product.setStatus(productOld.getStatus());
            product.setUpdateTime(Timestamp.from(Instant.now()));
            product.setUpdateBy("admin");
            product.setCode(productOld.getCode());
            System.out.println("cate--------ccc: " + product.getCategory().getId());
            product = productRepository.save(product);
            List<ProductImage> lstProductImages = new ArrayList<>();
            if (requet.getProductImage() != null) {
                for (ProductImageRequest x : requet.getProductImage()) {
                    if (x.getId() <= 0 || x.getId() == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy ảnh", null);
                    }
                    if (!productImageRepository.existsById(x.getId())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy ảnh", null);
                    }
                    ProductImage entity = productImageRepository.findById(x.getId()).get();
                    entity.setCreateBy(entity.getCreateBy());
                    entity.setCreateTime(entity.getCreateTime());
                    entity.setUpdateTime(Timestamp.from(Instant.now()));
                    entity.setUpdateBy("admin");
                    entity.setUrlImage(x.getUrlImage());
                    entity.setMainImage(x.getMainImage());
                    entity.setStatus(entity.getStatus());
                    entity.setColor(entity.getColor());
                    entity.setProduct(product);
                    lstProductImages.add(entity);
                }
                productImageRepository.saveAll(lstProductImages);
            }
            return ObjectMapperUtils.map(product, ProductReponse.class);
        } catch (Exception e) {
            logger.error(e.toString());
            responseFormat.response(HttpServletResponse.SC_BAD_REQUEST, null, "Dữ liệu không hợp lệ");
            throw new RuntimeException("Failed to update product", e);
        }
    }

    public Product checkAndReturnProduct(ProductRequest requet) {
        if (checkIdProduct(requet.getCategory(), categoryRepository.existsById(requet.getCategory()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cate null", null);
        } // check cate
        if (checkIdProduct(requet.getMaterial(), materialRepository.existsById(requet.getMaterial()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mate null", null);
        } // check mate
        if (checkIdProduct(requet.getDesign(), designRepository.existsById(requet.getDesign()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Degin null", null);
        } // check degin
        if (checkIdProduct(requet.getForm(), formRepository.existsById(requet.getForm()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Form null", null);
        } // check form
        if (checkIdProduct(requet.getSleeve(), sleeveRepository.existsById(requet.getSleeve()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sleeve null", null);
        } // check sleeve
        if (checkIdProduct(requet.getCollar(), collarRepository.existsById(requet.getCollar()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Collar null", null);
        } // check collar
        Product entity = ObjectMapperUtils.map(requet, Product.class);
        // if (requet.getDiscount() <= 0 || requet.getDiscount() == null) {
        // // entity.setDiscount(null);
        // } else if (!designRepository.existsById(requet.getDiscount())) {
        // return null;
        // } else {
        // Discount discount = new Discount();
        // discount.setId(requet.getDiscount());
        // // entity.setDiscount(discount);
        // }

        Category category = new Category();
        category.setId(requet.getCategory());
        entity.setCategory(category);

        Material material = new Material();
        material.setId(requet.getMaterial());
        entity.setMaterial(material);

        Design design = new Design();
        design.setId(requet.getDesign());
        entity.setDesign(design);

        Form form = new Form();
        form.setId(requet.getForm());
        entity.setForm(form);

        Sleeve sleeve = new Sleeve();
        sleeve.setId(requet.getSleeve());
        entity.setSleeve(sleeve);

        Collar collar = new Collar();
        collar.setId(requet.getCollar());
        entity.setCollar(collar);

        return entity;
    }

    public Product checkAllAndReturnProduct(ProductAllRequest requet) {
        if (checkIdProduct(requet.getCategory(), categoryRepository.existsById(requet.getCategory()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cate null", null);
        } // check cate
        if (checkIdProduct(requet.getMaterial(), materialRepository.existsById(requet.getMaterial()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mate null", null);
        } // check mate
        if (checkIdProduct(requet.getDesign(), designRepository.existsById(requet.getDesign()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Degin null", null);
        } // check degin
        if (checkIdProduct(requet.getForm(), formRepository.existsById(requet.getForm()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Form null", null);
        } // check form
        if (checkIdProduct(requet.getSleeve(), sleeveRepository.existsById(requet.getSleeve()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sleeve null", null);
        } // check sleeve
        if (checkIdProduct(requet.getCollar(), collarRepository.existsById(requet.getCollar()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Collar null", null);
        } // check collar
        Product entity = ObjectMapperUtils.map(requet, Product.class);
        // if (requet.getDiscount() <= 0 || requet.getDiscount() == null) {
        // entity.setDiscount(null);
        // } else if (!designRepository.existsById(requet.getDiscount())) {
        // return null;
        // } else {
        // Discount discount = new Discount();
        // discount.setId(requet.getDiscount());
        // entity.setDiscount(discount);
        // }
        // entity.setDiscount(null);
        Category category = new Category();
        category.setId(requet.getCategory());
        entity.setCategory(category);

        Material material = new Material();
        material.setId(requet.getMaterial());
        entity.setMaterial(material);

        Design design = new Design();
        design.setId(requet.getDesign());
        entity.setDesign(design);

        Form form = new Form();
        form.setId(requet.getForm());
        entity.setForm(form);

        Sleeve sleeve = new Sleeve();
        sleeve.setId(requet.getSleeve());
        entity.setSleeve(sleeve);

        Collar collar = new Collar();
        collar.setId(requet.getCollar());
        entity.setCollar(collar);
        System.out.println("----------------" + entity.toString());
        return entity;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "userclient", "product2"}, allEntries = true) // ngưng hoạt động
    public Map<String, Object> delete(Integer id) {
        if (checkIdProduct(id, productRepository.existsById(id))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy sản phẩm", null);
        }
        Map<String, Object> response = new HashMap<>();
        try {
            productRepository.updateStatusProduct(1, id);
            response.put("status", true);
            response.put("data", "OK");
            response.put("message", "Thành Công!!!");
            return response;
        } catch (Exception e) {
            response.put("status", false);
            response.put("data", "ERROR");
            response.put("message", "Không thể thay đổi trạng thái sản phẩm!!!");
            return response;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "userclient", "product2"}, allEntries = true) // ngưng hoạt động
    public Map<String, Object> hoatDong(Integer id) {
        if (checkIdProduct(id, productRepository.existsById(id))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy sản phẩm", null);
        }
        Map<String, Object> response = new HashMap<>();
        try {
            productRepository.updateStatusProduct(0, id);
            response.put("status", true);
            response.put("data", "OK");
            response.put("message", "Thành Công!!!");
            return response;
        } catch (Exception e) {
            response.put("status", false);
            response.put("data", "ERROR");
            response.put("message", "Không thể thay đổi trạng thái sản phẩm!!!");
            return response;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "userclient", "product2"}, allEntries = true) // ngưng hoạt động
    public Map<String, Object> xoaMem(Integer id) {
        if (checkIdProduct(id, productRepository.existsById(id))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy sản phẩm", null);
        }
        Map<String, Object> response = new HashMap<>();
        List<Integer> lst = oderRepository.checkStatusOrderByIdProduct(id);
        if (!(lst == null) || (lst.isEmpty())) {
            response.put("status", false);
            response.put("data", "ERROR");
            response.put("message", "Sản phẩm vẫn còn tồn tại trong hoá đơn chưa được xử lý!!!");
            return response;
        }
        response.put("status", true);
        response.put("data", "OK");
        response.put("message", "Update Thành Công");
        productRepository.updateStatusProduct(2, id);
        return response;
    }

    @Override
    public ProductReponse findById(Integer id) {
        if (checkIdProduct(id, productRepository.existsById(id))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy chi tiết sản phẩm", null);
        }
        Product entity = productRepository.findById(id).get();
        Integer total = 0;
        for (ProductDetail x : entity.getProductDetail()) {
            total = x.getQuantity() + total;
        }
        ProductReponse response = ObjectMapperUtils.map(entity, ProductReponse.class);
        return response;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "userclient", "product2"}, allEntries = true)
    public ProductReponse createProductDetailImage(ProductAllRequest request) {
        try {
            Product product = checkAllAndReturnProduct(request);
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dữ liệu không hợp lệ", null);
            }
            product.setCreateBy("admin");
            product.setCreateTime(Timestamp.from(Instant.now()));
            product.setStatus(1);
            product.setProductDetail(null);
            product.setProductImage(null);
            System.out.println("cate--------ccc: " + product.getCategory().getId());
            product = productRepository.save(product);
            product.setCode("SP" + GenCode.codeProduct(product.getId()));

            List<ProductDetail> productDetails = new ArrayList<>();
            for (ProductDetailRequest x : request.getProductDetail()) {
                Color c = colorRepository.findById(x.getColor()).get();
                if (c.getId() != x.getColor()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid color", null);
                }
                Size size = sizeRepository.findById(x.getSize()).get();
                ProductDetail eDetail = new ProductDetail();
                eDetail.setCreateTime(Timestamp.from(Instant.now()));
                eDetail.setCreateBy("admin");
                eDetail.setProduct(product);
                eDetail.setColor(c);
                eDetail.setSize(size);
                eDetail.setBarCode("");
                eDetail.setQuantity(x.getQuantity());
                eDetail.setStatus(0);
                productDetails.add(eDetail);
            }
            productDetailRepository.saveAll(productDetails);
            List<ProductDetail> newProductDetail = new ArrayList<>();
            for (ProductDetail x : productDetails) {// Create Barcode product detail
                x.setBarCode(createBarCode(x.getId()));
                newProductDetail.add(x);
            }
            productDetailRepository.saveAll(newProductDetail);
            List<ProductImage> lstProductImages = new ArrayList<>();
            for (ProductImageRequest x : request.getProductImage()) {
                ProductImage entity = new ProductImage();
                Color c = colorRepository.findById(x.getColorId()).get();
                if (c.getId() != x.getColorId()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid color", null);
                }
                entity.setUrlImage(x.getUrlImage());
                entity.setStatus(Status.STOCKING);
                entity.setCreateBy("admin");
                entity.setColor(c);
                entity.setMainImage(x.getMainImage());
                entity.setCreateTime(Timestamp.from(Instant.now()));
                entity.setProduct(product);
                lstProductImages.add(entity);
            }
            productImageRepository.saveAll(lstProductImages);
            return ObjectMapperUtils.map(product, ProductReponse.class);
        } catch (Exception e) {
            logger.error(e.toString());
            responseFormat.response(HttpServletResponse.SC_BAD_REQUEST, null, "Dữ liệu không hợp lệ");
            throw new RuntimeException("Failed to create product", e);
        }
    }

    @Override
    @Cacheable("product")
    public List<ProductReponse> getAllByFilter(ProductFilterRequest filter) {
        productDiscountService.getAllByDiscount();
        Query queries = createQuery(filter);
        List<Product> getAllByFilter = queries.getResultList();
        List<ProductReponse> productReponses = ObjectMapperUtils.mapAll(getAllByFilter, ProductReponse.class);
        for (ProductReponse productReponse : productReponses) {
            List<ProductDiscountResponese2> productDiscountToKeep = new ArrayList<>();
            for (ProductDiscountResponese2 pd : productReponse.getProductDiscount()) {
                if (pd.getStatus() != 1) {
                    productDiscountToKeep.add(pd);
                }
            }
            productReponse.setProductDiscount(productDiscountToKeep);
            Integer total = 0;
            for (com.ManShirtShop.dto.product.ProductDetailResponse y : productReponse.getProductDetail()) {
                total = y.getQuantity() + total;
            }
            productReponse.setTotal(total);
        }
        return productReponses;
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
        return x;
    }

    // ---------------------------------------CLI------------------------------------------------
    @Override
    public List<ProductResponseClient> fillterProductClient(ProductFilterClientRequest productFilterClientRequest) {
        List<SearchProductClient> lst = productRepository.getAllClients(productFilterClientRequest);
        List<ProductResponseClient> prdCli = ObjectMapperUtils.mapAll(lst, ProductResponseClient.class);
        for (ProductResponseClient x : prdCli) {
            x.setProductDetail(productDetailRepository.findProductDetailClientByProductId(x.getId()).stream().map(
                    detail -> mapDetailSerch(detail)).toList());
            x.setProductImage(productImageRepository.findproductImageClientSearchByProductId(x.getId())
                    .stream().map(iamge -> mapPrdImageSearch(iamge)).toList()
            );
        }
        return prdCli;
    }

    public ProductDetailResponseClient  mapDetailSerch(IProductDetailClientSearch i){
        ProductDetailResponseClient prdDetail = new ProductDetailResponseClient();
        prdDetail.setId(i.getId());
        prdDetail.setProductId(i.getProduct_id());
        prdDetail.setStatus(i.getStatus());
        prdDetail.setBarCode(i.getBar_code());
        prdDetail.setQuantity(i.getQuantity());
        prdDetail.setSize(new SizeResponseClient(i.getSizeId(),i.getCode(),0));
        prdDetail.setColor(new ColorResponseClient(i.getColorId(),i.getName()));
        return prdDetail;
    }

    public ProductImageResponseClient mapPrdImageSearch(IProductImageSearch i){
        ProductImageResponseClient prd = new ProductImageResponseClient();
        prd.setId(i.getId());
        prd.setMainImage(i.getMain_image());
        prd.setUrlImage(i.getUrl_image());
        prd.setStatus(i.getStatus());
        prd.setProductId(i.getProduct_id());
        prd.setColor(new ColorResponseClient(i.getColorId(),i.getName()));
        return prd;
    }


    @Override
    public List<ProductResponseClient> getTopProduct() {
        List<IProductResponseClient> lst = productRepository.getTopProduct();
        List<ProductResponseClient> prdCli = ObjectMapperUtils.mapAll(lst, ProductResponseClient.class);
        // List<Integer> listIDPrd = new ArrayList<>();
        for (ProductResponseClient x : prdCli) {
            x.setProductDetail(productDetailRepository.findProductDetailClientByProductId(x.getId()).stream().map(
                    detail -> mapDetailSerch(detail)).toList());
            x.setProductImage(productImageRepository.findproductImageClientSearchByProductId(x.getId())
                    .stream().map(iamge -> mapPrdImageSearch(iamge)).toList()
            );
        }
        return prdCli;
    }

    @Override
    public List<ProductResponseClient> getTopNewProduct() {
        List<IProductResponseClient> lst = productRepository.getTopNewProduct();
        List<ProductResponseClient> prdCli = ObjectMapperUtils.mapAll(lst, ProductResponseClient.class);
        for (ProductResponseClient x : prdCli) {
            x.setProductDetail(productDetailRepository.findProductDetailClientByProductId(x.getId()).stream().map(
                    detail -> mapDetailSerch(detail)).toList());
            x.setProductImage(productImageRepository.findproductImageClientSearchByProductId(x.getId())
                    .stream().map(iamge -> mapPrdImageSearch(iamge)).toList()
            );
        }
        return prdCli;
    }

    @Override
    public List<ProductResponseClient> getTopDiscountProduct() {
        List<IProductResponseClient> lst = productRepository.getTopDiscountProduct();
        List<ProductResponseClient> prdCli = ObjectMapperUtils.mapAll(lst, ProductResponseClient.class);
        for (ProductResponseClient x : prdCli) {
            x.setProductDetail(productDetailRepository.findProductDetailClientByProductId(x.getId()).stream().map(
                    detail -> mapDetailSerch(detail)).toList());
            x.setProductImage(productImageRepository.findproductImageClientSearchByProductId(x.getId())
                    .stream().map(iamge -> mapPrdImageSearch(iamge)).toList()
            );
        }
        return prdCli;
    }

    @Override
    public List<ProductResponseClient> getProductClientByDiscountId(String name) {
        List<IProductResponseClient> lst = productRepository.getProductClientByDiscountId(name);
        List<ProductResponseClient> prdCli = ObjectMapperUtils.mapAll(lst, ProductResponseClient.class);
        for (ProductResponseClient x : prdCli) {
            x.setProductDetail(productDetailRepository.findProductDetailClientByProductId(x.getId()).stream().map(
                    detail -> mapDetailSerch(detail)).toList());
            x.setProductImage(productImageRepository.findproductImageClientSearchByProductId(x.getId())
                    .stream().map(iamge -> mapPrdImageSearch(iamge)).toList()
            );
        }
        return prdCli;
    }

    @Override
    @Cacheable("userclient")
    public List<ProductResponseClient> getAllClient() {
        // Pageable first10Page = PageRequest.of(0, 10);
        List<IProductResponseClient> lst = productRepository.findProductWithCodeNamePriceAndImages();
        List<ProductResponseClient> prdCli = ObjectMapperUtils.mapAll(lst, ProductResponseClient.class);
        // List<Integer> listIDPrd = new ArrayList<>();
        for (ProductResponseClient x : prdCli) {
            x.setProductDetail(productDetailRepository.findProductDetailClientByProductId(x.getId()).stream().map(
                    detail -> mapDetailSerch(detail)).toList());
            x.setProductImage(productImageRepository.findproductImageClientSearchByProductId(x.getId())
                    .stream().map(iamge -> mapPrdImageSearch(iamge)).toList()
            );
        }
        return prdCli;
    }

    @Override
    public ProductFullResponseClient getProductClientById(Integer id) {
        IProductFullResponseClient prdDB = productRepository.getOneProductClient(id);
        ProductFullResponseClient prd = ObjectMapperUtils.map(prdDB, ProductFullResponseClient.class);
        prd.setProductImage(ObjectMapperUtils.mapAll(productImageRepository.findByProductId(prd.getId()),
                ProductImageResponseClient.class));
        prd.setProductDetail(ObjectMapperUtils.mapAll(productDetailRepository.findByProductId(prd.getId()),
                ProductDetailResponseClient.class));
        return prd;
    }

    // public static void main(String[] args) {
    // ProductServiceImpl a = new ProductServiceImpl();
    // a.createBarCode(12);
    // }
    private Query createQuery(ProductFilterRequest filter) {
        StringBuilder whereClause = createWhereClause(filter);
        String orderByClause = "group by p.id ORDER BY p.createTime DESC";
        String sql = "Select p FROM Product p \n" + //  
                "Left JOIN p.productDiscount pd\n" + //
                "left JOIN pd.discountId d \n" + //
                "left JOIN p.productDetail pdd WHERE (1 = 1)" + whereClause + orderByClause;
        Query queries = entityManager.createQuery(sql, Product.class);
        createParameter(queries, filter);
        return queries;
    }

    private StringBuilder createWhereClause(ProductFilterRequest filter) {
        StringBuilder whereClause = new StringBuilder();
        if (filter.getCategory() != null) {
            whereClause.append(" AND ( p.category.id in (:category) )");
        }
        if (filter.getCollar() != null) {
            whereClause.append(" AND ( p.collar.id in (:collar) )");
        }
        if (filter.getDesign() != null) {
            whereClause.append(" AND ( p.design.id in (:design) )");
        }
        if (filter.getForm() != null) {
            whereClause.append(" AND ( p.form.id in (:form) )");
        }
        if (filter.getMaterial() != null) {
            whereClause.append(" AND ( p.material.id in (:material) )");
        }
        if (filter.getSleeve() != null) {
            whereClause.append(" AND ( p.sleeve.id in (:sleeve) )");
        }
        if (filter.getSize() != null) {
            whereClause.append(" AND ( pdd.size.id in (:size) )");
        }
        if (filter.getColor() != null) {
            whereClause.append(" AND ( pdd.color.id in (:color) )");
        }
        if (filter.getStatus() != null) {
            whereClause.append(" AND ( p.status = :status )");
        }
        if (filter.getDiscount() != null) {
            whereClause.append(" AND ( d.id = :discount ) AND (DATE(d.endDate) >= DATE(now())) AND (DATE(d.startDate) <= DATE(now()))");
        }
        if (filter.getLow() != null && filter.getHigh() != null) {
            whereClause.append(" AND ( p.price >= :low and p.price <= :high )");
        }
        return whereClause;
    }

    private void createParameter(Query queries, ProductFilterRequest filter) {
        if (filter.getCategory() != null) {
            setValueToParam(queries, "category", filter.getCategory());
        }
        if (filter.getColor() != null) {
            setValueToParam(queries, "color", filter.getColor());
        }
        if (filter.getCollar() != null) {
            setValueToParam(queries, "collar", filter.getCollar());
        }
        if (filter.getDesign() != null) {
            setValueToParam(queries, "design", filter.getDesign());
        }
        if (filter.getForm() != null) {
            setValueToParam(queries, "form", filter.getForm());
        }
        if (filter.getMaterial() != null) {
            setValueToParam(queries, "material", filter.getMaterial());
        }
        if (filter.getSleeve() != null) {
            setValueToParam(queries, "sleeve", filter.getSleeve());
        }
        if (filter.getSize() != null) {
            setValueToParam(queries, "size", filter.getSize());
        }
        if (filter.getStatus() != null) {
            setValueToParamI(queries, "status", filter.getStatus());
        }
        if (filter.getDiscount() != null) {
            setValueToParamI(queries, "discount", filter.getDiscount());
        }
        if (filter.getLow() != null && filter.getHigh() != null) {
            setValueToParamD(queries, "low", filter.getLow());
            setValueToParamD(queries, "high", filter.getHigh());
        }
    }

    private void setValueToParam(Query queries, String param, List<Integer> values) {

        queries.setParameter(param, values);


    }

    private void setValueToParamI(Query queries, String param, Integer value) {

        queries.setParameter(param, value);


    }

    private void setValueToParamD(Query queries, String param, Double value) {

        queries.setParameter(param, value);

    }

    @Override
    public List<ProductReponse> getAllByDiscount() {
        List<Product> getAll = productRepository.getAllByStatus();
        List<ProductReponse> lst = ObjectMapperUtils.mapAll(getAll, ProductReponse.class);
        return lst;
    }

}
