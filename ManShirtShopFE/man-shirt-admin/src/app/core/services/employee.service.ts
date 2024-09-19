import { Injectable } from "@angular/core";
import { employeeRespone, employeeRequest } from "../models/employee.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class EmployeeService {
  constructor(private http: HttpClient) {}

  getEmployee(): Observable<employeeRespone[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<employeeRespone[]>(
      `${environment.apiUrl}/employee/getAll`,
      {
        headers: headers,
      }
    );
  }

  createEmployee(employee: employeeRequest): Observable<employeeRespone> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<employeeRespone>(
      `${environment.apiUrl}/employee/create`,
      employee,
      {
        headers: headers,
      }
    );
  }

  updateEmployee(employee: employeeRequest): Observable<employeeRespone> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<employeeRespone>(
      `${environment.apiUrl}/employee/update`,
      employee,
      {
        headers: headers,
      }
    );
  }
  deleteEmployee(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/employee/delete?id=${id}`, {
      headers: headers,
    });
  }

  getEmployeeId(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<any>(
      `${environment.apiUrl}/employee/findById?id=` + id,
      {
        headers: headers,
      }
    );
  }
}
