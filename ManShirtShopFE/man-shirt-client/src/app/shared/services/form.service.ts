import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { form } from "../classes/product";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class FormService {
  public Forms;
  constructor(private http: HttpClient) {}

  private get forms(): Observable<form[]> {
    return this.http.get<form[]>(
      `${environment.apiUrl}/client/api/properties/findAllForm`
    );
  }

  public get getForm(): Observable<form[]> {
    return this.forms;
  }
}
