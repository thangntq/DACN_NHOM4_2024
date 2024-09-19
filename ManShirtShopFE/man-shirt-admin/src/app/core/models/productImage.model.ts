import { color } from "./color.models";

export class ProductImageResponse {
  id: number;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  urlImage: string;
  mainImage: boolean;
  color: color;
  productId: number;
  status: number;
}
export class ProductImageRequest {
  id: number;
  mainImage: boolean;
  urlImage: string;
  colorId: number;
  productId: number;
  status: number;
}
