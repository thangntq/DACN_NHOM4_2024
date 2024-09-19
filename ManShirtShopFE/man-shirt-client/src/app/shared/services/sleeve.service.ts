import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { sleeve } from "../classes/product";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class SleeveService {
  public Sleeves;
  constructor(private http: HttpClient) {}

  private get sleeves(): Observable<sleeve[]> {
    return this.http.get<sleeve[]>(
      `${environment.apiUrl}/client/api/properties/findAllSleeve`
    );
  }

  public get getSleeve(): Observable<sleeve[]> {
    return this.sleeves;
  }
}
