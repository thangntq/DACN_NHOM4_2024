import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

import { DefaultComponent } from "./dashboards/default/default.component";
import { materialComponent } from "./specifications/material/material.component";
import { sleeveComponent } from "./specifications/sleeve/sleeve.component";
import { collarComponent } from "./specifications/collar/collar.component";
import { colorComponent } from "./specifications/color/color.component";
import { designComponent } from "./specifications/design/design.component";
import { formComponent } from "./specifications/form/form.component";
import { sizeComponent } from "./specifications/size/size.component";
import { discountComponent } from "./discount/discount.component";
import { voucherComponent } from "./voucher/voucher.component";
import { ProductComponent } from "./product/product.component";
import { OrderComponent } from "./order/order.component";
import { ContactComponent } from "./contactShop/contact.component";
import { employeeComponent } from "./employee/employee.component";
import { categoryoriComponent } from "./categoryorigin/categoryori.component";
import { categoriComponent } from "./categori/categori.component";
import { customerComponent } from "./customer/customer.component";
import { RatingComponent } from "./rating/rating.component";
import { doanhthuComponent } from "./statistical/Doanhthu/doanhthu.component";

import { sanphamComponent } from "./statistical/Sanpham/sanpham.component";
import { ExchangeComponent } from "./exchange/exchange.component";
import { RoleGuard } from "../core/guards/role.guard";
import { Role } from "../core/models/auth.models";
const routes: Routes = [
  { path: "", redirectTo: "dashboard" },
  { path: "dashboard", component: DefaultComponent },
  { path: "orders", component: OrderComponent },
  { path: "products", component: ProductComponent },
  {
    path: "discounts",
    component: discountComponent,
    canActivate: [RoleGuard],
    data: { roles: Role.Admin },
  },
  {
    path: "vouchers",
    component: voucherComponent,
    canActivate: [RoleGuard],
    data: { roles: Role.Admin },
  },
  {
    path: "contact",
    component: ContactComponent,
    canActivate: [RoleGuard],
    data: { roles: Role.Admin },
  },
  { path: "customer", component: customerComponent },
  {
    path: "employee",
    component: employeeComponent,
    canActivate: [RoleGuard],
    data: { roles: Role.Admin },
  },
  {
    path: "catergoryori",
    component: categoryoriComponent,
    canActivate: [RoleGuard],
    data: { roles: Role.Admin },
  },
  {
    path: "categorys",
    component: categoriComponent,
    canActivate: [RoleGuard],
    data: { roles: Role.Admin },
  },

  { path: "rating", component: RatingComponent },
  { path: "exchange", component: ExchangeComponent },

  {
    path: "attributes",
    children: [
      {
        path: "material",
        component: materialComponent,
      },
      {
        path: "collar",
        component: collarComponent,
      },
      {
        path: "sleeve",
        component: sleeveComponent,
      },
      {
        path: "color",
        component: colorComponent,
      },
      {
        path: "design",
        component: designComponent,
      },
      {
        path: "form",
        component: formComponent,
      },
      {
        path: "size",
        component: sizeComponent,
      },
    ],
  },

  {
    path: "STATISTICAL",
    children: [
      {
        path: "sanpham",
        component: sanphamComponent,
      },
      {
        path: "doanhthu",
        component: doanhthuComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {}
