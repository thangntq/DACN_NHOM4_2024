import { ProductRequest } from "./product.model";
import { discount } from "./discount.models";

export class discountproduct {
  id: number;
  percent: number;
  productId: [];
  discountId: number;

}

export class ProductDiscount {
  id: number;
  percent: number;
  productId: number;
  discountId: number;
  
}
