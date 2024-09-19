import { Component, OnInit, Input, HostListener } from "@angular/core";
import { Router } from "@angular/router";
import { AuthenticationService } from "src/app/pages/account/login/auth.service";

@Component({
  selector: "app-header-one",
  templateUrl: "./header-one.component.html",
  styleUrls: ["./header-one.component.scss"],
})
export class HeaderOneComponent implements OnInit {
  @Input() class: string;
  @Input() themeLogo: string = "assets/images/logos/logo.png"; // Default Logo
  @Input() topbar: boolean = true; // Default True
  @Input() sticky: boolean = false; // Default false
  userInfo: any;
  public stick: boolean = false;
  isLogin: boolean;
  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isLogin = this.authService.isLogin();
    // if(this.authService.isLogin()){
    //   this.router.navigate(["/account"]);
    // }
    if (this.isLogin) {
      this.authService.getUser().subscribe({
        next: (value) => {
          this.userInfo = value[0];
        },
        error: (error) => {},
      });
    }
  }

  // @HostListener Decorator
  @HostListener("window:scroll", [])
  onWindowScroll() {
    let number =
      window.pageYOffset ||
      document.documentElement.scrollTop ||
      document.body.scrollTop ||
      0;
    if (number >= 150 && window.innerWidth > 400) {
      this.stick = true;
    } else {
      this.stick = false;
    }
  }
  logout() {
    this.authService.logout();
    this.isLogin = this.authService.isLogin();
  }
}
