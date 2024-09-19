import { ProductDetailRespone } from "./productDetail.model";

export class cart {
  id: number;
  name: string;
  cartDetail: cartDetail[];
}

export class cartDetail {
  quantity: number;
  detail: ProductDetailRespone;
}
