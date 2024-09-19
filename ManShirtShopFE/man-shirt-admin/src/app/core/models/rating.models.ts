import {  customerrequest} from "./customer.models";
import { ProductRequest } from "./product.model";
import { Order } from "./order.models";

export class rating{
  id : number;
  rating : number;
  content: string;
  customerId: number;
  orderId: number;
  productId: number;
  status: number;
}
