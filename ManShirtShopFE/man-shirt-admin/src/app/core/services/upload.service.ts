import { Injectable } from "@angular/core";
import { ImageUploadDto } from "../models/upload.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";
@Injectable({
  providedIn: "root",
})
export class UploadService {
  constructor(private http: HttpClient) {}

  uploadImageByColor(imageFiles: File[]): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuthSendingFile();
    const formData = new FormData();
    imageFiles.forEach((file) => {
      formData.append("files", file);
    });
    return this.http.post<any>(
      `${environment.apiUrl}/upload-firebase`,
      formData,
      {
        headers: headers,
      }
    );
  }

  uploadImage(imageFiles: File): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuthSendingFile();
    const formData = new FormData();
    formData.append("files", imageFiles);
    return this.http.post<any>(
      `${environment.apiUrl}/upload-firebase`,
      formData,
      {
        headers: headers,
      }
    );
  }
}
