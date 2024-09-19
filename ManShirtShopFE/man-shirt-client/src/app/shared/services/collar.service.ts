import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { collar } from "../classes/product";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class CollarService {
  public Collars;
  constructor(private http: HttpClient) {}

  private get collars(): Observable<collar[]> {
    return this.http.get<collar[]>(
      `${environment.apiUrl}/client/api/properties/findAllCollar`
    );
  }

  public get getCollar(): Observable<collar[]> {
    return this.collars;
  }
}
