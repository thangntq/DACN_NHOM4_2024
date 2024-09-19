export class customerrequest{
  id: number;
  email :string;
  password: string;
  fullname: string;
  birthDate: Date;
  phone: string;
  role: number;
  photo: string;
  status: number;
}

export class customerresponse{
  id: number;
  email :string;
  password: string;
  fullname: string;
  birthDate: Date;
  adress: string;
  phone: string;
  role: number;
  photo: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
}

