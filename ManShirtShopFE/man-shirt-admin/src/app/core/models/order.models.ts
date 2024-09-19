export class Order {
  id: number;
  code: string;
  freight: number;
  shipName: string;
  address: string;
  cityName: string;
  districtName: string;
  wardName: string;
  shipPhone: string;
  note: string;
  paymentType: string;
  total: number;
  statusPay: number;
  saleForm: boolean;
  idDistrict: number;
  idWard: string;
  voucher: number;
  employeed: number;
  customer: number;
  status: number;
  lstProductDetail: ProductDetail[];
  constructor() {}
}

export class ProductDetail {
  id: number;
  quantity: number;
  constructor() {}
}
