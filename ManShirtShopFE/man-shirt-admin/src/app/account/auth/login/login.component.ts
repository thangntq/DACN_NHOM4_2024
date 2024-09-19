import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { AuthenticationService } from "../../../core/services/auth.service";
import { ActivatedRoute, Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { AutoLogoutService } from "./auto-logout.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
})

/**
 * Login component
 */
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  submitted = false;
  error = "";
  returnUrl: string;
  year: number = new Date().getFullYear();
  loading: boolean;
  showPassword = false;
  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private toastrService: ToastrService,
    private autoLogoutService: AutoLogoutService
  ) {}

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      email: ["admin@gmail.com", [Validators.required, Validators.email]],
      password: ["123456", [Validators.required]],
    });
    this.returnUrl = this.route.snapshot.queryParams["returnUrl"] || "/";
  }
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    this.loading = true;
    if (this.loginForm.invalid) {
      return;
    } else {
      this.authenticationService
        .login(this.f.email.value, this.f.password.value)
        .subscribe({
          next: (res) => {
            console.log(res);

            if (
              res.roles.includes("ROLE_Admin") ||
              res.roles.includes("ROLE_Manager")
            ) {
              this.authenticationService.saveToken(res);
              this.autoLogoutService.lastAction = Date.now();
              this.router.navigate([this.returnUrl]);
            } else {
              this.toastrService.error("Đăng nhập không thành công");
              this.loading = false;
            }
          },
          error: (err) => {
            this.toastrService.error("Đăng nhập không thành công");
            this.loading = false;
          },
        });
    }
  }
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}
