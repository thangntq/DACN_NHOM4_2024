export class voucherResponse{
  id: number;
  name: string;
  startDate: Date;
  endDate:Date;
  description: string;
  status: number;
  discount: number; //số tiền hoặc phần trăm voucher
  code:string;      //code
  type: boolean;    //giảm theo phần trăm hoặc theo số tiền
  min?: number;     //giá trị đơn hàng tối thiểu để sử dụng voucher
  max?: number;     //số tiền tối đa giảm được khi sử dụng voucher
}


export class voucherRequest{
  id: number;
  name: string;
  startDate: Date;
  endDate:Date;
  description: string;
  status: number;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  discount: number;
  code:string;      //code
  type: boolean;    //giảm theo phần trăm hoặc theo số tiền
  min?: number;     //giá trị đơn hàng tối thiểu để sử dụng voucher
  max?: number;     //số tiền tối đa giảm được khi sử dụng voucher
}
