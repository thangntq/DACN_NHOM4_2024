import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

import { ProductNoSidebarComponent } from "./product/sidebar/product-no-sidebar/product-no-sidebar.component";
import { CollectionLeftSidebarComponent } from "./collection/collection-left-sidebar/collection-left-sidebar.component";
import { CartComponent } from "./cart/cart.component";
import { WishlistComponent } from "./wishlist/wishlist.component";
import { CompareComponent } from "./compare/compare.component";

import { Resolver } from "../shared/services/resolver.service";
import { DiscountResolver } from "../shared/services/discount-resolver.service";

const routes: Routes = [
  {
    path: "product/:slug",
    component: ProductNoSidebarComponent,
    resolve: {
      data: Resolver,
    },
  },
  {
    path: "san-pham-moi",
    component: CollectionLeftSidebarComponent,
  },
  {
    path: "giam-gia-sieu-lon",
    component: CollectionLeftSidebarComponent,
  },
  {
    path: "ban-chay-nhat",
    component: CollectionLeftSidebarComponent,
  },
  {
    path: "",
    component: CollectionLeftSidebarComponent,
  },
  {
    path: "sale/:discount",
    component: CollectionLeftSidebarComponent,
    resolve: {
      data: DiscountResolver,
    },
  },
  {
    path: "cart",
    component: CartComponent,
  },
  {
    path: "wishlist",
    component: WishlistComponent,
  },
  {
    path: "compare",
    component: CompareComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ShopRoutingModule {}
