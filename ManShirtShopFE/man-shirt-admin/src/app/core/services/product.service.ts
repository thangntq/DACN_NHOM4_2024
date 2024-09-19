import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
import {
  FilterData,
  ProductRequest,
  ProductRespone,
} from "../models/product.model";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import {
  ProductDetail,
  ProductDetailRespone,
} from "../models/productDetail.model";
import { HeadersUtil } from "../util/headers.util";
@Injectable({
  providedIn: "root",
})
export class ProductService {
  productRequest: ProductRequest;
  constructor(private http: HttpClient) {}

  createProduct(productRequest: ProductRequest): Observable<ProductRespone> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<ProductRespone>(
      `${environment.apiUrl}/product/savePDI`,
      productRequest,
      {
        headers: headers,
      }
    );
  }
  createProductDetail(
    productDetail: ProductDetail[]
  ): Observable<ProductRespone> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<ProductRespone>(
      `${environment.apiUrl}/productDetail/createAll`,
      productDetail,
      {
        headers: headers,
      }
    );
  }
  updateProductDetail(
    productDetail: ProductDetail
  ): Observable<ProductRespone> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<ProductRespone>(
      `${environment.apiUrl}/productDetail/update`,
      productDetail,
      {
        headers: headers,
      }
    );
  }
  updateProduct(productRequest: ProductRequest): Observable<ProductRespone> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<ProductRespone>(
      `${environment.apiUrl}/product/update`,
      productRequest,
      {
        headers: headers,
      }
    );
  }
  getProduct(): Observable<ProductRespone[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<ProductRespone[]>(
      `${environment.apiUrl}/product/getAll`,
      {
        headers: headers,
      }
    );
  }
  getProductFilter(filterData: FilterData): Observable<ProductRespone[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<ProductRespone[]>(
      `${environment.apiUrl}/product/getAllByFilter`,
      filterData,
      {
        headers: headers,
      }
    );
  }
  deleteProduct(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/product/delete?id=${id}`, {
      headers: headers,
    });
  }
  updateStatusHoatDong(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get(
      `${environment.apiUrl}/product/updateStatusHoatDong?id=${id}`,
      {
        headers: headers,
      }
    );
  }
  updateStatusXoaVinhVien(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get(
      `${environment.apiUrl}/product/updateStatusXoaVinhVien?id=${id}`,
      {
        headers: headers,
      }
    );
  }
  deleteProductDetail(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(
      `${environment.apiUrl}/productDetail/delete?id=${id}`,
      {
        headers: headers,
      }
    );
  }
  updateDetailStatusHoatDong(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(
      `${environment.apiUrl}/productDetail/updateStatusHoatDong?id=${id}`,
      {
        headers: headers,
      }
    );
  }
  updateDetailStatusXoaMem(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(
      `${environment.apiUrl}/productDetail/updateStatusXoaMem?id=${id}`,
      {
        headers: headers,
      }
    );
  }
  findByBarCode(barCode: string): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<any>(
      `${environment.apiUrl}/productDetail/findByBarcode?barcode=` + barCode,
      {
        headers: headers,
      }
    );
  }

  getExportProduct(status: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<any>(
      `${environment.apiUrl}/product/export/` + status,
      {
        headers: headers,
        responseType: "blob" as "json",
      }
    );
  }
  getExportTemplateProduct(): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<any>(
      `${environment.apiUrl}/product/export-template-generateExcel/`,
      {
        headers: headers,
        responseType: "blob" as "json",
      }
    );
  }
  fileExcle(file: File): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuthSendingFile();
    const formData = new FormData();
    formData.append("file", file);
    return this.http.post<any>(
      `${environment.apiUrl}/product/import/fileExcle`,
      formData,
      {
        headers: headers,
      }
    );
  }
}
