import { Cookie } from "ng2-cookies";
import { HttpHeaders } from "@angular/common/http";
import { AuthConstant } from "../constant/auth.constant";
import { isNullOrUndefined } from "util";

export class HeadersUtil {
  constructor() {}
  public static getHeaders(): HttpHeaders {
    return new HttpHeaders({
      "Content-Type": "application/json",
    });
  }

  public static getHeadersRedmineApi(redmineApiKey): HttpHeaders {
    return new HttpHeaders({
      // 'Content-Type': 'application/json','Access-Control-Allow-Origin': '*'
      "X-Redmine-API-Key": redmineApiKey,
    });
  }

  public static getHeadersAuthOnly(): HttpHeaders {
    const token =
      AuthConstant.TOKEN_TYPE_KEY + Cookie.get(AuthConstant.ACCESS_TOKEN_KEY);
    if (isNullOrUndefined(token)) {
      return new HttpHeaders();
    }
    return new HttpHeaders({
      Authorization: token,
    });
  }

  public static getHeadersUploadOnly(): HttpHeaders {
    const token =
      AuthConstant.TOKEN_TYPE_KEY + Cookie.get(AuthConstant.ACCESS_TOKEN_KEY);
    if (isNullOrUndefined(token)) {
      return new HttpHeaders();
    }
    return new HttpHeaders({
      Authorization: token,
      "Upload-check": "1",
    });
  }

  public static getHeadersAuth(): HttpHeaders {
    const token =
      AuthConstant.TOKEN_TYPE_KEY + Cookie.get(AuthConstant.ACCESS_TOKEN_KEY);
    if (isNullOrUndefined(token)) {
      return HeadersUtil.getHeaders();
    }

    return new HttpHeaders({
      "Content-Type": "application/json",
      Authorization: token,
    });
  }

  public static getHeadersAuthFile(): HttpHeaders {
    const token =
      AuthConstant.TOKEN_TYPE_KEY + Cookie.get(AuthConstant.ACCESS_TOKEN_KEY);
    if (isNullOrUndefined(token)) {
      return HeadersUtil.getHeaders();
    }

    return new HttpHeaders({
      "Content-Type": "application/pdf",
      Authorization: token,
    });
  }

  public static getHeadersAuthWithUsername(): HttpHeaders {
    const token =
      AuthConstant.TOKEN_TYPE_KEY + Cookie.get(AuthConstant.ACCESS_TOKEN_KEY);
    if (isNullOrUndefined(token)) {
      return HeadersUtil.getHeaders();
    }
    return new HttpHeaders({
      "Content-Type": "application/json",
      Authorization: token,
      "X-UNAME": Cookie.get("username"),
    });
  }

  public static getHeadersAuthSendingFile(): HttpHeaders {
    const token =
      AuthConstant.TOKEN_TYPE_KEY + Cookie.get(AuthConstant.ACCESS_TOKEN_KEY);
    if (isNullOrUndefined(token)) {
      return HeadersUtil.getHeaders();
    }
    return new HttpHeaders({
      // 'Content-Type': 'multipart/form-data',
      Authorization: token,
    });
  }

  public static getHeadersRedmine(redmineApiKey: any): HttpHeaders {
    if (isNullOrUndefined(redmineApiKey)) {
      return HeadersUtil.getHeaders();
    }
    return new HttpHeaders({
      "X-Redmine-API-Key": redmineApiKey,
    });
  }

  public static getHeadersAuthAttachment(): HttpHeaders {
    const token =
      AuthConstant.TOKEN_TYPE_KEY + Cookie.get(AuthConstant.ACCESS_TOKEN_KEY);
    if (isNullOrUndefined(token)) {
      return HeadersUtil.getHeaders();
    }

    return new HttpHeaders({
      "Access-Control-Allow-Origin": "*",
      responseType: "blob" as "json",
      Authorization: token,
    });
  }
}
