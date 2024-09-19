import { Injectable } from "@angular/core";
import { contactResponse, contactRequest } from "../models/contact.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class ContactService {
  constructor(private http: HttpClient) {}

  getContact(): Observable<contactResponse[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<contactResponse[]>(
      `${environment.apiUrl}/contact/findAll`,
      {
        headers: headers,
      }
    );
  }

  createContact(contact: contactRequest): Observable<contactResponse> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<contactResponse>(
      `${environment.apiUrl}/contact/create`,
      contact,
      {
        headers: headers,
      }
    );
  }

  updateContact(contact: contactRequest): Observable<contactResponse> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<contactResponse>(
      `${environment.apiUrl}/contact/update`,
      contact,
      {
        headers: headers,
      }
    );
  }
  deleteContact(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/contact/delete?id=${id}`, {
      headers: headers,
    });
  }
}
