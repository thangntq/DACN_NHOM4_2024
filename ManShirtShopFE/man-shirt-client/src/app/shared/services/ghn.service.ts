import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { color } from "../classes/product";
import { map, startWith } from "rxjs/operators";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class GhnService {
  private provinces: any;
  private districts: any;
  private wards: any;
  constructor(private http: HttpClient) {}

  private get lstprovinces(): Observable<any> {
    this.provinces = this.http.get<any>(
      `${environment.apiUrl}/ghn/api/get-province`
    );
    return this.provinces;
  }
  private lstdistricts(provinceId: number): Observable<any> {
    this.districts = this.http.get<any>(
      `${environment.apiUrl}/ghn/api/get-district?provinceId=` + provinceId
    );
    return this.districts;
  }
  private lstwards(districtId: number): Observable<any> {
    this.wards = this.http.get<any>(
      `${environment.apiUrl}/ghn/api/get-ward?wardId=` + districtId
    );
    return this.wards;
  }

  public get getProvinces(): Observable<any> {
    return this.lstprovinces;
  }
  public getDistricts(provinceId: number): Observable<any> {
    return this.lstdistricts(provinceId);
  }
  public getWards(districtId: number): Observable<any> {
    return this.lstwards(districtId);
  }

  getServiceFee(toDistrict: number, toWard: number): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/ghn/api/get-service-fee?toDistrict=` +
        toDistrict +
        `&toWard=` +
        toWard
    );
  }
}
