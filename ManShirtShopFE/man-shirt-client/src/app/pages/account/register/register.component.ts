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
import { Router } from "@angular/router";
import { ModalDismissReasons, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { ToastrService } from "ngx-toastr";
import { OrderService } from "src/app/shared/services/order.service";

@Component({
  selector: "app-register",
  templateUrl: "./register.component.html",
  styleUrls: ["./register.component.scss"],
})
export class RegisterComponent implements OnInit {
  isShowPassword: boolean = false;
  verificationCode: string = "";
  isShowConfirmPassword: boolean = false;
  @ViewChild("sendCode", { static: false }) sendCode: TemplateRef<any>;
  public closeResult: string;
  public modalOpen: boolean = false;
  registerForm: FormGroup;
  isSubmit = false;
  isLoading = false;
  isLoadingRegister = false;
  countdown: number;
  timer: any;
  formattedCountdown: any;
  countdownEnded: boolean = false;
  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private orderService: OrderService,
    private toastrService: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      fullName: ["", Validators.required],
      phoneNumber: ["", Validators.required],
      email: ["", [Validators.required, Validators.email]],
      password: [
        "",
        [
          Validators.required,
          Validators.minLength(6),
          Validators.maxLength(50),
        ],
      ],
      confirmPassword: ["", Validators.required],
    });
  }
  confirmRegister() {
    if (this.verificationCode && this.verificationCode.length == 6) {
      this.isLoadingRegister = true;
      const request: any = {};
      request.fullname = this.registerForm.get("fullName").value;
      request.phone = this.registerForm.get("phoneNumber").value;
      request.password = this.registerForm.get("password").value;
      request.confirmPassword = this.registerForm.get("confirmPassword").value;
      request.code = this.verificationCode;
      this.orderService.register(request).subscribe({
        next: (value) => {
          if (value.message) {
            this.ngOnDestroy();
            this.toastrService.success("Đăng kí thành công");
            window.location.href = "/login";
          } else {
            this.toastrService.info("Mã code không hợp lệ");
            this.isLoadingRegister = false;
          }
        },
      });
    }
  }
  passwordMatchValidator() {
    const password = this.registerForm.get("password").value;
    const confirmPassword = this.registerForm.get("confirmPassword").value;

    if (password !== confirmPassword) {
      return false;
    } else {
      return true;
    }
  }
  openModal() {
    if (isPlatformBrowser(this.platformId)) {
      this.startCountdown();
      this.modalService
        .open(this.sendCode, {
          size: "md",
          ariaLabelledBy: "address-modal",
          centered: true,
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
  register(): void {
    this.isSubmit = true;
    if (this.registerForm.invalid) {
      return;
    }
    if (!this.passwordMatchValidator()) {
      return;
    }
    this.isLoading = true;
    this.orderService
      .sendCodeDK(this.registerForm.get("email").value)
      .subscribe({
        next: (value) => {
          if (value.error) {
            this.toastrService.info(value.error);
            this.isLoading = false;
          }
          if (value.message) {
            this.openModal();
            this.isLoading = false;
          }
        },
        error: (err) => {
          this.openModal();
          this.isLoading = false;
        },
      });
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
  maskEmail(email: string): string {
    const username = email.substring(0, email.indexOf("@"));
    const domain = email.substring(email.indexOf("@"));
    const maskedUsername =
      username.substring(0, 7) + "*".repeat(username.length - 7);
    const maskedEmail = maskedUsername + domain;
    return maskedEmail;
  }
  startCountdown(): void {
    this.countdown = 60;

    this.timer = setInterval(() => {
      if (this.countdown > 0) {
        this.countdown--;
        this.formatCountdown();
      } else {
        clearInterval(this.timer);
        this.countdownEnded = true;
      }
    }, 1000);
  }

  formatCountdown(): void {
    this.formattedCountdown = `Vui lòng chờ ${this.countdown} giây để gửi lại.`;
  }
  sendAgain() {
    this.orderService
      .sendCodeDK(this.registerForm.get("email").value)
      .subscribe({
        next: (value) => {
          this.countdownEnded = false;
          this.startCountdown();
        },
        error: (err) => {
          this.countdownEnded = false;
          this.startCountdown();
        },
      });
  }
}
