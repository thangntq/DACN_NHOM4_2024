import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from "@angular/common/http";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { LoadingBarHttpClientModule } from "@ngx-loading-bar/http-client";
import { LoadingBarRouterModule } from "@ngx-loading-bar/router";
import { ToastrModule } from "ngx-toastr";
import { TranslateLoader, TranslateModule } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { SharedModule } from "./shared/shared.module";
import { AppRoutingModule } from "./app-routing.module";

import { AppComponent } from "./app.component";
import { ShopComponent } from "./shop/shop.component";
import { PagesComponent } from "./pages/pages.component";
import { JwtInterceptor } from "./shared/helpers/jwt.interceptor";
import { ErrorInterceptor } from "./shared/helpers/error.interceptor";

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, "./assets/i18n/", ".json");
}

@NgModule({
  declarations: [AppComponent, ShopComponent, PagesComponent],
  imports: [
    BrowserModule.withServerTransition({ appId: "serverApp" }),
    BrowserAnimationsModule,
    HttpClientModule,
    NgbModule,
    LoadingBarHttpClientModule,
    LoadingBarRouterModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      progressBar: false,
      enableHtml: true,
    }),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      },
    }),
    SharedModule,
    AppRoutingModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
