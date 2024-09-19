import { HttpParams } from '@angular/common/http';
import { isNullOrUndefined } from 'is-what';

export class ParamUtil {

  public static toRequestParams<T>(obj: T) {
    const params: RequestParam[] = [];
    if (!isNullOrUndefined(obj)){
      Object.keys(obj).forEach(k => {
        if (!isNullOrUndefined(obj[k])) {
          params.push(new RequestParam(k, obj[k]));
        }
      });
    }
    return params;
  }

  public static toRequestParamsByValueAndKey<T>(values: [], key: string) {
    const params: RequestParam[] = [];
    values.forEach(val => {
      params.push(new RequestParam(key, val));
    });
    return params;
  }
}
export class RequestParam {
  name: string;
  value: string;

  constructor(name: string, value: string) {
    this.name = name;
    this.value = value;
  }

 }
