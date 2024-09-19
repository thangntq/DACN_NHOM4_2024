import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { Ng5SliderModule } from "ng5-slider";

import {
  NgbModule,
  NgbNavModule,
  NgbPaginationModule,
  NgbDropdownModule,
  NgbModalModule,
  NgbTooltipModule,
  NgbCollapseModule,
} from "@ng-bootstrap/ng-bootstrap";
import { NgApexchartsModule } from "ng-apexcharts";
import { SimplebarAngularModule } from "simplebar-angular";
import { LightboxModule } from "ngx-lightbox";
import { WidgetModule } from "../shared/widget/widget.module";
import { UIModule } from "../shared/ui/ui.module";
import { PagesRoutingModule } from "./pages-routing.module";
import { DashboardsModule } from "./dashboards/dashboards.module";
import { ChartModule } from "./chart/chart.module";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { Ng2SearchPipeModule } from "ng2-search-filter";
import { SpecificationsModule } from "./specifications/specifications.module";
import { CategoryComponent } from "./category/category.component";
import { MatTableModule } from "@angular/material/table";
import { MatIconModule } from "@angular/material/icon";
import { discountComponent } from "./discount/discount.component";
import { voucherComponent } from "./voucher/voucher.component";
import { ProductComponent } from "./product/product.component";
import { ContactComponent } from "./contactShop/contact.component";
import { employeeComponent } from "./employee/employee.component";
import { categoryoriComponent } from "./categoryorigin/categoryori.component";
import { categoriComponent } from "./categori/categori.component";
import { customerComponent } from "./customer/customer.component";
import { RatingComponent } from "./rating/rating.component";
import { sanphamComponent } from "./statistical/Sanpham/sanpham.component";
import { doanhthuComponent } from "./statistical/Doanhthu/doanhthu.component";
import { NgxMaskModule } from "ngx-mask";
import { NgSelectModule } from "@ng-select/ng-select";
import { DropzoneModule } from "ngx-dropzone-wrapper";
import { OrderComponent } from "./order/order.component";
import { defineElement } from "lord-icon-element";
import lottie from "lottie-web";
import { PosComponent } from "./pos/pos.component";


import { NgxLoadingModule } from "ngx-loading";
import { MatTabsModule } from "@angular/material/tabs";
import { NgxBarcodeScannerModule } from "@eisberg-labs/ngx-barcode-scanner";
import { NgbAccordionModule } from "@ng-bootstrap/ng-bootstrap";
import { BillComponent } from "./bill/bill.component";
import { CKEditorModule } from "@ckeditor/ckeditor5-angular";
import { ExchangeComponent } from './exchange/exchange.component';
@NgModule({
  declarations: [
    CategoryComponent,
    discountComponent,
    voucherComponent,
    ProductComponent,
    OrderComponent,
    ContactComponent,
    employeeComponent,
    categoryoriComponent,
    categoriComponent,
    customerComponent,
    PosComponent,
    BillComponent,
    RatingComponent,
    sanphamComponent,
    doanhthuComponent,
    ExchangeComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    NgbModule,
    NgbDropdownModule,
    SpecificationsModule,
    NgbModalModule,
    PagesRoutingModule,
    NgApexchartsModule,
    ReactiveFormsModule,
    DashboardsModule,
    NgSelectModule,
    HttpClientModule,
    UIModule,
    ChartModule,
    WidgetModule,
    NgbNavModule,
    NgbTooltipModule,
    NgbCollapseModule,
    SimplebarAngularModule,
    LightboxModule,
    MatTableModule,
    MatIconModule,
    NgbPaginationModule,
    Ng2SearchPipeModule,
    Ng5SliderModule,
    NgxMaskModule,
    DropzoneModule,
    MatTabsModule,
    NgxLoadingModule.forRoot({}),
    NgxBarcodeScannerModule,
    NgbAccordionModule,
    CKEditorModule,
  ],
})
export class PagesModule {
  constructor() {
    defineElement(lottie.loadAnimation);
  }
}
