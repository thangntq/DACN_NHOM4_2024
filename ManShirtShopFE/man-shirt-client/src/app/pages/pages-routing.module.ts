import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

import { DashboardComponent } from "./account/dashboard/dashboard.component";
import { LoginComponent } from "./account/login/login.component";
import { ForgetPasswordComponent } from "./account/forget-password/forget-password.component";
import { ContactComponent } from "./account/contact/contact.component";
import { AboutUsComponent } from "./about-us/about-us.component";
import { SearchComponent } from "./search/search.component";
import { OrderSuccessComponent } from "./order-success/order-success.component";
import { CompareOneComponent } from "./compare/compare-one/compare-one.component";
import { CompareTwoComponent } from "./compare/compare-two/compare-two.component";
import { CollectionComponent } from "./collection/collection.component";
import { LookbookComponent } from "./lookbook/lookbook.component";
import { ErrorComponent } from "./error/error.component";
import { ComingSoonComponent } from "./coming-soon/coming-soon.component";
import { BlogLeftSidebarComponent } from "./blog/blog-left-sidebar/blog-left-sidebar.component";
import { BlogRightSidebarComponent } from "./blog/blog-right-sidebar/blog-right-sidebar.component";
import { BlogNoSidebarComponent } from "./blog/blog-no-sidebar/blog-no-sidebar.component";
import { BlogDetailsComponent } from "./blog/blog-details/blog-details.component";
import { OrdersComponent } from "./account/orders/orders.component";
import { AddressComponent } from "./account/address/address.component";
import { HistoryComponent } from "./account/history/history.component";
import { OrderdetailComponent } from "./account/orderdetail/orderdetail.component";
import { OrderFailComponent } from "./order-fail/order-fail.component";
import { RegisterComponent } from "./account/register/register.component";
import { AuthGuard } from "../shared/guards/auth.guard";
import { ReturndetailComponent } from "./account/returndetail/returndetail.component";

const routes: Routes = [
  {
    path: "account",
    children: [
      { path: "", component: DashboardComponent },
      { path: "orders", component: OrdersComponent },
      { path: "orders/:id", component: OrderdetailComponent },
      { path: "return/:id", component: ReturndetailComponent },
      { path: "address", component: AddressComponent },
      { path: "history", component: HistoryComponent },
    ],
    canActivate: [AuthGuard],
  },
  {
    path: "login",
    component: LoginComponent,
  },
  {
    path: "register",
    component: RegisterComponent,
  },
  {
    path: "forget/password",
    component: ForgetPasswordComponent,
    canActivate: [AuthGuard],
  },
  {
    path: "contact",
    component: ContactComponent,
  },
  {
    path: "aboutus",
    component: AboutUsComponent,
  },
  {
    path: "search",
    component: SearchComponent,
  },
  {
    path: "order/success",
    component: OrderSuccessComponent,
    canActivate: [AuthGuard],
  },
  {
    path: "order/fail",
    component: OrderFailComponent,
    canActivate: [AuthGuard],
  },
  {
    path: "compare/one",
    component: CompareOneComponent,
  },
  {
    path: "compare/two",
    component: CompareTwoComponent,
  },
  {
    path: "collection",
    component: CollectionComponent,
  },
  {
    path: "lookbook",
    component: LookbookComponent,
  },
  {
    path: "404",
    component: ErrorComponent,
  },
  {
    path: "comingsoon",
    component: ComingSoonComponent,
  },
  {
    path: "blog/left/sidebar",
    component: BlogLeftSidebarComponent,
  },
  {
    path: "blog/right/sidebar",
    component: BlogRightSidebarComponent,
  },
  {
    path: "blog/no/sidebar",
    component: BlogNoSidebarComponent,
  },
  {
    path: "blog/details",
    component: BlogDetailsComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {}
