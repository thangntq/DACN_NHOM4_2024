import { color } from "./color.models";
import { size } from "./size.models";

export class ProductDetailRespone {
  id: number;
  name: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  barCode: String;
  quantity: number;
  color: color;
  size: size;
  productId: number;
  status: number;
}
export class ProductDetailRequest {
  id: number;
  barCode: String;
  quantity: number;
  color: number;
  size: number;
  productId: number;
}
export class ProductDetail {
  id: number;
  quantity: number;
  color: number;
  size: number;
  product: number;
  lstProductImage: ProductImage[];
  status: number;
}

export class ProductImage {
  id: number;
  mainImage: boolean;
  urlImage: string;
  colorId: number;
  productId: number;
  status: number;
}
