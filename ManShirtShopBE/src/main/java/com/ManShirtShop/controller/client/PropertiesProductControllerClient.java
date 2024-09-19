package com.ManShirtShop.controller.client;

import com.ManShirtShop.controller.ReturnController;
import com.ManShirtShop.dto.Collar_Dto.CollarResponse;
import com.ManShirtShop.dto.Color_Dto.ColorResponse;
import com.ManShirtShop.dto.OriginalCategory_Dto.OriginalCategoryResponse;
import com.ManShirtShop.dto.category.CategoryResponse;
import com.ManShirtShop.dto.contact.ContactResponse;
import com.ManShirtShop.dto.design.DesignResponse;
import com.ManShirtShop.dto.discount.DiscountResponse;
import com.ManShirtShop.dto.form.FormResponse;
import com.ManShirtShop.dto.material_Dto.ResponseMaterial;
import com.ManShirtShop.dto.size_dto.SizeResponse;
import com.ManShirtShop.dto.sleeve.SleeveResponse;
import com.ManShirtShop.dto.voucher.VoucherResponse;
import com.ManShirtShop.entities.Return;
import com.ManShirtShop.repository.ProductRepository;
import com.ManShirtShop.repository.ReturnRepository;
import com.ManShirtShop.service.category.CategoryService;
import com.ManShirtShop.service.client.product_client.ProductFilterClientRequest;
import com.ManShirtShop.service.collar.CollarService;
import com.ManShirtShop.service.color.ColorService;
import com.ManShirtShop.service.contact.ContactService;
import com.ManShirtShop.service.design.DesignService;
import com.ManShirtShop.service.discount.DiscountService;
import com.ManShirtShop.service.form.FormService;
import com.ManShirtShop.service.material.MaterialService;
import com.ManShirtShop.service.originalCategory.OriginalCategoryService;
import com.ManShirtShop.service.product.ProductService;
import com.ManShirtShop.service.size.SizeService;
import com.ManShirtShop.service.sleeve.SleeveService;
import com.ManShirtShop.service.voucher.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "client/api/properties")
@Tag(name = "Properties Product Client api")
public class PropertiesProductControllerClient {

    @Autowired
    ContactService contactService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CollarService collarService;

    @Autowired
    ColorService colorService;

    @Autowired
    DesignService designService;

    @Autowired
    DiscountService discountService;

    @Autowired
    FormService formService;

    @Autowired
    MaterialService materialService;

    @Autowired
    OriginalCategoryService originalCategoryService;

    @Autowired
    SizeService sizeService;

    @Autowired
    SleeveService sleeveService;
    @Autowired
    VoucherService voucherService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;


    @GetMapping(value = "findAllCollar")
    public ResponseEntity<List<CollarResponse>> getALlCollar() {
        return ResponseEntity.ok().body(collarService.getAll());
    }

    @GetMapping(value = "findAllColor")
    public ResponseEntity<List<ColorResponse>> getALlColor() {
        return ResponseEntity.ok().body(colorService.getAll());
    }

    @Operation(summary = "Lấy ra danh sách Contact có status = 0")
    @GetMapping(value = "findAllContact")
    public ResponseEntity<List<ContactResponse>> getAllContact() {
        return ResponseEntity.ok(contactService.getAll());
    }

    @GetMapping(value = "findAllDegin")
    public ResponseEntity<List<DesignResponse>> getALlDegin() {
        return ResponseEntity.ok().body(designService.getAll());
    }

    @Operation(summary = "Lấy ra danh sách discount có stt = 0")
    @GetMapping(value = "findAllDiscount")
    public ResponseEntity<List<DiscountResponse>> getALlDiscount() {
        return ResponseEntity.ok().body(discountService.getAll());
    }

    @Operation(summary = "Lấy ra discount theo id")
    @GetMapping(value = "findByIdDiscount")
    public ResponseEntity<?> findByIdDiscount(@RequestParam Integer id) {
        return discountService.findById(id);
    }

    @GetMapping(value = "findAllForm")
    public ResponseEntity<List<FormResponse>> getALlForm() {
        return ResponseEntity.ok().body(formService.getAll());
    }

    @GetMapping(value = "findAllMateria")
    public ResponseEntity<List<ResponseMaterial>> getALlMateria() {
        return ResponseEntity.ok().body(materialService.getAll());
    }

    @GetMapping(value = "findAllOriginalCategory")
    public ResponseEntity<List<OriginalCategoryResponse>> getALlOriginalCategory() {
        return ResponseEntity.ok().body(originalCategoryService.getAll());
    }

    @GetMapping(value = "findAllSize")
    public ResponseEntity<List<SizeResponse>> getALlSize() {
        return ResponseEntity.ok().body(sizeService.getAll());
    }

    @GetMapping(value = "findAllSleeve")
    public ResponseEntity<List<SleeveResponse>> getALlSleeve() {
        return ResponseEntity.ok().body(sleeveService.getAll());
    }

    @Operation(summary = "Lấy ra danh sách Voucher có status = 0")
    @GetMapping(value = "findAllVoucher")
    public ResponseEntity<List<VoucherResponse>> getALlVoucher() {
        return ResponseEntity.ok().body(voucherService.getAll());
    }

    @Operation(summary = "Lấy ra discount theo name")
    @GetMapping(value = "findByNamesDiscount")
    public ResponseEntity<?> findByIdDiscount(@RequestParam String name) {
        return discountService.findByName(name);
    }


}


















































