import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { material } from "../classes/product";

@Injectable({
  providedIn: "root",
})
export class MaterialService {
  public Materials;
  constructor(private http: HttpClient) {}

  private get materials(): Observable<material[]> {
    return this.http.get<material[]>(
      `${environment.apiUrl}/client/api/properties/findAllMateria`
    );
  }

  public get getMaterial(): Observable<material[]> {
    return this.materials;
  }
}
