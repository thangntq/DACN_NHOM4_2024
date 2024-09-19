import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { design } from "../classes/product";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class DesignService {
  public Designs;
  constructor(private http: HttpClient) {}

  private get designs(): Observable<design[]> {
    return this.http.get<design[]>(
      `${environment.apiUrl}/client/api/properties/findAllDegin`
    );
  }

  public get getDesign(): Observable<design[]> {
    return this.designs;
  }
}
