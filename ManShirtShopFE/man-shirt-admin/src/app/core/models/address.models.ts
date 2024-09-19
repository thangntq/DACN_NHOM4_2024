import { customerrequest } from "./customer.models";
export class adress{
  id: number;
  address: string;
  cityName: string;
  districtName:string;
  wardName:string;
  other:string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
  customerResponse : customerrequest;
}
