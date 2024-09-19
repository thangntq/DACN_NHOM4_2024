
export class ApiUrlUtil {

  public static buildQueryString(path: string, params?: RequestParam[]): string {
    let url = '';
    url += path;

    if (params && params.length > 0) {
      for (let i = 0; i < params.length; i++) {
        if (i === 0) {
          url += '?';
        } else {
          url += '&';
        }
        url += params[i].name;
        url += '=';
        url += params[i].value;
      }
    }
    return url;
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
