import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GhnService {

  constructor(private http: HttpClient) {}

  getProvince(): Observable<any> {
    return this.http.get<any>(`http://103.162.20.122:8080/ghn/api/get-province`);

  }
  getDistrict(provinceId: number): Observable<any> {
    return this.http.get<any>(`http://103.162.20.122:8080/ghn/api/get-district?provinceId=` + provinceId);
  }
  getWard(districtId: number): Observable<any> {
    return this.http.get<any>(`http://103.162.20.122:8080/ghn/api/get-ward?wardId=` + districtId);
  }
  getServiceFee(toDistrict : number, toWard : number ) : Observable<any>{
    return this.http.get<any>(`http://103.162.20.122:8080/ghn/api/get-service-fee?toDistrict=` + toDistrict + `&toWard=`+ toWard);
  }
}
