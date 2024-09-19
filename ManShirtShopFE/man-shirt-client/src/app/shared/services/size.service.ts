import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { size } from "../classes/product";
import { map, startWith } from "rxjs/operators";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class SizeService {
  public Sizes;
  constructor(private http: HttpClient) {}

  private get sizes(): Observable<size[]> {
    return this.http.get<size[]>(
      `${environment.apiUrl}/client/api/properties/findAllSize`
    );
  }

  // Get Products
  public get getSizes(): Observable<size[]> {
    return this.sizes;
  }
}
