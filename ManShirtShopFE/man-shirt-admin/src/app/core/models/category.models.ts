export class categoryResponse {
  id: number;
  name: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
  categoryId: number;
  lstCategory: categoryResponse[];
}
export class categoryRequest {
  id: number;
  name: string;
  categoryId: number;
}
