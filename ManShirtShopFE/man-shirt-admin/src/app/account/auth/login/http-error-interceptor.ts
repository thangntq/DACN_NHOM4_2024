// import { Injectable } from "@angular/core";
// import {
//   HttpErrorResponse,
//   HttpEvent,
//   HttpHandler,
//   HttpInterceptor,
//   HttpRequest,
//   HttpResponse,
// } from "@angular/common/http";
// import "rxjs/add/operator/do";
// import { environment } from "src/environments/environment";
// import { Cookie } from "ng2-cookies";
// import { Observable } from "rxjs/";
// import { AutoLogoutService } from "./auto-logout.service";
// import { AuthenticationService } from "src/app/core/services/auth.service";
// import { AuthConstant } from "src/app/core/constant/auth.constant";

// @Injectable()
// export class HttpErrorInterceptor implements HttpInterceptor {
//   private autoLogoutService: AutoLogoutService;

//   public constructor(private authService: AuthenticationService) {
//     this.autoLogoutService = new AutoLogoutService(authService);
//   }

//   intercept(
//     request: HttpRequest<any>,
//     next: HttpHandler
//   ): Observable<HttpEvent<any>> {
//     return next.handle(request).do(
//       (event: HttpEvent<any>) => {
//         if (event instanceof HttpResponse) {
//           this.autoLogoutService.reset();
//           this.autoLogoutService.initInterval();
//         }
//       },
//       (err: any) => {
//         if (err instanceof HttpErrorResponse) {
//           const revokeTokenUrl =
//             environment.apiUrl +
//             "/oauth/token?access_token=" +
//             Cookie.get(AuthConstant.ACCESS_TOKEN_KEY);
//           const loginUrl = environment.apiUrl + "/oauth/token";
//           if (
//             err.status === 401 &&
//             request.url !== revokeTokenUrl &&
//             request.url !== loginUrl
//           ) {
//             this.authService.logOut();
//           }

//           if (err.status === 400) {
//             console.error(err.error);
//           }
//         }
//       }
//     );
//   }
// }
