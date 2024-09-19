import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

import { ShopComponent } from "./shop/shop.component";
import { PagesComponent } from "./pages/pages.component";
import { CheckoutComponent } from "./shop/checkout/checkout.component";
import { ErrorComponent } from "./shop/checkout/error/error.component";
import { AuthGuard } from "./shared/guards/auth.guard";

const routes: Routes = [
  {
    path: "",
    loadChildren: () => import("./home/home.module").then((m) => m.HomeModule),
  },
  {
    path: "shop",
    component: ShopComponent,
    loadChildren: () => import("./shop/shop.module").then((m) => m.ShopModule),
  },
  {
    path: "",
    component: PagesComponent,
    loadChildren: () =>
      import("./pages/pages.module").then((m) => m.PagesModule),
  },
  {
    path: "checkout/:code",
    component: CheckoutComponent,
    canActivate: [AuthGuard],
  },
  {
    path: "checkouts/error",
    component: ErrorComponent,
    canActivate: [AuthGuard],
  },
  {
    path: "**", // Navigate to Home Page if not found any page
    redirectTo: "",
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      initialNavigation: "enabled",
      useHash: false,
      anchorScrolling: "enabled",
      scrollPositionRestoration: "enabled",
      relativeLinkResolution: "legacy",
    }),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
