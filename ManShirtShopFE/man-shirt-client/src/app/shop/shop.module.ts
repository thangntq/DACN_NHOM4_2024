import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { NgxPayPalModule } from "ngx-paypal";
import { Ng5SliderModule } from "ng5-slider";
import { InfiniteScrollModule } from "ngx-infinite-scroll";
import { SharedModule } from "../shared/shared.module";
import { ShopRoutingModule } from "./shop-routing.module";
import { LightboxModule } from "ngx-lightbox";
// Product Details Components
import { ProductNoSidebarComponent } from "./product/sidebar/product-no-sidebar/product-no-sidebar.component";

// Product Details Widgest Components
import { ServicesComponent } from "./product/widgets/services/services.component";
import { CountdownComponent } from "./product/widgets/countdown/countdown.component";
import { SocialComponent } from "./product/widgets/social/social.component";
import { StockInventoryComponent } from "./product/widgets/stock-inventory/stock-inventory.component";

// Collection Components
import { CollectionLeftSidebarComponent } from "./collection/collection-left-sidebar/collection-left-sidebar.component";

// Collection Widgets
import { GridComponent } from "./collection/widgets/grid/grid.component";
import { PaginationComponent } from "./collection/widgets/pagination/pagination.component";
import { BrandsComponent } from "./collection/widgets/brands/brands.component";
import { ColorsComponent } from "./collection/widgets/colors/colors.component";
import { SizeComponent } from "./collection/widgets/size/size.component";
import { PriceComponent } from "./collection/widgets/price/price.component";

import { CartComponent } from "./cart/cart.component";
import { WishlistComponent } from "./wishlist/wishlist.component";
import { CompareComponent } from "./compare/compare.component";
import { CheckoutComponent } from "./checkout/checkout.component";
import { ErrorComponent } from "./checkout/error/error.component";
import { DesignComponent } from "./collection/widgets/design/design.component";
import { CategoryComponent } from "./collection/widgets/category/category.component";
import { MaterialComponent } from "./collection/widgets/material/material.component";
import { FormComponent } from "./collection/widgets/form/form.component";
import { CollarComponent } from "./collection/widgets/collar/collar.component";
import { SleeveComponent } from "./collection/widgets/sleeve/sleeve.component";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
@NgModule({
  declarations: [
    ProductNoSidebarComponent,
    ServicesComponent,
    CountdownComponent,
    SocialComponent,
    StockInventoryComponent,
    CollectionLeftSidebarComponent,
    GridComponent,
    PaginationComponent,
    BrandsComponent,
    ColorsComponent,
    SizeComponent,
    PriceComponent,
    CartComponent,
    WishlistComponent,
    CompareComponent,
    CheckoutComponent,
    ErrorComponent,
    DesignComponent,
    CategoryComponent,
    MaterialComponent,
    FormComponent,
    CollarComponent,
    SleeveComponent,
  ],
  imports: [
    CommonModule,
    NgxPayPalModule,
    Ng5SliderModule,
    InfiniteScrollModule,
    ShopRoutingModule,
    SharedModule,
    NgbModule,
    LightboxModule,
  ],
})
export class ShopModule {}
