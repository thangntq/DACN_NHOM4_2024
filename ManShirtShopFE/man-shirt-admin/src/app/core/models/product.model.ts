import { categoryResponse } from "./category.models";
import { collar } from "./collar.models";
import { design } from "./design.models";
import { ProductDiscount } from "./discountproduct.models";
import { form } from "./form.models";
import { material } from "./material.models";
import {
  ProductDetailRequest,
  ProductDetailRespone,
} from "./productDetail.model";
import {
  ProductImageRequest,
  ProductImageResponse,
} from "./productImage.model";
import { sleeve } from "./sleeve.models";

export class ProductRespone {
  id: number;
  name: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  total: number;
  price: number;
  description: string;
  weight: number;
  category: categoryResponse;
  design: design;
  form: form;
  material: material;
  sleeve: sleeve;
  collar: collar;
  productDiscount: ProductDiscount;
  status: number;
  productDetail: ProductDetailRespone[];
  productImage: ProductImageResponse[];
}
export class ProductRequest {
  id: number;
  name: string;
  price: number;
  description: string;
  weight: number;
  category: number;
  design: number;
  form: number;
  material: number;
  sleeve: number;
  collar: number;
  discount: number;
  productDetail: ProductDetailRequest[];
  productImage: ProductImageRequest[];
  status: number;
}
export class FilterData {
  size: number[];
  form: number[];
  color: number[];
  category: number[];
  material: number[];
  design: number[];
  sleeve: number[];
  collar: number[];
  status: number;
  discount: number;
  low: number;
  high: number;
}
