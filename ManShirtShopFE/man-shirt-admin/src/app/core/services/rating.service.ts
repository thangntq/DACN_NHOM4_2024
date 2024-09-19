import { Injectable } from "@angular/core";
import { rating } from "../models/rating.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class RatingService {
  constructor(private http: HttpClient) {}

  getRating(): Observable<rating[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<rating[]>(`${environment.apiUrl}/Rating/findAll`, {
      headers: headers,
    });
  }

  createRating(rating: rating): Observable<rating> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<rating>(
      `${environment.apiUrl}/Rating/create`,
      rating,
      {
        headers: headers,
      }
    );
  }

  updateStausOff(id: any): Observable<rating> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<rating>(
      `${environment.apiUrl}/Rating/updateStausOff?id=` + id,
      {
        headers: headers,
      }
    );
  }

  updateStausOn(id: any): Observable<rating> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<rating>(
      `${environment.apiUrl}/Rating/updateStausOne?id=` + id,
      {
        headers: headers,
      }
    );
  }
}
