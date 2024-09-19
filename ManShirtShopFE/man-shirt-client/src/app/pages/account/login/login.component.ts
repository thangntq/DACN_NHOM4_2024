import {
  Component,
  Inject,
  OnInit,
  PLATFORM_ID,
  TemplateRef,
  ViewChild,
} from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { AuthenticationService } from "./auth.service";
import { ToastrService } from "ngx-toastr";
import { ModalDismissReasons, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { isPlatformBrowser } from "@angular/common";
import { OrderService } from "src/app/shared/services/order.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
})
export class LoginComponent implements OnInit {
  @ViewChild("forget", { static: false }) ForgetModal: TemplateRef<any>;
  @ViewChild("SendCode", { static: false }) SendCode: TemplateRef<any>;
  @ViewChild("forgetPass", { static: false }) forgetPass: TemplateRef<any>;
  loginForm: FormGroup;
  forgotForm: FormGroup;
  submitted = false;
  isSendCode = false;
  returnUrl: string;
  isShowPassword: boolean = false;
  public closeResult: string;
  public modalOpen: boolean = false;
  email: string;
  countdown: number;
  timer: any;
  countdownEnded: boolean = false;
  formattedCountdown: any;
  isLoadingForgotPassword: boolean;
  verificationCode: string = "";
  isSubmit = false;
  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private toastrService: ToastrService,
    private orderService: OrderService
  ) {
    if (authenticationService.isLogin()) {
      this.router.navigate(["/account"]);
    }
  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required]],
    });
    this.forgotForm = this.formBuilder.group({
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
    this.returnUrl = this.route.snapshot.queryParams["returnUrl"] || "/";
  }
  get f() {
    return this.loginForm.controls;
  }
  onSubmit() {
    this.submitted = true;
    if (this.loginForm.invalid) {
      this.submitted = false;
      return;
    } else {
      this.authenticationService
        .login(this.f.email.value, this.f.password.value)
        .subscribe({
          next: (res) => {
            if (res.code == 403) {
              this.toastrService.error(res.message);
              this.submitted = false;
              return;
            }
            if (!res.roles.includes("ROLE_Customer")) {
              this.toastrService.error("Đăng nhập không thành công");
              this.submitted = false;
              return;
            }
            this.authenticationService.saveToken(res);
            // this.autoLogoutService.lastAction = Date.now();
            this.router.navigate([this.returnUrl]);
          },
          error: (err) => {
            this.toastrService.error("Đăng nhập không thành công");
            this.submitted = false;
          },
        });
    }
  }
  isEmailValid(): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email);
  }
  sendCode() {
    if (!this.isEmailValid()) {
      this.toastrService.info("Vui lòng nhập đúng email");
      return;
    }
    this.isSendCode = true;
    this.orderService.sendCode(this.email).subscribe((res) => {
      if (res.message == "Gửi code thành công") {
        this.isSendCode = false;
        this.ngOnDestroy();
        this.openModalSendCode();
      } else {
        this.toastrService.info(res.message);
        this.isSendCode = false;
      }
    });
  }
  openModal() {
    this.modalOpen = true;
    if (isPlatformBrowser(this.platformId)) {
      // For SSR
      this.modalService
        .open(this.ForgetModal, {
          size: "md",
          ariaLabelledBy: "forget-modal",
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
  openModalSendCode() {
    if (isPlatformBrowser(this.platformId)) {
      this.startCountdown();
      this.modalService
        .open(this.SendCode, {
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
  openModalForgetPass() {
    if (isPlatformBrowser(this.platformId)) {
      this.startCountdown();
      this.modalService
        .open(this.forgetPass, {
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
    this.orderService.sendCode(this.email).subscribe({
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
  confirmForgot() {
    if (this.verificationCode && this.verificationCode.length == 6) {
      this.isLoadingForgotPassword = true;
      this.orderService.confirmCode(this.verificationCode).subscribe({
        next: (res) => {
          if (res.message) {
            this.ngOnDestroy();
            this.openModalForgetPass();
          } else {
            this.toastrService.info(res.error);
          }
          this.isLoadingForgotPassword = false;
        },
      });
    }
  }
  submitForgetPass() {
    this.isSubmit = true;
    if (this.forgotForm.invalid) {
      return;
    }
    if (!this.passwordMatchValidator()) {
      return;
    }
    const request: any = {};
    request.newPassword = this.forgotForm.get("password").value;
    request.confirmPassword = this.forgotForm.get("confirmPassword").value;
    request.code = this.verificationCode;
    this.isSendCode = true;
    this.orderService.forgotPass(request).subscribe({
      next: (value) => {
        if (value.error) {
          this.toastrService.info(value.error);
          this.isSendCode = false;
        }
        if (value.message) {
          this.ngOnDestroy();
          this.toastrService.success(value.message);
          this.isSendCode = false;
        }
      },
      error: (err) => {
        this.isSendCode = false;
      },
    });
  }
  passwordMatchValidator() {
    const password = this.forgotForm.get("password").value;
    const confirmPassword = this.forgotForm.get("confirmPassword").value;

    if (password !== confirmPassword) {
      return false;
    } else {
      return true;
    }
  }
}
