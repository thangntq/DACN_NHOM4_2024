import { isPlatformBrowser } from "@angular/common";
import {
  Component,
  Inject,
  OnInit,
  PLATFORM_ID,
  TemplateRef,
  ViewChild,
} from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ModalDismissReasons, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { ToastrService } from "ngx-toastr";
import { GhnService } from "src/app/shared/services/ghn.service";
import { OrderService } from "src/app/shared/services/order.service";
import { AuthenticationService } from "../login/auth.service";

@Component({
  selector: "app-address",
  templateUrl: "./address.component.html",
  styleUrls: ["./address.component.scss"],
})
export class AddressComponent implements OnInit {
  public openDashboard: boolean = false;
  @ViewChild("addressModal", { static: false }) AddressModal: TemplateRef<any>;
  lstAddress: any;
  public addressForm: FormGroup;
  public closeResult: string;
  public modalOpen: boolean = false;
  provinces: any;
  districts: any;
  wards: any;
  isSubmitting = false;
  addressEdit: any;
  userInfo: any;
  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private fb: FormBuilder,
    private modalService: NgbModal,
    private ghnService: GhnService,
    private toastrService: ToastrService,
    private orderService: OrderService,
    private authService: AuthenticationService
  ) {}

  ngOnInit(): void {
    this.loadProvinces();
    this.loadAddress();
    this.authService.getUser().subscribe({
      next: (value) => {
        this.userInfo = value[0];
      },
      error: (error) => {},
    });
  }
  private loadAddress() {
    this.orderService.getAddress().subscribe({
      next: (res) => {
        this.lstAddress = res;
      },
      error: (err) => {
        this.toastrService.error("Lỗi api address");
      },
    });
  }
  save() {
    if (this.addressForm.controls["name"].invalid) {
      this.toastrService.warning("Họ và tên trống");
      return;
    }
    if (this.addressForm.controls["phone"].invalid) {
      this.toastrService.warning("Số điện thoại sai");
      return;
    }
    if (
      this.addressForm.controls["address"].invalid ||
      this.addressForm.controls["province"].invalid ||
      this.addressForm.controls["district"].invalid ||
      this.addressForm.controls["ward"].invalid
    ) {
      this.toastrService.warning("Địa chỉ trống");
      return;
    }
    this.isSubmitting = true;
    let address: any = {};
    address.fullName = this.addressForm.controls["name"].value;
    address.phone = this.addressForm.controls["phone"].value;
    address.address = this.addressForm.controls["address"].value;
    address.idCity = this.addressForm.controls["province"].value;
    this.provinces.forEach((element) => {
      if (element.ProvinceID == address.idCity) {
        address.cityName = element.ProvinceName;
      }
    });
    address.idDistrict = this.addressForm.controls["district"].value;
    this.districts.forEach((element) => {
      if (element.DistrictID == address.idDistrict) {
        address.districtName = element.DistrictName;
      }
    });
    address.idWard = this.addressForm.controls["ward"].value;
    this.wards.forEach((element) => {
      if (element.WardCode === address.idWard) {
        address.wardName = element.WardName;
      }
    });
    address.default = this.addressForm.controls["default"].value;
    address.other = "";
    if (this.addressEdit) {
      address.id = this.addressEdit.id;
    }
    console.log(address);
    this.orderService.addAddress(address).subscribe({
      next: (res) => {
        if (!this.addressEdit) {
          this.toastrService.success("Thêm địa chỉ thành công");
        } else {
          this.toastrService.success("Sửa địa chỉ thành công");
        }
        this.loadAddress();
        this.ngOnDestroy();
        this.isSubmitting = false;
      },
      error: (err) => {
        this.toastrService.error("Thất bại");
        this.isSubmitting = false;
      },
    });
  }
  remove(address: any) {
    this.orderService.removeAddress(address.id).subscribe({
      next: (res) => {
        this.toastrService.success("Xóa địa chỉ thành công");
        this.loadAddress();
      },
      error: (err) => {
        this.toastrService.error("Xóa thất bại");
      },
    });
  }
  private loadProvinces() {
    this.ghnService.getProvinces.subscribe({
      next: (res) => {
        this.provinces = res.data;
      },
      error: (err) => {
        this.toastrService.error("Lỗi api tỉnh");
      },
    });
  }
  edit(address: any) {
    this.openModal();
    this.addressEdit = address;
    this.loadDistricts(address.idCity);
    this.loadWards(address.idDistrict);
    this.addressForm = this.fb.group({
      name: [
        address.fullName,
        [
          Validators.required,
          Validators.pattern(
            "^[AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]+ [AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]+(?: [AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]*)*"
          ),
        ],
      ],
      phone: [
        address.phone,
        [
          Validators.required,
          Validators.pattern(/(84|0[3|5|7|8|9])+([0-9]{8})\b/g),
        ],
      ],
      address: [
        address.address,
        [Validators.required, Validators.maxLength(50)],
      ],
      province: [address.idCity, Validators.required],
      district: [address.idDistrict, Validators.required],
      ward: [address.idWard, Validators.required],
      default: [address.default, Validators.required],
    });
    this.addressForm.controls["phone"].setValue(address.phone);
  }
  public onProvinceChange(provinceId: number) {
    this.loadDistricts(provinceId);
    this.addressForm.controls["district"].setValue("");
    this.addressForm.controls["ward"].setValue("");
  }

  private loadDistricts(provinceId: number) {
    this.ghnService.getDistricts(provinceId).subscribe({
      next: (res) => {
        this.districts = res.data;
      },
      error: (err) => {
        this.toastrService.error("Lỗi api huyện");
      },
    });
  }

  public onDistrictChange(districtId: number) {
    this.loadWards(districtId);
    this.addressForm.controls["ward"].setValue("");
  }

  private loadWards(districtId: number) {
    this.ghnService.getWards(districtId).subscribe({
      next: (res) => {
        this.wards = res.data;
      },
      error: (err) => {
        this.toastrService.error("Lỗi api xã");
      },
    });
  }

  openModal() {
    this.addressEdit = null;
    this.addressForm = this.fb.group({
      name: [
        "",
        [
          Validators.required,
          Validators.pattern(
            "^[AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]+ [AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]+(?: [AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]*)*"
          ),
        ],
      ],
      phone: [
        "",
        [
          Validators.required,
          Validators.pattern(/(84|0[3|5|7|8|9])+([0-9]{8})\b/g),
        ],
      ],
      address: ["", [Validators.required, Validators.maxLength(50)]],
      province: ["", Validators.required],
      district: ["", Validators.required],
      ward: ["", Validators.required],
      default: [false, Validators.required],
    });
    this.modalOpen = true;
    if (isPlatformBrowser(this.platformId)) {
      // For SSR
      this.modalService
        .open(this.AddressModal, {
          size: "lg",
          ariaLabelledBy: "address-modal",
          centered: false,
          windowClass: "return-modal",
          backdrop: false,
        })
        .result.then(
          (result) => {
            `Result ${result}`;
          },
          (reason) => {
            this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
          }
        );
    }
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return "by pressing ESC";
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return "by clicking on a backdrop";
    } else {
      return `with: ${reason}`;
    }
  }

  ngOnDestroy() {
    if (this.modalOpen) {
      this.modalService.dismissAll();
    }
  }
  ToggleDashboard() {
    this.openDashboard = !this.openDashboard;
  }
}
