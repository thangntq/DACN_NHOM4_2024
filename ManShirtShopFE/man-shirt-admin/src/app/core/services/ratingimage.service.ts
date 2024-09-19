import { Injectable } from "@angular/core";
import { ImageUploadRating, ImageUpload } from "../models/ratingimage.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";
@Injectable({
  providedIn: "root",
})
export class UploadRatingService {
  constructor(private http: HttpClient) {}

  uploadImageRating(imageFiles: File): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
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

  getRatingImage(): Observable<ImageUploadRating[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<ImageUploadRating[]>(
      `${environment.apiUrl}/RatingImage/findAll`,
      {
        headers: headers,
      }
    );
  }

  createRatingImage(
    UploadRating: ImageUploadRating
  ): Observable<ImageUploadRating> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<ImageUploadRating>(
      `${environment.apiUrl}/RatingImage/create`,
      UploadRating,
      {
        headers: headers,
      }
    );
  }

  updateRatingImage(
    UploadRating: ImageUploadRating
  ): Observable<ImageUploadRating> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<ImageUploadRating>(
      `${environment.apiUrl}/RatingImage/update`,
      UploadRating,
      {
        headers: headers,
      }
    );
  }

  deleteRatingImage(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(
      `${environment.apiUrl}/RatingImage/delete?id=${id}`,
      {
        headers: headers,
      }
    );
  }
}
