export class Product {
  id: number;
  code: string;
  name: string;
  price: number;
  description: string;
  rating: number;
  new?: boolean;
  sale?: boolean;
  weight: number;
  discount: number;
  soluongdaban: number;
  productDetail: ProductDetail[];
  productImage: ProductImage[];
}
export class category {
  id: number;
  name: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
  categoryId: number;
  category: category[];
}
export class design {
  id: number;
  name: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
}
export class form {
  id: number;
  name: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
}
export class material {
  id: number;
  name: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
}
export class sleeve {
  id: number;
  name: string;
  diameter: number;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
}
export class collar {
  id: number;
  name: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
}
export class discount {
  id: number;
  name: string;
  startDate: Date;
  endDate: Date;
  description: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
}
export class ProductDetail {
  id: number;
  barCode: String;
  quantity: number;
  color: color;
  size: size;
  productId: number;
  status: number;
}
export class color {
  id: number;
  name: string;
}

export class size {
  id: number;
  code: string;
}
export class ProductImage {
  id: number;
  urlImage: string;
  mainImage: boolean;
  color: color;
  productId: number;
  status: number;
}
export class cart {
  id: number;
  name: string;
  cartDetail: cartDetail[];
}

export class cartDetail {
  quantity: number;
  detail: ProductDetail;
}
