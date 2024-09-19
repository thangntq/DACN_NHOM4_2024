import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

const state = {
  checkoutItems: JSON.parse(localStorage["checkoutItems"] || "[]"),
};

@Injectable({
  providedIn: "root",
})
export class OrderService {
  constructor(private router: Router, private http: HttpClient) {}

  // Get Checkout Items
  public get checkoutItems(): Observable<any> {
    const itemsStream = new Observable((observer) => {
      observer.next(state.checkoutItems);
      observer.complete();
    });
    return <Observable<any>>itemsStream;
  }

  // Create order
  public createOrder(product: any, details: any, orderId: any, amount: any) {
    var item = {
      shippingDetails: details,
      product: product,
      orderId: orderId,
      totalAmount: amount,
    };
    state.checkoutItems = item;
    localStorage.setItem("checkoutItems", JSON.stringify(item));
    localStorage.removeItem("cartItems");
    this.router.navigate(["/shop/checkout/success", orderId]);
  }

  getAddress(): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/client/api/adress/getAllAdressClient`
    );
  }
  addAddress(request: any): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/client/api/adress/add`,
      request
    );
  }
  removeAddress(id: any): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/client/api/adress/delete?id=` + id
    );
  }
  getOrder(): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/client/api/order/findByIdCustomer`
    );
  }
  getOrderByBillCode(billCode: any): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/client/api/order/findByCode?code=` + billCode
    );
  }
  getReturnByBillCode(returnCode: any): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/client/api/return/` + returnCode
    );
  }
  findByCodeInSucces(billCode: any): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/client/api/order/findByCodeInSucces?code=` +
        billCode
    );
  }
  checkout(request: any): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/client/api/order/create`,
      request
    );
  }

  createCheckout(request: any): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/client/api/checkOut/create`,
      request
    );
  }
  getCheckout(request: any): Observable<any> {
    const randomParam = new Date().getTime();
    const url = `${environment.apiUrl}/client/api/checkOut/get?code=${request}&random=${randomParam}`;
    return this.http.get<any>(url);
  }
  getAllVoucher(request: any): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/client/api/voucher/getAllVoucher`,
      request
    );
  }
  getLeadTime(toDistrict: number, toWard: number): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/ghn/api/get-leadtime?toDistrict=` +
        toDistrict +
        `&toWard=` +
        toWard
    );
  }

  checkCart(request: any): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/client/api/checkOut/checkCart`,
      request
    );
  }

  rating(request: any): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/api/Rating/create`,
      request
    );
  }
  getRatingByProduct(id: any): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/api/Rating/findByIdProduct?id=` + id
    );
  }
  uploadImage(imageFiles: File): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuthSendingFile();
    const formData = new FormData();
    formData.append("files", imageFiles);
    return this.http.post<any>(
      `${environment.apiUrl}/api/upload-firebase`,
      formData,
      {
        headers: headers,
      }
    );
  }

  addReturn(request): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/client/api/return/add`,
      request
    );
  }
  addReturnDetail(request): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/client/api/return/addDetail`,
      request
    );
  }

  sendCodeDK(email: string): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/api/customer/send-code-dki?email=` + email,
      email
    );
  }
  confirmCodeDK(code: string): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/api/customer/confirm-code-dki?code=` + code,
      code
    );
  }
  register(request: any): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/api/customer/register`,
      request
    );
  }
  sendCode(email: string): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/api/customer/send-code?email=` + email,
      email
    );
  }
  confirmCode(code: string): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/api/customer/confirm-code?code=` + code,
      code
    );
  }
  forgotPass(request: any): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/api/customer/forgot-password`,
      request
    );
  }
}
