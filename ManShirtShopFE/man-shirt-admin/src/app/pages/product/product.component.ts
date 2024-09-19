import { Component, OnInit, ViewChild } from "@angular/core";
import { Options } from "ng5-slider";
import {
  animate,
  state,
  style,
  transition,
  trigger,
} from "@angular/animations";
import { Lightbox } from "ngx-lightbox";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { CategoryService } from "src/app/core/services/category.service";
import { categoryResponse } from "src/app/core/models/category.models";
import { DesignService } from "src/app/core/services/design.service";
import { design } from "src/app/core/models/design.models";
import { FormService } from "src/app/core/services/form.service";
import { form } from "src/app/core/models/form.models";
import { MaterialService } from "src/app/core/services/material.service";
import { material } from "src/app/core/models/material.models";
import { SleeveService } from "src/app/core/services/sleeve.service";
import { CollarService } from "src/app/core/services/collar.service";
import { sleeve } from "src/app/core/models/sleeve.models";
import { collar } from "src/app/core/models/collar.models";
import Swal from "sweetalert2";
import { ColorService } from "src/app/core/services/color.service";
import { color } from "src/app/core/models/color.models";
import { size } from "src/app/core/models/size.models";
import { SizeService } from "src/app/core/services/size.service";
import * as XLSX from "xlsx";
import { saveAs } from "file-saver";
import {
  FilterData,
  ProductRequest,
  ProductRespone,
} from "src/app/core/models/product.model";
import {
  ProductDetail,
  ProductDetailRequest,
  ProductDetailRespone,
  ProductImage,
} from "src/app/core/models/productDetail.model";
import {
  ProductImageRequest,
  ProductImageResponse,
} from "src/app/core/models/productImage.model";
import { discount } from "src/app/core/models/discount.models";
import { DiscountService } from "src/app/core/services/discount.service";
import { ProductService } from "src/app/core/services/product.service";
import { UploadService } from "src/app/core/services/upload.service";
import { ActivatedRoute, NavigationExtras, Router } from "@angular/router";
import { ExcelService } from "src/app/core/services/excel.service";
import { ToastrService } from "ngx-toastr";
import * as ClassicEditor from "@ckeditor/ckeditor5-build-classic";
@Component({
  selector: "app-product",
  templateUrl: "./product.component.html",
  styleUrls: ["./product.component.scss"],
  animations: [
    trigger("detailExpand", [
      state("collapsed", style({ height: "0px", minHeight: "0" })),
      state("expanded", style({ height: "*" })),
      transition(
        "expanded <=> collapsed",
        animate("225ms cubic-bezier(0.4, 0.0, 0.2, 1)")
      ),
    ]),
  ],
})
export class ProductComponent implements OnInit {
  public Editor = ClassicEditor;
  submittedDesign = false;
  submittedForm = false;
  submittedMaterial = false;
  submittedSleeve = false;
  submittedCollar = false;
  submittedColor = false;
  submittedSize = false;
  submittedDetail = false;

  design: design = {
    id: 0,
    name: "",
    createTime: undefined,
    updateTime: undefined,
    createBy: "",
    updateBy: "",
    status: 0,
  };
  formValue: form = {
    id: 0,
    name: "",
    createTime: undefined,
    updateTime: undefined,
    createBy: "",
    updateBy: "",
    status: 0,
  };
  material: material = {
    id: 0,
    name: "",
    createTime: undefined,
    updateTime: undefined,
    createBy: "",
    updateBy: "",
    status: 0,
  };
  sleeve: sleeve = {
    id: 0,
    name: "",
    diameter: 0,
    createTime: undefined,
    updateTime: undefined,
    createBy: "",
    updateBy: "",
    status: 0,
  };
  collar: collar = {
    id: 0,
    name: "",
    createTime: undefined,
    updateTime: undefined,
    createBy: "",
    updateBy: "",
    status: 0,
  };
  color: color = {
    id: 0,
    name: "",
    createTime: undefined,
    updateTime: undefined,
    createBy: "",
    updateBy: "",
    status: 0,
  };
  size: size = {
    id: 0,
    code: "",
    description: "",
    createTime: undefined,
    updateTime: undefined,
    createBy: "",
    updateBy: "",
    status: 0,
  };

  product: ProductRequest = {
    id: 0,
    name: "",
    price: 0,
    description: "",
    weight: 0,
    category: 0,
    design: 0,
    form: 0,
    material: 0,
    sleeve: 0,
    collar: 0,
    discount: 0,
    productDetail: [],
    productImage: [],
    status: 0,
  };
  productDetail: ProductDetail[] = [];
  formData: FormGroup;
  formDesign: FormGroup;
  formForm: FormGroup;
  formMaterial: FormGroup;
  formSleeve: FormGroup;
  formCollar: FormGroup;
  formColor: FormGroup;
  formSize: FormGroup;
  formProductDetail: FormGroup[];
  formDetail: FormGroup;

  modalRef: NgbModalRef;
  modalDesign: NgbModalRef;
  modalForm: NgbModalRef;
  modalMaterial: NgbModalRef;
  modalSleeve: NgbModalRef;
  modalCollar: NgbModalRef;
  modalColor: NgbModalRef;
  modalSize: NgbModalRef;
  modalImport: NgbModalRef;

  categoryDatas: categoryResponse[];
  designDatas: design[];
  formDatas: form[];
  materialDatas: material[];
  sleeveDatas: sleeve[];
  collarDatas: collar[];
  colorDatas: color[];
  sizeDatas: size[];
  discountDatas: discount[];

  @ViewChild("content") content: any;
  @ViewChild("designModal") designModal: any;
  @ViewChild("formModal") formModal: any;
  @ViewChild("materialModal") materialModal: any;
  @ViewChild("sleeveModal") sleeveModal: any;
  @ViewChild("collarModal") collarModal: any;
  @ViewChild("colorModal") colorModal: any;
  @ViewChild("sizeModal") sizeModal: any;
  @ViewChild("productDetail") productDetailModal: any;
  @ViewChild("importModal") importModal: any;

  isLoading = false;
  loading = false;
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 1;
  productListDatas: ProductRespone[];
  productListDatasPage: ProductRespone[];
  productEdit = false;
  productDetailCreate = false;
  breadCrumbItems: Array<{}>;
  pricevalue = 0;
  minVal: any;
  maxVal: any;
  term: any;
  statusCbc: any = [
    {
      id: 0,
      name: "Đang hoạt động",
    },
    {
      id: 1,
      name: "Chưa hoạt động",
    },
  ];
  priceoption: Options = {
    floor: 0,
    ceil: 3000000,
    translate: (value: number): string => {
      return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "đ";
    },
  };
  log = "";
  discountRates: number[] = [];
  fileName = "product.xlsx";

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private lightbox: Lightbox,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private categoryService: CategoryService,
    private designService: DesignService,
    private formService: FormService,
    private materialService: MaterialService,
    private sleeveService: SleeveService,
    private collarService: CollarService,
    private colorService: ColorService,
    private sizeService: SizeService,
    private discountService: DiscountService,
    private productService: ProductService,
    private uploadService: UploadService,
    private excelService: ExcelService,
    private toastr: ToastrService
  ) {
    this.route.queryParams.subscribe((params) => {
      if (params["category"]) {
        this.filterData.category = [];
        this.category = Number(params["category"]);
        this.filterData.category.push(this.category);
      }
      if (params["discount"]) {
        this.discount = Number(params["discount"]);
        this.filterData.discount = this.discount;
      }
    });

    for (let i = 0; i < this.products.length; i++) {
      this.formProductDetail[i] = this.formBuilder.group({
        ["id"]: [""],
        ["barCode"]: [""],
        ["quantity"]: ["", Validators.required],
      });
    }
    this.formDesign = this.formBuilder.group({
      name: ["", [Validators.required]],
    });
    this.formForm = this.formBuilder.group({
      name: ["", [Validators.required]],
    });
    this.formMaterial = this.formBuilder.group({
      name: ["", [Validators.required]],
    });
    this.formSleeve = this.formBuilder.group({
      name: ["", [Validators.required]],
      diameter: ["", [Validators.required]],
    });
    this.formCollar = this.formBuilder.group({
      name: ["", [Validators.required]],
    });
    this.formColor = this.formBuilder.group({
      name: ["", [Validators.required]],
    });
    this.formSize = this.formBuilder.group({
      code: ["", [Validators.required]],
      description: ["", [Validators.required]],
    });
  }

  ngOnInit() {
    this.breadCrumbItems = [
      { label: "Quản lý" },
      { label: "Sản phẩm", active: true },
    ];
    this.loadData();
    this.cbcCategory();
    this.cbcDesign();
    this.cbcForm();
    this.cbcMaterial();
    this.cbcSleve();
    this.cbcCollar();
    this.cbcColor();
    this.cbcSize();
    this.cbcDiscount();
  }
  category: number;
  discount: number;
  status: number = 0;
  public filterData: FilterData = {
    size: null,
    form: null,
    color: null,
    status: this.status,
    design: null,
    sleeve: null,
    collar: null,
    low: null,
    high: null,
    discount: null,
    category: null,
    material: null,
  };
  loadData() {
    this.loading = true;
    this.productListDatas = [];
    this.productListDatasPage = [];
    this.route.queryParams.subscribe((params) => {
      if (params["category"]) {
        this.filterData.category = [];
        this.category = Number(params["category"]);
        this.filterData.category.push(this.category);
      } else {
        this.filterData.category = null;
      }
      if (params["discount"]) {
        this.discount = Number(params["discount"]);
        this.filterData.discount = this.discount;
      } else {
        this.filterData.discount = null;
      }
      if (params["status"]) {
        this.status = Number(params["status"]);
        this.filterData.status = this.status;
      } else {
        this.filterData.status = 0;
      }
    });
    this.productService.getProductFilter(this.filterData).subscribe({
      next: (data) => {
        this.productListDatas = data;
        this.totalItems = data.length;
        this.productListDatasPage = data.slice(
          (this.currentPage - 1) * this.itemsPerPage,
          this.currentPage * this.itemsPerPage
        );
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
      },
    });
  }
  updateFilter(tags: any) {
    tags.page = null;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: tags,
      queryParamsHandling: "merge",
      skipLocationChange: false,
    });
    // .finally(() => {
    //   this.viewScroller.setOffset([120, 120]);
    //   this.viewScroller.scrollToAnchor("products"); // Anchore Link
    // });
  }
  applyFilter() {
    let queryParams = {
      category: null,
      discount: null,
      status: null,
    };
    if (this.category !== null) {
      queryParams.category = this.category;
    } else {
      queryParams.category = null;
    }
    if (this.status !== 0) {
      queryParams.status = this.status;
    } else {
      queryParams.status = null;
    }
    if (this.discount !== null) {
      queryParams.discount = this.discount;
    } else {
      queryParams.discount = null;
    }
    let navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      queryParams: queryParams,
      queryParamsHandling: "merge",
    };

    this.router.navigate([], navigationExtras);

    setTimeout(() => {
      this.loadData();
    }, 100);
  }
  clearFilter() {
    this.category = null;
    this.discount = null;
    this.status = 0;
    this.route.snapshot.queryParams;
    let navigationExtras: NavigationExtras = {
      queryParams: {},
    };
    this.router.navigate([], navigationExtras);
    setTimeout(() => {
      this.loadData();
    }, 100);
  }
  get form() {
    return this.formData.controls;
  }
  get formValueDesign() {
    return this.formDesign.controls;
  }
  get formValueForm() {
    return this.formForm.controls;
  }
  get formValueMaterial() {
    return this.formMaterial.controls;
  }
  get formValueSleeve() {
    return this.formSleeve.controls;
  }
  get formValueCollar() {
    return this.formCollar.controls;
  }
  get formValueColor() {
    return this.formColor.controls;
  }
  get formValueSize() {
    return this.formSize.controls;
  }
  get formValueDetail() {
    return this.formDetail.controls;
  }
  openModal() {
    this.formData = this.formBuilder.group({
      id: [""],
      name: ["", [Validators.required]],
      category: ["", [Validators.required]],
      discount: [""],
      description: ["", [Validators.required]],
      price: ["", [Validators.required]],
      weight: ["", [Validators.required]],
      design: ["", [Validators.required]],
      form: ["", [Validators.required]],
      material: ["", [Validators.required]],
      sleeve: ["", [Validators.required]],
      collar: ["", [Validators.required]],
      colors: ["", [Validators.required]],
      sizes: ["", [Validators.required]],
    });
    this.selectedColors = [];
    this.imagesByColor = [];
    // this.category.id = null;
    this.product = {
      id: 0,
      name: "",
      price: 0,
      description: "",
      weight: 0,
      category: 0,
      design: 0,
      form: 0,
      material: 0,
      sleeve: 0,
      collar: 0,
      discount: 0,
      productDetail: [],
      productImage: [],
      status: 0,
    };
    this.productEdit = false;
    this.productDetailCreate = false;
    this.products = [];
    this.modalRef = this.modalService.open(this.content, {
      size: "xl",
      backdrop: false,
      windowClass: "custom-modal",
    });
  }
  cbcCategory() {
    this.categoryService.getCategory().subscribe((data) => {
      this.categoryDatas = data;
    });
  }

  cbcDesign() {
    this.designService.getDesign().subscribe((data) => {
      this.designDatas = data;
    });
  }
  cbcForm() {
    this.formService.getForm().subscribe((data) => {
      this.formDatas = data;
    });
  }
  cbcMaterial() {
    this.materialService.getMaterial().subscribe((data) => {
      this.materialDatas = data;
    });
  }
  cbcSleve() {
    this.sleeveService.getSleeve().subscribe((data) => {
      this.sleeveDatas = data;
    });
  }
  cbcCollar() {
    this.collarService.getCollar().subscribe((data) => {
      this.collarDatas = data;
    });
  }
  cbcColor() {
    this.colorService.getColor().subscribe((data) => {
      this.colorDatas = data;
    });
  }
  cbcSize() {
    this.sizeService.getSize().subscribe((data) => {
      this.sizeDatas = data;
    });
  }
  cbcDiscount() {
    this.discountService.getDiscount().subscribe((data) => {
      this.discountDatas = data;
    });
  }

  openDesignModal() {
    this.modalDesign = this.modalService.open(this.designModal, {
      size: "s",
    });
  }
  openFormModal() {
    this.modalForm = this.modalService.open(this.formModal, {
      size: "s",
    });
  }
  openMaterialModal() {
    this.modalMaterial = this.modalService.open(this.materialModal, {
      size: "s",
    });
  }
  openSleeveModal() {
    this.modalSleeve = this.modalService.open(this.sleeveModal, {
      size: "s",
    });
  }
  openCollarModal() {
    this.modalCollar = this.modalService.open(this.collarModal, {
      size: "s",
    });
  }
  openColorModal() {
    this.modalColor = this.modalService.open(this.colorModal, {
      size: "s",
    });
  }

  openSizeModal() {
    this.modalSize = this.modalService.open(this.sizeModal, {
      size: "s",
    });
  }
  openModalImport() {
    this.modalImport = this.modalService.open(this.importModal, {
      size: "s",
    });
  }
  saveDesign() {
    this.submittedDesign = true;
    if (this.formDesign.valid) {
      const name = this.formDesign.get("name").value;
      this.design.name = name;
      this.designService.createDesign(this.design).subscribe({
        next: (res) => {
          this.modalDesign.close();
          Swal.fire("Success!", " added successfully.", "success");
          this.cbcDesign();
        },
        error: (err) => {
          Swal.fire("Error!", "Failed to add .", "error");
        },
      });
    }
  }

  saveForm() {
    this.submittedForm = true;
    if (this.formForm.valid) {
      const name = this.formForm.get("name").value;
      this.formValue.name = name;
      this.formService.createForm(this.formValue).subscribe({
        next: (res) => {
          this.modalForm.close();
          Swal.fire("Success!", " added successfully.", "success");
          this.cbcForm();
        },
        error: (err) => {
          Swal.fire("Error!", "Failed to add .", "error");
        },
      });
    }
  }
  saveMaterial() {
    this.submittedMaterial = true;
    if (this.formMaterial.valid) {
      const name = this.formMaterial.get("name").value;
      this.material.name = name;
      this.materialService.createMaterial(this.material).subscribe({
        next: (res) => {
          this.modalMaterial.close();
          Swal.fire("Success!", " added successfully.", "success");
          this.cbcMaterial();
        },
        error: (err) => {
          Swal.fire("Error!", "Failed to add .", "error");
        },
      });
    }
  }
  saveSleeve() {
    this.submittedSleeve = true;
    if (this.formSleeve.valid) {
      const name = this.formSleeve.get("name").value;
      this.sleeve.name = name;
      this.sleeveService.createSleeve(this.sleeve).subscribe({
        next: (res) => {
          this.modalSleeve.close();
          Swal.fire("Success!", " added successfully.", "success");
          this.cbcSleve();
        },
        error: (err) => {
          Swal.fire("Error!", "Failed to add .", "error");
        },
      });
    }
  }
  saveCollar() {
    this.submittedCollar = true;
    if (this.formCollar.valid) {
      const name = this.formCollar.get("name").value;
      this.collar.name = name;
      this.collarService.createCollar(this.collar).subscribe({
        next: (res) => {
          this.modalCollar.close();
          Swal.fire("Success!", " added successfully.", "success");
          this.cbcCollar();
        },
        error: (err) => {
          Swal.fire("Error!", "Failed to add .", "error");
        },
      });
    }
  }
  saveColor() {
    this.submittedColor = true;
    if (this.formColor.valid) {
      const name = this.formColor.get("name").value;
      this.color.name = name;
      this.colorService.createColor(this.color).subscribe({
        next: (res) => {
          this.modalColor.close();
          Swal.fire("Success!", " added successfully.", "success");
          this.cbcColor();
        },
        error: (err) => {
          Swal.fire("Error!", "Failed to add .", "error");
        },
      });
    }
  }
  saveSize() {
    this.submittedSize = true;
    if (this.formSize.valid) {
      const name = this.formSize.get("code").value;
      const description = this.formSize.get("description").value;
      this.size.code = name;
      this.size.description = description;
      this.sizeService.createSize(this.size).subscribe({
        next: (res) => {
          this.modalSize.close();
          Swal.fire("Success!", " added successfully.", "success");
          this.cbcSize();
        },
        error: (err) => {
          Swal.fire("Error!", "Failed to add .", "error");
        },
      });
    }
  }
  async saveProduct() {
    if (this.productEdit != true && this.productDetailCreate != true) {
      if (
        this.formData.valid &&
        this.formProductDetail?.every((x) => x.valid)
      ) {
        if (this.validImages()) {
          this.isLoading = true;
          this.loading = true;
          this.product.productDetail = [];
          this.product.productImage = [];
          this.product.id = this.formData.get("id").value;
          this.product.name = this.formData.get("name").value;
          this.product.price = this.formData.get("price").value;
          this.product.description = this.formData.get("description").value;
          this.product.weight = this.formData.get("weight").value;
          this.product.category = this.formData.get("category").value;
          this.product.design = this.formData.get("design").value;
          this.product.form = this.formData.get("form").value;
          this.product.material = this.formData.get("material").value;
          this.product.sleeve = this.formData.get("sleeve").value;
          this.product.collar = this.formData.get("collar").value;
          this.product.discount = this.formData.get("discount").value;
          for (let i = 0; i < this.formProductDetail.length; i++) {
            const productDetail = {
              id: 0,
              barCode: undefined,
              quantity: 0,
              color: 0,
              size: 0,
              productId: 0,
            };
            productDetail.barCode =
              this.formProductDetail[i].get("barCode").value;
            productDetail.quantity =
              this.formProductDetail[i].get("quantity").value;
            productDetail.color = this.products[i].color;
            productDetail.size = this.products[i].size;
            this.product.productDetail[i] = productDetail;
          }
          const uploadPromises = [];
          for (const color of this.selectedColors) {
            if (this.imagesByColor[color.id]?.length > 0) {
              for (let i = 0; i < this.imagesByColor[color.id].length; i++) {
                const productImage: ProductImageRequest = {
                  id: 0,
                  mainImage: i == 0 ? true : false,
                  urlImage: this.imagesByColor[color.id][i].url,
                  colorId: color.id,
                  productId: 0,
                  status: 0,
                };
                this.product.productImage.push(productImage);
              }
            }
          }
          Promise.all(uploadPromises)
            .then(() => {
              console.log(this.product);
              this.productService.createProduct(this.product).subscribe({
                next: (res) => {
                  this.modalRef.close();
                  this.toastr.success(" Added successfully");
                  this.isLoading = false;
                  this.loading = false;
                  this.loadData();
                  this.clear();
                },
                error: (err) => {
                  this.toastr.error("Failed to add");
                  this.isLoading = false;
                  this.loading = false;
                },
              });
            })
            .catch((err) => {
              // Handle error here
            });
        } else {
          Swal.fire("Cảnh báo!", "Mỗi màu phải có ít nhất 4 ảnh", "warning");
        }
      } else {
        Swal.fire(
          "Cảnh báo!",
          "Bạn cần phải nhập đủ thông tin của sản phẩm",
          "warning"
        );
      }
    } else if (this.productEdit == true) {
      if (this.formData.valid) {
        if (this.validImages()) {
          this.isLoading = true;
          this.loading = true;
          this.product.productDetail = [];
          this.product.id = this.formData.get("id").value;
          this.product.name = this.formData.get("name").value;
          this.product.price = this.formData.get("price").value;
          this.product.description = this.formData.get("description").value;
          this.product.weight = this.formData.get("weight").value;
          this.product.category = this.formData.get("category").value;
          this.product.design = this.formData.get("design").value;
          this.product.form = this.formData.get("form").value;
          this.product.material = this.formData.get("material").value;
          this.product.sleeve = this.formData.get("sleeve").value;
          this.product.collar = this.formData.get("collar").value;
          this.product.discount = this.formData.get("discount").value;

          const uploadPromises = [];
          for (const color of this.selectedColors) {
            if (this.imagesByColor[color.id]?.length > 0) {
              for (let i = 0; i < this.imagesByColor[color.id].length; i++) {
                const found = this.product.productImage.some(
                  (productImage) =>
                    productImage.urlImage ===
                    this.imagesByColor[color.id][i].url
                );
                if (!found) {
                  const index = this.product.productImage.findIndex(
                    (a) => a.id === this.imagesByColor[color.id][i].id
                  );
                  this.product.productImage[index].urlImage =
                    this.imagesByColor[color.id][i].url;
                }
              }
            }
          }
          console.log(this.product.productImage);

          Promise.all(uploadPromises)
            .then(() => {
              console.log(this.product);
              this.productService.updateProduct(this.product).subscribe({
                next: (res) => {
                  this.modalRef.close();
                  this.toastr.success(" Update successfully");
                  this.isLoading = false;
                  this.loading = false;
                  this.loadData();
                  this.clear();
                },
                error: (err) => {
                  this.toastr.error(" Update failed");
                  this.isLoading = false;
                  this.loading = false;
                },
              });
            })
            .catch((err) => {
              // Handle error here
            });
        } else {
          Swal.fire("Cảnh báo!", "Mỗi màu phải có ít nhất 4 ảnh", "warning");
        }
      } else {
        Swal.fire(
          "Cảnh báo!",
          "Bạn cần phải nhập đủ thông tin của sản phẩm",
          "warning"
        );
      }
    } else if (this.productDetailCreate == true) {
      if (this.formProductDetail?.every((x) => x.valid)) {
        if (!(this.formProductDetail.length > 0)) {
          Swal.fire("Cảnh báo!", "Sản phẩm đã tồn tại", "warning");
          return;
        }
        if (this.validImages()) {
          this.isLoading = true;
          this.loading = true;
          this.productDetail = [];
          for (let i = 0; i < this.formProductDetail.length; i++) {
            const detail: ProductDetail = {
              id: 0,
              quantity: 0,
              color: 0,
              size: 0,
              product: 0,
              lstProductImage: [],
              status: 0,
            };
            detail.quantity = this.formProductDetail[i].get("quantity").value;
            detail.color = this.products[i].color;
            detail.size = this.products[i].size;
            detail.product = this.product.id;
            this.productDetail.push(detail);
          }
          for (const color of this.selectedColors) {
            if (this.imagesByColor[color.id]?.length > 0) {
              for (let i = 0; i < this.imagesByColor[color.id].length; i++) {
                const productImage: ProductImageRequest = {
                  id: 0,
                  mainImage: i == 0 ? true : false,
                  urlImage: this.imagesByColor[color.id][i].url,
                  colorId: color.id,
                  productId: 0,
                  status: 0,
                };
                this.productDetail[0].lstProductImage.push(productImage);
              }
            }
          }
          console.log(this.productDetail);
          this.productService
            .createProductDetail(this.productDetail)
            .subscribe({
              next: (res) => {
                this.modalRef.close();
                this.toastr.success("Added successfully");
                this.isLoading = false;
                this.loading = false;
                this.loadData();
                this.clear();
              },
              error: (err) => {
                this.toastr.error("Failed to add");
                this.isLoading = false;
                this.loading = false;
              },
            });
        } else {
          Swal.fire("Cảnh báo!", "Mỗi màu phải có ít nhất 4 ảnh", "warning");
        }
      } else {
        Swal.fire(
          "Cảnh báo!",
          "Bạn cần phải nhập đủ thông tin của sản phẩm",
          "warning"
        );
      }
    }
  }
  idImageRemove: number[];
  editProduct(element: any) {
    this.idImageRemove = [];
    this.products = [];
    this.productDetailCreate = false;
    this.productEdit = true;
    this.product = element;
    for (let i = 0; i < element.productImage.length; i++) {
      this.product.productImage[i].colorId = element.productImage[i].color.id;
    }
    this.formData = this.formBuilder.group({
      id: [""],
      name: ["", [Validators.required]],
      category: ["", [Validators.required]],
      discount: [""],
      description: ["", [Validators.required]],
      price: ["", [Validators.required]],
      weight: ["", [Validators.required]],
      design: ["", [Validators.required]],
      form: ["", [Validators.required]],
      material: ["", [Validators.required]],
      sleeve: ["", [Validators.required]],
      collar: ["", [Validators.required]],
    });
    this.formData.get("id").setValue(element.id);
    this.formData.get("name").setValue(element.name);
    this.formData.get("category").setValue(element.category.id);
    this.formData.get("description").setValue(element.description);
    this.formData.get("price").setValue(element.price);
    this.formData.get("weight").setValue(element.weight);
    this.formData.get("design").setValue(element.design.id);
    this.formData.get("form").setValue(element.form.id);
    this.formData.get("material").setValue(element.material.id);
    this.formData.get("sleeve").setValue(element.sleeve.id);
    this.formData.get("collar").setValue(element.collar.id);
    this.selectedColors = [];
    this.imagesByColor = [];
    element.productImage.forEach((productImage) => {
      const color = productImage.color;
      const index = this.selectedColors.findIndex(
        (c) => c.id === color.id && c.name === color.name
      );
      if (index === -1) {
        this.selectedColors.push(color);
      }
    });
    for (const color of this.selectedColors) {
      for (const img of element.productImage) {
        if (img.color.id === color.id) {
          const image = {
            file: null,
            url: img.urlImage,
            mainImage: img.mainImage,
            color: color,
            id: img.id,
          };
          if (!this.imagesByColor[color.id]) {
            this.imagesByColor[color.id] = [];
          }
          this.imagesByColor[color.id].push(image);
        }
      }
    }
    console.log(this.imagesByColor);

    this.modalRef = this.modalService.open(this.content, {
      size: "lg",
    });
  }
  titleProduct: string;
  createProductDetail(element: any) {
    this.titleProduct = element.name;
    this.productEdit = false;
    this.productDetailCreate = true;
    this.selectedColors = [];
    this.imagesByColor = [];
    this.products = [];
    this.product = element;
    this.productDetail = element.productDetail.map((detail) => {
      return {
        ...detail,
        color: detail.color.id,
        size: detail.size.id,
      };
    });
    console.log(this.productDetail);

    this.formData = this.formBuilder.group({
      id: [""],
      name: ["", [Validators.required]],
      category: ["", [Validators.required]],
      discount: [""],
      description: ["", [Validators.required]],
      price: ["", [Validators.required]],
      weight: ["", [Validators.required]],
      design: ["", [Validators.required]],
      form: ["", [Validators.required]],
      material: ["", [Validators.required]],
      sleeve: ["", [Validators.required]],
      collar: ["", [Validators.required]],
      colors: ["", [Validators.required]],
      sizes: ["", [Validators.required]],
    });
    this.modalRef = this.modalService.open(this.content, {
      size: "lg",
    });
  }
  deleteProduct(product: any) {
    Swal.fire({
      title: "Bạn có chắc không?",
      text: "Ngừng hoạt động sản phẩm " + product.name,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Tôi đồng ý!",
    }).then((result) => {
      if (result.value) {
        this.loading = true;
        this.productService.deleteProduct(product.id).subscribe({
          next: (res) => {
            this.toastr.info(
              "Đã ngừng hoạt động sản phẩm " + product.name + " thành công"
            );
            this.modalRef.close();
            this.loadData();
          },
          error: (err) => {
            this.toastr.error("Thất bại");
            this.loading = false;
          },
        });
      }
    });
  }
  updateStatusHoatDong(product: any) {
    Swal.fire({
      title: "Bạn có chắc không?",
      text: "Kích hoạt sản phẩm " + product.name,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Tôi đồng ý!",
    }).then((result) => {
      if (result.value) {
        this.loading = true;
        this.productService.updateStatusHoatDong(product.id).subscribe({
          next: (res) => {
            this.toastr.info(
              "Đã kích hoạt sản phẩm " + product.name + " thành công"
            );
            this.modalRef.close();
            this.loadData();
          },
          error: (err) => {
            this.toastr.error("Thất bại");
            this.loading = false;
          },
        });
      }
    });
  }
  updateStatusXoaVinhVien(product: any) {
    Swal.fire({
      title: "Bạn có chắc không?",
      text: "Xóa sản phẩm " + product.name,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Tôi đồng ý!",
    }).then((result) => {
      if (result.value) {
        this.loading = true;
        this.productService.updateStatusXoaVinhVien(product.id).subscribe({
          next: (res) => {
            if (res.status) {
              this.toastr.info("Xóa sản phẩm " + product.name + " thành công");
              this.modalRef.close();
              this.loadData();
            } else {
              this.toastr.info(res.message);
              this.loading = false;
            }
          },
          error: (err) => {
            this.toastr.error("Thất bại");
            this.loading = false;
          },
        });
      }
    });
  }
  titleProductDetail: string;
  productDetailUpdate: ProductDetail;
  editProductDetailDB(detail: any, element: any) {
    this.titleProductDetail =
      element.name + " - " + detail.color.name + " - " + detail.size.code;
    this.productDetailUpdate = detail;
    this.formDetail = this.formBuilder.group({
      ["barCode"]: [detail.barCode],
      ["status"]: [detail.status == 0 ? "Đang hoạt động" : "Ngừng hoạt động"],
      ["quantity"]: [detail.quantity, Validators.required],
    });
    this.modalRef = this.modalService.open(this.productDetailModal, {
      size: "md",
    });
  }
  updateProductDetail() {
    this.submittedDetail = true;
    this.isLoading = true;
    this.loading = true;
    if (this.formDetail.valid) {
      this.productDetailUpdate.quantity = this.formDetail.get("quantity").value;
      const detail: ProductDetail = {
        id: 0,
        quantity: 0,
        color: 0,
        size: 0,
        product: 0,
        lstProductImage: [],
        status: 0,
      };
      detail.quantity = this.formDetail.get("quantity").value;
      detail.id = this.productDetailUpdate.id;
      this.productService.updateProductDetail(detail).subscribe({
        next: (res) => {
          this.modalRef.close();
          this.toastr.success("Updated successfully");
          this.isLoading = false;
          this.loading = false;
          this.loadData();
        },
        error: (err) => {
          this.toastr.success("Updated failed");
          this.isLoading = false;
          this.loading = false;
        },
      });
    } else {
      Swal.fire(
        "Cảnh báo!",
        "Bạn cần phải nhập đủ thông tin của sản phẩm",
        "warning"
      );
    }
  }
  deleteProductDetailDB(detail: any) {
    Swal.fire({
      title: "Bạn có chắc không?",
      text: "Ngừng hoạt động " + detail.color.name + "/" + detail.size.code,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Tôi đồng ý!",
    }).then((result) => {
      if (result.value) {
        this.loading = true;
        this.productService.deleteProductDetail(detail.id).subscribe({
          next: (res) => {
            this.modalRef.close();
            this.toastr.success("Thành công");
            this.loadData();
          },
          error: (err) => {
            this.toastr.error("Thất bại");
          },
        });
      }
    });
  }
  updateDetailStatusHoatDong(detail: any) {
    Swal.fire({
      title: "Bạn có chắc không?",
      text: "Kích hoạt lại " + detail.color.name + "/" + detail.size.code,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Tôi đồng ý!",
    }).then((result) => {
      if (result.value) {
        this.loading = true;
        this.productService.updateDetailStatusHoatDong(detail.id).subscribe({
          next: (res) => {
            this.modalRef.close();
            this.toastr.success("Thành công");
            this.loadData();
          },
          error: (err) => {
            this.toastr.error("Thất bại");
          },
        });
      }
    });
  }
  updateDetailStatusXoaMem(detail: any) {
    Swal.fire({
      title: "Bạn có chắc không?",
      text: "Xóa " + detail.color.name + "/" + detail.size.code,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Tôi đồng ý!",
    }).then((result) => {
      if (result.value) {
        this.loading = true;
        this.productService.updateDetailStatusXoaMem(detail.id).subscribe({
          next: (res) => {
            if (res.status) {
              this.modalRef.close();
              this.toastr.success("Thành công");
              this.loadData();
              this.modalRef.close();
              this.loadData();
            } else {
              this.toastr.info(res.message);
              this.loading = false;
            }
          },
          error: (err) => {
            this.toastr.error("Thất bại");
          },
        });
      }
    });
  }
  clear() {
    this.formData.reset();
    this.formData.clearValidators();
    this.formData.updateValueAndValidity();
    if (this.formProductDetail) {
      for (let i = 0; i < this.formProductDetail.length; i++) {
        this.clearForm(i);
      }
    }
    this.selectedColors = [];
    this.products = [];
    this.imagesByColor = [];
  }
  clearForm(index) {
    this.formProductDetail[index].reset();
    this.formProductDetail[index].clearValidators();
    this.formProductDetail[index].updateValueAndValidity();
  }
  validImages() {
    for (const color of this.selectedColors) {
      if (this.imagesByColor[color.id].length < 4) {
        return false;
      }
    }
    return true;
  }
  onPageChange(event): void {
    this.currentPage = event;
    this.productListDatasPage = this.productListDatas.slice(
      (this.currentPage - 1) * this.itemsPerPage,
      this.currentPage * this.itemsPerPage
    );
  }
  searchFilter(e) {
    // const searchStr = e.target.value;
    // this.products = productList.filter((product) => {
    //   return product.name.toLowerCase().search(searchStr.toLowerCase()) !== -1;
    // });
  }

  discountLessFilter(e, percentage) {
    // if (e.target.checked && this.discountRates.length === 0) {
    //   this.products = productList.filter((product) => {
    //     return product.discount < percentage;
    //   });
    // } else {
    //   this.products = productList.filter((product) => {
    //     return product.discount >= Math.max.apply(null, this);
    //   }, this.discountRates);
    // }
  }

  discountMoreFilter(e, percentage: number) {
    // if (e.target.checked) {
    //   this.discountRates.push(percentage);
    // } else {
    //   this.discountRates.splice(this.discountRates.indexOf(percentage), 1);
    // }
    // this.products = productList.filter((product) => {
    //   return product.discount >= Math.max.apply(null, this);
    // }, this.discountRates);
  }

  valueChange(value: number, boundary: boolean): void {
    if (boundary) {
      this.minVal = value;
    } else {
      this.maxVal = value;
    }
  }
  ExportTOExcel() {
    this.loading = true;
    this.productService.getExportProduct(0).subscribe((res) => {
      this.loading = false;
      const blob = new Blob([res], { type: "application/vnd.ms-excel" });
      saveAs(blob, "products_" + new Date().toLocaleDateString() + ".xlsx");
    });
  }
  getTemplateExcelProduct() {
    this.loading = true;
    this.productService.getExportTemplateProduct().subscribe((res) => {
      this.loading = false;
      const blob = new Blob([res], { type: "application/vnd.ms-excel" });
      saveAs(
        blob,
        "products_template_" + new Date().toLocaleDateString() + ".xlsx"
      );
    });
  }
  imagesByColor: { [color: number]: any[] } = {};
  onFileSelected(event: any, color: any) {
    this.loading = true;
    const files: File[] = event.target.files;
    this.uploadService.uploadImageByColor(files).subscribe({
      next: (res) => {
        for (const file of res) {
          const image = {
            file: file,
            url: file,
            color: color,
            id: this.idImageRemove ? this.idImageRemove.shift() : 0,
          };
          if (!this.imagesByColor[color]) {
            this.imagesByColor[color] = [];
          }
          this.imagesByColor[color].push(image);
        }
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
      },
    });
    console.log(this.imagesByColor);
  }
  removeImage(image: any) {
    this.idImageRemove = [];
    const color = (image.color && image.color.id) || image.color;
    this.idImageRemove.push(image.id);
    const index = this.imagesByColor[color].indexOf(image);
    if (index >= 0) {
      this.imagesByColor[color].splice(index, 1);
      if (this.imagesByColor[color].length === 0) {
        delete this.imagesByColor[color];
      }
    }
    console.log(this.imagesByColor);
  }
  products: any[] = [];
  selectedColors: any[] = [];
  selectedSizes: any[] = [];
  onChange() {
    this.products = [];
    for (let size of this.formData.get("sizes")?.value) {
      for (let color of this.formData.get("colors")?.value) {
        this.products.push({
          name:
            this.formData.get("name")?.value +
            " - " +
            this.colorDatas.find((c) => c.id === color)?.name +
            " - " +
            this.sizeDatas.find((c) => c.id === size)?.code,
          color: color,
          size: size,
        });
      }
    }
    if (this.productDetailCreate) {
      for (let detail of this.productDetail) {
        const index = this.products.findIndex(
          (value) => value.color === detail.color && value.size === detail.size
        );

        if (index > -1) {
          this.products.splice(index, 1);
        }
      }
    }
    this.selectedColors = [];
    for (let color of this.formData.get("colors")?.value) {
      this.selectedColors.push(this.colorDatas.find((c) => c.id === color));
      this.imagesByColor[color] = [];
    }
    if (this.productDetailCreate) {
      for (let detail of this.productDetail) {
        const color = this.selectedColors.findIndex(
          (value) => value.id === detail.color
        );
        if (color > -1) {
          this.selectedColors.splice(color, 1);
        }
      }
    }
    this.selectedSizes = [];
    for (let size of this.formData.get("sizes")?.value) {
      this.selectedSizes.push(this.sizeDatas.find((c) => c.id === size));
    }
    if (this.productDetailCreate) {
      for (let detail of this.productDetail) {
        const size = this.selectedSizes.findIndex(
          (value) => value.id === detail.size
        );
        if (size > -1) {
          this.selectedSizes.splice(size, 1);
        }
      }
    }
    this.formProductDetail = [];
    for (let i = 0; i < this.products.length; i++) {
      this.formProductDetail[i] = this.formBuilder.group({
        ["id"]: [""],
        ["barCode"]: [""],
        ["quantity"]: ["", Validators.required],
      });
    }
  }
  deleteProductDetail(product) {
    this.products = this.products.filter((p) => p !== product);

    const { color, size } = product;
    const colorExists = this.products.some((p) => p.color === color);
    const sizeExists = this.products.some((p) => p.size === size);

    if (!colorExists) {
      this.formData.controls.colors.patchValue(
        this.formData.controls.colors.value.filter((v) => v !== color)
      );
      this.selectedColors = [];
      for (let color of this.formData.get("colors")?.value) {
        this.selectedColors.push(this.colorDatas.find((c) => c.id === color));
        this.imagesByColor[color] = [];
      }
    }

    if (!sizeExists) {
      this.formData.controls.sizes.patchValue(
        this.formData.controls.sizes.value.filter((v) => v !== size)
      );
      this.selectedSizes = [];
      for (let size of this.formData.get("sizes")?.value) {
        this.selectedSizes.push(this.sizeDatas.find((c) => c.id === size));
      }
    }
    this.formProductDetail = [];
    for (let i = 0; i < this.products.length; i++) {
      this.formProductDetail[i] = this.formBuilder.group({
        ["id"]: [""],
        ["barCode"]: [""],
        ["quantity"]: ["", Validators.required],
      });
    }
  }
  openImage(images: ProductImageResponse[], index: number): void {
    const albums = [];
    for (let i = 0; i < images.length; i++) {
      const src = images[i].urlImage;
      const caption = "Image " + i + " caption here";
      const thumb = images[i].urlImage;
      const album = {
        src,
        caption,
        thumb,
      };
      albums.push(album);
    }
    this.lightbox.open(albums, index);
  }

  columnsToDisplay = ["#", "product", "price", "weight", "category", "status"];
  detailColumns = ["barCode", "name", "quantity", "statusDetail"];
  expandedElement: ProductRespone | null;
  detailElement: ProductDetailRespone | null;
  fileExcel: File;
  onFileChange(event) {
    this.fileExcel = event.target.files[0];
  }
  importExcel() {
    this.loading = true;
    if (this.fileExcel) {
      this.productService.fileExcle(this.fileExcel).subscribe({
        next: (res) => {
          this.toastr.error("Nhập dữ liệu thành công");
          this.loadData();
          this.modalImport.close();
        },
        error: (err) => {
          this.toastr.error("Thất bại");
          this.loading = false;
        },
      });
    }
  }
}
