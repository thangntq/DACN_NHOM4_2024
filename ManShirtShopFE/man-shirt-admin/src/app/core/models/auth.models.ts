export class User {
  id: number;
  username: string;
  password: string;
  firstName?: string;
  lastName?: string;
  token?: string;
  email: string;
}
export enum Role {
  Admin = "ROLE_Admin",
  Manager = "ROLE_Manager",
}
