import { Injectable } from "@angular/core";
import {
  Router,
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
} from "@angular/router";
import { AuthenticationService } from "../services/auth.service";

@Injectable({ providedIn: "root" })
export class RoleGuard implements CanActivate {
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    this.authenticationService.getUser().subscribe({
      next: (res) => {
        const roles = route.data;
        if (roles && !res[0].roles?.includes(roles.roles)) {
          this.router.navigate(["/"]);
          return false;
        }
        return true;
      },
      error: (err) => {
        return false;
      },
    });

    // not logged in so redirect to login page with the return url
    // this.router.navigate(["account/login"], {
    //   queryParams: { returnUrl: state.url },
    // });
    return true;
  }
}
