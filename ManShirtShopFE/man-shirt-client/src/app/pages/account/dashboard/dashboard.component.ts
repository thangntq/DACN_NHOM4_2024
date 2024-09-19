import {
  Component,
  Inject,
  OnInit,
  PLATFORM_ID,
  TemplateRef,
  ViewChild,
} from "@angular/core";
import { AuthenticationService } from "../login/auth.service";
import { OrderService } from "src/app/shared/services/order.service";
import { ModalDismissReasons, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { isPlatformBrowser } from "@angular/common";
import { ToastrService } from "ngx-toastr";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: "app-dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.component.scss"],
})
export class DashboardComponent implements OnInit {
  public openDashboard: boolean = false;
  @ViewChild("SendCode", { static: false }) SendCode: TemplateRef<any>;
  @ViewChild("forgetPass", { static: false }) forgetPass: TemplateRef<any>;
  userInfo: any;
  verificationCode: string = "";
  formattedCountdown: any;
  countdownEnded: boolean = false;
  countdown: number;
  timer: any;
  isLoadingForgotPassword: boolean;
  public modalOpen: boolean = false;
  public closeResult: string;
  forgotForm: FormGroup;
  isSubmit = false;
  isSendCode = false;
  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private authService: AuthenticationService,
    private orderService: OrderService,
    private modalService: NgbModal,
    private toastrService: ToastrService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.authService.getUser().subscribe({
      next: (value) => {
        this.userInfo = value[0];
      },
      error: (error) => {},
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
    // this.orderService.forgotPass(request).subscribe({
    //   next: (value) => {
    //     if (value.error) {
    //       this.toastrService.info(value.error);
    //       this.isSendCode = false;
    //     }
    //     if (value.message) {
    //       this.ngOnDestroy();
    //       this.toastrService.success(value.message);
    //       this.isSendCode = false;
    //     }
    //   },
    //   error: (err) => {
    //     this.isSendCode = false;
    //   },
    // });
  }
  ToggleDashboard() {
    this.openDashboard = !this.openDashboard;
  }
  maskEmail(email: string): string {
    const username = email.substring(0, email.indexOf("@"));
    const domain = email.substring(email.indexOf("@"));
    const maskedUsername =
      username.substring(0, 7) + "*".repeat(username.length - 7);
    const maskedEmail = maskedUsername + domain;
    return maskedEmail;
  }
  sendAgain() {
    this.orderService.sendCode(this.userInfo?.email).subscribe({
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

  confirmForgot() {
    if (this.verificationCode && this.verificationCode.length == 6) {
      this.isLoadingForgotPassword = true;
      this.orderService.confirmCode(this.verificationCode).subscribe({
        next: (res) => {
          if (res.message) {
            this.ngOnDestroy();
            this.verificationCode = "";
            this.isSubmit = false;
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
            this.openModalForgetPass();
          } else {
            this.toastrService.info(res.error);
          }
          this.isLoadingForgotPassword = false;
        },
      });
    }
  }
  sendCode() {
    this.openModalSendCode();
    this.orderService.sendCode(this.userInfo?.email);
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
    this.modalService.dismissAll();
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
}
