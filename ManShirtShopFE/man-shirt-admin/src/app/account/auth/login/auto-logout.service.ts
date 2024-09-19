import { Cookie } from "ng2-cookies";
import { Injectable } from "@angular/core";
import { AuthenticationService } from "src/app/core/services/auth.service";

// const store = require('store');

const MINUTES_UNITL_AUTO_LOGOUT = 5; // in mins
const CHECK_INTERVAL = 1000; // in ms
const STORE_KEY = "lastAction";

@Injectable()
export class AutoLogoutService {
  auth: AuthenticationService;

  get lastAction() {
    return Number(localStorage.getItem(STORE_KEY));
  }

  set lastAction(value) {
    localStorage.setItem(STORE_KEY, value.toString());
  }

  constructor(authService: AuthenticationService) {
    this.auth = authService;
    // this.check();
    // this.initListener();
    // this.initInterval();
  }

  initListener() {
    document.body.addEventListener("click", () => this.reset());
  }

  reset() {
    this.lastAction = Date.now();
  }

  initInterval() {
    // setInterval(() => {
    //   if (Cookie.get('username') !== null && Cookie.get('username') !== '') {
    //     this.check();
    //   }
    // }, CHECK_INTERVAL);
  }

  // check() {
  //   const now = Date.now();
  //   const timeleft = this.lastAction + MINUTES_UNITL_AUTO_LOGOUT * 60 * 1000;
  //   const diff = timeleft - now;
  //   // console.log(diff);
  //   const isTimeout = diff < 0;

  //   if (isTimeout) {
  //     this.auth.logOut();
  //   }
  // }
}
