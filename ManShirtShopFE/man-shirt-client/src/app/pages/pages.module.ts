import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { GalleryModule } from "@ks89/angular-modal-gallery";
import { SharedModule } from "../shared/shared.module";
import { PagesRoutingModule } from "./pages-routing.module";

// Pages Components
import { CartComponent } from "./account/cart/cart.component";
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
// Blog Components
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
import { ReturndetailComponent } from "./account/returndetail/returndetail.component";
// Portfolio Components

@NgModule({
  declarations: [
    CartComponent,
    DashboardComponent,
    LoginComponent,
    ForgetPasswordComponent,
    ContactComponent,
    AboutUsComponent,
    SearchComponent,
    OrderSuccessComponent,
    CompareOneComponent,
    CompareTwoComponent,
    CollectionComponent,
    LookbookComponent,
    ErrorComponent,
    ComingSoonComponent,
    BlogLeftSidebarComponent,
    BlogRightSidebarComponent,
    BlogNoSidebarComponent,
    BlogDetailsComponent,
    OrdersComponent,
    AddressComponent,
    HistoryComponent,
    OrderdetailComponent,
    OrderFailComponent,
    RegisterComponent,
    ReturndetailComponent,
  ],
  imports: [
    CommonModule,
    GalleryModule.forRoot(),
    SharedModule,
    PagesRoutingModule,
  ],
})
export class PagesModule {}
