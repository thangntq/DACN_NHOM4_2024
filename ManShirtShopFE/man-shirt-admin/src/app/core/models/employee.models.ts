export class employeeRequest{
  id: number;
  email :string;
  password: string;
  fullname: string;
  birthDate: Date;
  address: string;
  phone: string;
  note: string;
  role: number;
  photo: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
}

export class employeeRespone{

  id: number;
  email :string;
  password: string;
  fullname: string;
  birthDate: Date;
  address: string;
  phone: string;
  note: string;
  role: any;
  photo: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
}

export class roleEmployee{

  id : number;
  name:string;
  status:number;

}
