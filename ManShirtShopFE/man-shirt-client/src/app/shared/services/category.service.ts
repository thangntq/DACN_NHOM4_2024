import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { category } from "../classes/product";
import { map, startWith } from "rxjs/operators";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class CategoryService {
  public Categorys;
  constructor(private http: HttpClient) {}

  private get categorys(): Observable<category[]> {
    return this.http.get<category[]>(
      `${environment.apiUrl}/client/api/properties/findAllOriginalCategory`
    );
  }

  // Get Products
  public get getCategorys(): Observable<category[]> {
    return this.categorys;
  }
}
