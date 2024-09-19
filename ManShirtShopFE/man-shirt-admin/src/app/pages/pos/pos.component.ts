import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { Cart } from "./cart.model";
import { cartData } from "./data";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { NgxBarcodeScannerService } from "@eisberg-labs/ngx-barcode-scanner";
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from "@angular/forms";
import Swal from "sweetalert2";
import { ToastrService } from "ngx-toastr";
import { GhnService } from "src/app/core/services/ghn.service";
import { cart, cartDetail } from "src/app/core/models/cart.model";
import { color } from "src/app/core/models/color.models";
import { ProductDetailRespone } from "src/app/core/models/productDetail.model";
import { FilterData, ProductRespone } from "src/app/core/models/product.model";
import { ProductService } from "src/app/core/services/product.service";
import { size } from "src/app/core/models/size.models";
import { CartService } from "src/app/core/services/cart.service";
import {
  customerrequest,
  customerresponse,
} from "src/app/core/models/customer.models";
import { CustomerService } from "src/app/core/services/customer.service";
import { VoucherService } from "src/app/core/services/voucher.service";
import { CategoryoriService } from "src/app/core/services/categoryori.service";
import { BillComponent } from "../bill/bill.component";
import { Order, ProductDetail } from "src/app/core/models/order.models";
import {
  COMPLETED,
  CONFIRMED,
  DA_THANH_TOAN,
  SALE_POS,
} from "src/app/core/constant/order.constant";
import { OrderService } from "src/app/core/services/order.service";
@Component({
  selector: "app-pos",
  templateUrl: "./pos.component.html",
  styleUrls: ["./pos.component.scss"],
})
export class PosComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;
  cartData: Cart[];
  isFullscreen = false;
  modalItem: NgbModalRef;
  modalCustomerVar: NgbModalRef;
  modalCheckoutVar: NgbModalRef;
  modalBarCode: NgbModalRef;
  modalOrderVar: NgbModalRef;
  categoryData: any[];
  customerData: any[];
  customerSearch: any;
  voucherData: any[];
  isClassVisible = false;
  selectProvince: any;
  selectDistrict: any;
  selectWard: any;
  selectedColor: color;
  selectedSize: size;
  formData: FormGroup;
  formCustomer: FormGroup;
  productListDatas: ProductRespone[];
  productDataModal: ProductRespone;
  loading = false;
  quantityDataModal: number = 1;
  detail: ProductDetailRespone;
  total: number = 0;
  barCodeValue: string;
  tabs: cart[];
  tabValue: any;
  costShip: number;
  costVoucher: number;
  codeVoucher: any;
  showAddCustomerButton = false;
  public filterData: FilterData = {
    size: null,
    form: null,
    color: null,
    status: 0,
    design: null,
    sleeve: null,
    collar: null,
    low: null,
    high: null,
    discount: null,
    category: null,
    material: null,
  };
  @ViewChild("modalPosItem") modalPosItem: any;
  @ViewChild("modalCustomer") modalCustomer: any;
  @ViewChild("modalCheckout") modalCheckout: any;
  @ViewChild("barCodeScanner") modalBarCodeScanner: any;
  @ViewChild("modalOrder") modalOrder: any;
  constructor(
    private modalService: NgbModal,
    private toastr: ToastrService,
    private ghnService: GhnService,
    private formBuilder: FormBuilder,
    private productService: ProductService,
    private barCodeService: NgxBarcodeScannerService,
    private cartService: CartService,
    private customerService: CustomerService,
    private voucherService: VoucherService,
    private categoryService: CategoryoriService,
    private orderService: OrderService
  ) {
    this.tabs = this.cartService.getCarts();
    this.formData = this.formBuilder.group({
      customer: ["", [Validators.required]],
      paymentType: ["Tiền mặt", [Validators.required]],
      province: ["", [Validators.required]],
      district: ["", [Validators.required]],
      ward: ["", [Validators.required]],
      voucher: ["", [Validators.required]],
      note: [""],
      address: ["", [Validators.required]],
    });
    this.formCustomer = this.formBuilder.group({
      fullName: ["", [Validators.required]],
      phone: [
        "",
        [
          Validators.required,
          Validators.pattern(/(84|0[3|5|7|8|9])+([0-9]{8})\b/g),
        ],
      ],
    });
  }
  openModalBarCodeScanner() {
    this.modalBarCode = this.modalService.open(this.modalBarCodeScanner, {
      size: "md",
    });
  }
  onValueChanges(detectedValue: string) {
    this.barCodeValue = detectedValue;
    this.barCodeService.stop();
    this.productService.findByBarCode(this.barCodeValue).subscribe({
      next: (res) => {
        this.modalBarCode.close();
        const productDetail: ProductDetailRespone = res;
        productDetail.productId = res.product?.id;
        this.addToCart(res);
      },
    });
  }
  checkout() {
    if (this.formData.get("customer").invalid) {
      this.toastr.warning("Thiếu thông tin khách hàng");
      return;
    }
    if (this.showAddress) {
      if (
        this.formData.get("province").invalid ||
        this.formData.get("district").invalid ||
        this.formData.get("ward").invalid ||
        this.formData.get("address").invalid
      ) {
        this.toastr.warning("Thiếu địa chỉ");
        return;
      }
    }
    this.loading = true;
    const order: Order = {
      id: 0,
      code: "",
      freight: 0,
      shipName: "",
      address: "",
      cityName: "",
      districtName: "",
      wardName: "",
      shipPhone: "",
      note: "",
      paymentType: "",
      total: 0,
      statusPay: 0,
      saleForm: false,
      idDistrict: 0,
      idWard: "",
      voucher: 0,
      employeed: 0,
      customer: 0,
      status: 0,
      lstProductDetail: [],
    };

    const customer = this.formData.get("customer").value;
    if (
      typeof customer === "object" &&
      customer !== null &&
      customer.hasOwnProperty("id")
    ) {
      order.customer = customer.id;
    } else {
      order.customer = customer;
    }
    order.paymentType = this.formData.get("paymentType").value;
    order.saleForm = SALE_POS;
    if (this.showAddress) {
      order.idDistrict = this.formData.get("district").value;
      order.idWard = this.formData.get("ward").value;
      order.address = this.formData.get("address").value;
      this.selectProvince.forEach((value) => {
        if (value.ProvinceID === this.formData.get("province").value) {
          order.cityName = value.ProvinceName;
        }
      });
      this.selectDistrict.forEach((value) => {
        if (value.DistrictID === this.formData.get("district").value) {
          order.districtName = value.DistrictName;
        }
      });
      this.selectWard.forEach((value) => {
        if (value.WardCode === this.formData.get("ward").value) {
          order.wardName = value.WardName;
        }
      });
      order.freight = this.costShip;
      this.customerData.forEach((value) => {
        if (value.id === order.customer) {
          order.shipName = value.fullname;
          order.shipPhone = value.phone;
        }
      });
      order.status = CONFIRMED;
    } else {
      order.status = COMPLETED;
    }
    order.note = this.formData.get("note").value;
    order.total = this.getTong(this.total, this.costShip, this.costVoucher);
    order.statusPay = DA_THANH_TOAN;
    if (this.costVoucher > 0) {
      order.voucher = this.formData.get("voucher").value;
    }
    order.employeed = null;
    order.lstProductDetail = [];
    this.tabValue.cartDetail.forEach((value) => {
      const productDetail = new ProductDetail();
      productDetail.quantity = value.quantity;
      productDetail.id = value.detail.id;
      order.lstProductDetail.push(productDetail);
    });
    console.log(order);

    this.orderService.createOrder(order).subscribe({
      next: (value) => {
        this.toastr.success("Thành công");
        this.modalCheckoutVar.close();
        this.modalOrderVar = this.modalService.open(BillComponent, {
          size: "md",
        });
        this.modalOrderVar.componentInstance.billData = value;
        this.loadProduct();
        this.cartService.removeCart(this.tabValue);
        this.tabs = this.cartService.getCarts();
        this.selectedTabIndex = this.tabs.length - 1;
        this.tabValue = this.tabs[this.tabs.length - 1];
        this.getTotalPrice();
        this.formData = this.formBuilder.group({
          customer: ["", [Validators.required]],
          paymentType: ["Tiền mặt", [Validators.required]],
          province: ["", [Validators.required]],
          district: ["", [Validators.required]],
          ward: ["", [Validators.required]],
          voucher: ["", [Validators.required]],
          note: [""],
          address: ["", [Validators.required]],
        });
        this.costShip = 0;
        this.costVoucher = 0;
        this.loading = false;
      },
      error: (err) => {
        this.toastr.error("Thất bại");
        this.loading = false;
      },
    });
  }
  ngOnInit(): void {
    this.loading = true;
    this.breadCrumbItems = [
      { label: "Bảng điều khiển" },
      { label: "Bán hàng", active: true },
    ];
    this._fetchData();
    this.ghnService.getProvince().subscribe((res) => {
      this.selectProvince = res.data;
    });
    this.loadProduct();
    this.loadCustomer();
    this.loadVoucher();
    this.loadCategory();
  }
  loadProduct() {
    this.productService.getProductFilter(this.filterData).subscribe({
      next: (data) => {
        console.log(data);

        this.productListDatas = data;
        if (this.tabs.length > 0) {
          this.tabValue = this.tabs[0];
          this.getTotalPrice();
        }
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
      },
    });
  }
  loadCategory() {
    this.categoryService.getCollar().subscribe({
      next: (res) => {
        this.categoryData = res;
      },
    });
  }
  loadCustomer() {
    this.customerService.getCustomer().subscribe({
      next: (res) => {
        this.customerData = res;
        this.customerData.forEach((value) => {
          value.name = value.fullname + " - " + value.phone;
        });
        // this.formData.controls["customer"].setValue(this.customerData[0].id);
        // this.customerSelected = this.customerData[0].id;
      },
    });
  }
  loadVoucher() {
    this.voucherService.getActiveVouchers().subscribe({
      next: (res) => {
        this.voucherData = res;
        this.voucherData.forEach((value) => {
          value.name =
            value.code +
            " - Giảm " +
            (value.type === true
              ? value.discount + " VNĐ"
              : value.discount + "%");
          // + (value.min !== null ?  '(Đơn hàng tối thiểu '+ value.min + ' VNĐ' : '') + (value.max !== null ?  ', Giảm tối đa '+ value.max + ' VNĐ' : ')');
        });
      },
    });
  }
  onChangeVoucher() {
    if (this.formData.get("voucher").valid) {
      const id = this.formData.get("voucher").value;
      const voucher = this.voucherData.find((value) => value.id === id);
      if (voucher.min && this.total < voucher.min) {
        this.costVoucher = 0;
        this.codeVoucher = undefined;
        this.toastr.warning(
          "Giá trị đơn hàng quá thấp để áp dụng voucher này."
        );
        return;
      }

      if (!voucher.type) {
        this.codeVoucher = voucher.code;
        // voucher giảm giá theo phần trăm
        this.costVoucher = (voucher.discount * this.total) / 100;
        if (voucher.max && this.costVoucher > voucher.max) {
          this.costVoucher = voucher.max; // giới hạn giá trị giảm giá tối đa nếu voucher giảm giá theo phần trăm
        }
      } else {
        this.codeVoucher = voucher.code;
        // voucher giảm giá bằng tiền
        this.costVoucher = voucher.discount;
        if (voucher.max && this.costVoucher > voucher.max) {
          this.toastr.warning("Giá trị giảm giá vượt quá mức tối đa cho phép.");
          return;
        }
      }
    } else {
      this.costVoucher = 0;
      this.codeVoucher = undefined;
    }
  }
  compareFn(customer1: any, customer2: any) {
    return customer1 && customer2 && customer1.id === customer2.id;
  }
  private _fetchData() {
    this.cartData = cartData;
  }
  getAllColors(product: ProductRespone): color[] {
    const colors = product.productDetail.map((detail) => detail.color);
    const uniqueColors = colors.filter(
      (color, index, self) => index === self.findIndex((c) => c.id === color.id)
    );
    return uniqueColors;
  }
  getAllSizes(product: ProductRespone): size[] {
    const sizes = product.productDetail.map((detail) => detail.size);
    const uniqueSizes = sizes.filter(
      (size, index, self) => index === self.findIndex((s) => s.id === size.id)
    );
    return uniqueSizes;
  }
  getQuantity(
    details: ProductDetailRespone[],
    selectedColor: color,
    selectedSize: size
  ): number {
    const detail = details.find(
      (d) => d.color.id === selectedColor.id && d.size.id === selectedSize.id
    );
    return detail ? detail.quantity : 0;
  }
  getQuantityByProductDetail(productDetail: ProductDetailRespone): number {
    const product = this.getProduct(productDetail);
    const detail = product?.productDetail?.find(
      (d) => d.id === productDetail?.id
    );
    return detail ? detail.quantity : 0;
  }
  getProductDetail(
    details: ProductDetailRespone[],
    selectedColor: color,
    selectedSize: size
  ): ProductDetailRespone {
    const detail = details.find(
      (d) => d.color.id === selectedColor.id && d.size.id === selectedSize.id
    );
    return detail ? detail : null;
  }
  getProduct(productDetail: ProductDetailRespone): ProductRespone {
    const product = this.productListDatas.find(
      (p) => p.id === productDetail.productId
    );
    return product ? product : null;
  }
  getTotalPrice() {
    this.total = 0;
    if (this.tabValue?.cartDetail.length > 0) {
      for (const item of this.tabValue.cartDetail) {
        const product = this.getProduct(item.detail);
        if (product) {
          this.total +=
            item.quantity *
            (product.price -
              (product.price * product.productDiscount[0]?.percent || 0) / 100);
        }
      }
    }
  }
  changeColor(color: color) {
    this.selectedColor = color;
  }
  changeSize(size: size) {
    this.selectedSize = size;
  }
  onChangeProvince(provinceId: number) {
    this.formData.get("district").setValue("");
    this.formData.get("ward").setValue("");
    if (provinceId !== null) {
      this.ghnService.getDistrict(provinceId).subscribe((res) => {
        this.selectDistrict = res.data;
      });
    }
  }
  onChangeDistrict(districtId: number) {
    this.formData.get("ward").setValue("");
    if (districtId !== null) {
      this.ghnService.getWard(districtId).subscribe((res) => {
        this.selectWard = res.data;
      });
    }
  }
  onChangeWard() {
    if (
      this.formData.get("district").valid &&
      this.formData.get("ward").valid
    ) {
      const district = this.formData.get("district").value;
      const ward = this.formData.get("ward").value;
      this.ghnService.getServiceFee(district, ward).subscribe({
        next: (res) => {
          this.costShip = res.data.total;
        },
        error: (err) => {
          this.toastr.error(
            "Api giao hàng nhanh lỗi không tính được tiền ship"
          );
        },
      });
    } else {
      this.costShip = 0;
    }
  }
  openModalPosItem(product: ProductRespone) {
    this.modalItem = this.modalService.open(this.modalPosItem, {
      size: "lg",
    });
    this.quantityDataModal = 1;
    this.productDataModal = product;
    const allColors = this.getAllColors(this.productDataModal);
    if (allColors.length > 0) {
      this.selectedColor = allColors[0];
    }
    const allSizes = this.getAllSizes(this.productDataModal);
    if (allSizes.length > 0) {
      this.selectedSize = allSizes[0];
    }
  }

  onSearchCustomerChange(searchValue: any): void {
    const searchText = searchValue.term;
    const validPhoneRegex = /(84|0[3|5|7|8|9])+([0-9]{8})\b/g;

    if (validPhoneRegex.test(searchText)) {
      this.showAddCustomerButton = true;
    } else {
      this.showAddCustomerButton = false;
    }
    this.customerSearch = searchValue.term;
  }
  openModalCustomer() {
    this.modalCustomerVar = this.modalService.open(this.modalCustomer, {
      size: "md",
    });
    this.formCustomer.get("phone").setValue(this.customerSearch);
  }
  openModalOrder() {
    this.modalOrderVar = this.modalService.open(BillComponent, {
      size: "md",
    });
  }
  saveCustomer() {
    if (!this.formCustomer.controls["phone"]?.valid) {
      this.toastr.warning("Số điện thoại không hợp lệ");
    }
    if (this.formCustomer.valid) {
      this.loading = true;
      const fullName = this.formCustomer.get("fullName").value;
      const phone = this.formCustomer.get("phone").value;
      const customer: customerrequest = {
        id: 0,
        email: "",
        password: "",
        fullname: "",
        birthDate: null,
        phone: "",
        role: 3,
        photo: "",
        status: 0,
      };
      customer.fullname = fullName;
      customer.phone = phone;
      console.log(customer);
      this.customerService.createCustomer(customer).subscribe({
        next: (res) => {
          this.toastr.success("Thêm thành công");
          this.customerService.getCustomer().subscribe({
            next: (resp) => {
              this.customerData = resp;
              this.customerData.forEach((value) => {
                value.name = value.fullname + " - " + value.phone;
              });
              const customerNew = this.customerData.findIndex(
                (value) => value.id === res.id
              );
              this.formData.controls["customer"].setValue(
                this.customerData[customerNew]
              );
              this.customerSelected = res.id;
              this.loading = false;
            },
            error(err) {
              this.loading = false;
            },
          });
          this.formCustomer = this.formBuilder.group({
            fullName: ["", [Validators.required]],
            phone: [
              "",
              [
                Validators.required,
                Validators.pattern(/(84|0[3|5|7|8|9])+([0-9]{8})\b/g),
              ],
            ],
          });
          this.modalCustomerVar.close();
        },
        error: (err) => {},
      });
    }
  }
  customerSelected: any;
  onChangeCustomer() {
    this.customerSelected = this.formData.get("customer").value;
    console.log(this.formData.get("customer").value);
  }
  openModalCheckout() {
    if (
      this.tabValue &&
      this.tabValue.cartDetail !== undefined &&
      this.tabValue.cartDetail.length > 0
    ) {
      this.tabValue.cartDetail.forEach((element) => {
        const quantity = this.getQuantityByProductDetail(element.detail);
        if (quantity < element.quantity) {
          this.toastr.error("Số lượng vượt quá số lượng trong kho");
          return;
        }
      });
      this.onChangeVoucher();
      if (this.customerSelected !== undefined) {
        const customerNew = this.customerData.findIndex(
          (value) => value.id === this.customerSelected
        );
        this.formData.controls["customer"].setValue(
          this.customerData[customerNew]
        );
      }
      this.modalCheckoutVar = this.modalService.open(this.modalCheckout, {
        size: "xl",
      });
    } else {
      this.toastr.warning("Hóa đơn trống!");
    }
  }
  openFullscreen() {
    const elem = document.documentElement;
    if (elem.requestFullscreen) {
      elem.requestFullscreen();
    } else if (elem.requestFullscreen) {
      elem.requestFullscreen();
    } else if (elem.requestFullscreen) {
      elem.requestFullscreen();
    } else if (elem.requestFullscreen) {
      elem.requestFullscreen();
    }
    this.isFullscreen = true;
  }

  closeFullscreen() {
    if (document.exitFullscreen) {
      document.exitFullscreen();
    }
    this.isFullscreen = false;
  }
  toggleClass(): void {
    this.isClassVisible = !this.isClassVisible;
  }
  showRemoveConfirmation: any;
  removeItem(productDetail: any): void {
    this.showRemoveConfirmation = productDetail;
  }
  removeDetail(productDetail: any): void {
    this.cartService.removeItemFromCart(this.tabValue, productDetail.detail);
    this.tabs = this.cartService.getCarts();
    this.getTotalPrice();
    this.showRemoveConfirmation = null;
  }
  cancelRemove(): void {
    this.showRemoveConfirmation = null;
  }
  showAddress: boolean = false;
  toggleAddress() {
    this.showAddress = !this.showAddress;
    if (this.showAddress) {
      this.onChangeWard();
    } else {
      this.costShip = 0;
    }
  }

  addTab(): void {
    const id = this.tabs[this.tabs.length - 1]?.id + 1 || 0 + 1;
    const tabId = `Hóa đơn ${id}`;
    const newTab = { id: id, name: tabId, cartDetail: [] };
    this.cartService.addCart(newTab);
    this.tabs = this.cartService.getCarts();
    this.selectedTabIndex = this.tabs.length - 1;
    this.tabValue = this.tabs[this.tabs.length - 1];
    this.getTotalPrice();
  }

  removeTab(cart: cart): void {
    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning", // thay thế type bằng icon
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!",
    }).then((result) => {
      if (result.value) {
        this.cartService.removeCart(cart);
        this.tabs = this.cartService.getCarts();
        this.selectedTabIndex = this.tabs.length - 1;
        this.tabValue = this.tabs[this.tabs.length - 1];
        this.getTotalPrice();
        this.toastr.success("Xóa hóa đơn thành công");
      }
    });
  }

  addToCart(productDetail: ProductDetailRespone) {
    const index = this.tabs.indexOf(this.tabValue);
    if (index < 0) {
      this.toastr.warning("Bạn phải tạo hóa đơn");
    } else {
      const quantity = this.getQuantityByProductDetail(productDetail);
      const existingItemIndex = this.tabValue?.cartDetail?.findIndex(
        (x) => x.detail.id === productDetail.id
      );

      if (
        Number(this.quantityDataModal) > quantity ||
        quantity <
          this.tabValue.cartDetail[existingItemIndex]?.quantity +
            Number(this.quantityDataModal)
      ) {
        this.toastr.error("Số lượng vượt quá số lượng trong kho");
        return;
      }
      this.cartService.addToCart(
        this.tabValue,
        productDetail,
        Number(this.quantityDataModal)
      );
      this.tabs = this.cartService.getCarts();
      this.selectTab(index);
      this.toastr.success("Thêm thành công");
      this.getTotalPrice();
    }
  }
  addToCart1(
    details: ProductDetailRespone[],
    selectedColor: color,
    selectedSize: size
  ) {
    const productDetail = this.getProductDetail(
      details,
      selectedColor,
      selectedSize
    );
    const index = this.tabs.indexOf(this.tabValue);
    if (index < 0) {
      this.toastr.warning("Bạn phải tạo hóa đơn");
    } else {
      const quantity = this.getQuantity(details, selectedColor, selectedSize);
      const existingItemIndex = this.tabValue?.cartDetail?.findIndex(
        (x) => x.detail.id === productDetail.id
      );
      if (
        Number(this.quantityDataModal) > quantity ||
        quantity <
          this.tabValue.cartDetail[existingItemIndex]?.quantity +
            Number(this.quantityDataModal)
      ) {
        this.toastr.error("Số lượng vượt quá số lượng trong kho");
        return;
      }
      this.cartService.addToCart(
        this.tabValue,
        productDetail,
        Number(this.quantityDataModal)
      );
      this.tabs = this.cartService.getCarts();
      // this.selectTab(index);
      this.toastr.success("Thêm thành công");
      this.getTotalPrice();
    }
  }
  selectedTabIndex = 0;
  selectTab(index) {
    this.formData = this.formBuilder.group({
      customer: ["", [Validators.required]],
      paymentType: ["Tiền mặt", [Validators.required]],
      province: ["", [Validators.required]],
      district: ["", [Validators.required]],
      ward: ["", [Validators.required]],
      voucher: ["", [Validators.required]],
      note: [""],
      address: ["", [Validators.required]],
    });
    this.selectedTabIndex = index;
    this.tabValue = this.tabs[index];
    this.getTotalPrice();
  }
  decrement() {
    if (this.quantityDataModal > 1) {
      this.quantityDataModal--;
    }
  }

  increment() {
    this.quantityDataModal++;
  }
  decrementItem(productDetail: ProductDetailRespone) {
    this.cartService.decreaseItemQuantity(this.tabValue, productDetail);
    this.tabs = this.cartService.getCarts();
    this.getTotalPrice();
  }
  setQuantity(quantity: string, productDetail: cartDetail) {
    const quantityItem = Number(quantity);
    const quantityOld = this.getQuantityByProductDetail(productDetail.detail);

    if (quantityItem > quantityOld) {
      productDetail.quantity = quantityOld;
      this.toastr.error("Số lượng vượt quá số lượng trong kho");
      return;
    }
    if (isNaN(quantityItem)) {
      return;
    }
    this.cartService.setQuantity(
      this.tabValue,
      productDetail.detail,
      quantityItem
    );
    this.tabs = this.cartService.getCarts();
    this.getTotalPrice();
  }
  incrementItem(productDetail: ProductDetailRespone) {
    const quantity = this.getQuantityByProductDetail(productDetail);

    const existingItemIndex = this.tabValue.cartDetail.findIndex(
      (x) => x.detail.id === productDetail.id
    );

    if (quantity < this.tabValue.cartDetail[existingItemIndex]?.quantity + 1) {
      this.toastr.error("Số lượng vượt quá số lượng trong kho");
      return;
    }
    this.cartService.increaseItemQuantity(this.tabValue, productDetail);
    this.tabs = this.cartService.getCarts();
    this.getTotalPrice();
  }
  getTong(total, costShip, costVoucher) {
    const finalPrice =
      total != 0
        ? total +
          (costShip >= 0 ? costShip : 0) -
          (costVoucher >= 0 ? costVoucher : 0)
        : 0;
    return finalPrice;
  }
  check;
}
