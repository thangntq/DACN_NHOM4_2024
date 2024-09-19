import { categoryori } from "./categoryorigin.models";

export class categoriresponse{
  id: number;
  name: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
  categoryId : categoryori;
}

export class categorirequest{
  id: number;
  name: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
  categoryId : number;
}

