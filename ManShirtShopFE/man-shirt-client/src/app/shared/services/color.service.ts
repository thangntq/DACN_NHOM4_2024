import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { color } from "../classes/product";
import { map, startWith } from "rxjs/operators";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class ColorService {
  public Colors;
  constructor(private http: HttpClient) {}

  private get colors(): Observable<color[]> {
    return this.http.get<color[]>(
      `${environment.apiUrl}/client/api/properties/findAllColor`
    );
  }

  // Get Products
  public get getColors(): Observable<color[]> {
    return this.colors;
  }
}
