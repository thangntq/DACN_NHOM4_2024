import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Cookie } from "ng2-cookies";
import { Router } from "@angular/router";
@Injectable({ providedIn: "root" })
export class AuthenticationService {
  constructor(private http: HttpClient, private router: Router) {}
  isLogin(): boolean {
    if (!Cookie.check("access_token")) {
      return false;
    }
    return true;
  }
  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/auth/login`, {
      email,
      password,
    });
  }
  saveToken(token: any) {
    const expireDate = new Date().getTime() + 1000 * token.expires_in;
    Cookie.set("access_token", token.accessToken, expireDate);
  }
  logOut() {
    if (Cookie.check("access_token")) {
      Cookie.delete("access_token");
      this.router.navigate(["/account/login"]);
    }
  }

  checkCredentials(state): boolean {
    if (!Cookie.check("access_token")) {
      this.router.navigate(["/account/login"], {
        queryParams: { returnUrl: state.url },
      });
      return false;
    }
    return true;
  }
  getUser(): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/employee/get_user`);
  }
}
