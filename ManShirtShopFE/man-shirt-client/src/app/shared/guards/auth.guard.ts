import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Route } from '@angular/router';
import { AuthenticationService } from 'src/app/pages/account/login/auth.service';


@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
    constructor(private authenticationService: AuthenticationService) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        return this.checkLogin(state);
      }
    
      canLoad(route: Route,state: RouterStateSnapshot): boolean {
        return this.checkLogin(state);
      }
    
      private checkLogin(state): boolean {
        return this.authenticationService.checkCredentials(state);
      }
}
