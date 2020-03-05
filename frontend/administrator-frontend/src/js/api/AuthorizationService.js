import { ContentType } from "../constant";
import { Action } from "../constant/Action.js";
import { Subscribable } from "../model";
import { RestClient } from "./RestClient.js";

export class AuthorizationService extends Subscribable {
  constructor() {
    super();
    this._restClient = new RestClient();
  }

  hasToken() {
    return localStorage.getItem(AuthorizationService.TOKEN) !== null;
  }

  verifyToken() {
    return new Promise((resolve, reject) => {
      this._restClient
          .post("token/verify")
          .accept(ContentType.APPLICATION_JSON)
          .set("Content-Type", ContentType.APPLICATION_JSON)
          .send(this.getToken())
          .then(response => {
            this.notifyAllSubscribers(Action.TOKEN_CORRECT);
            resolve(response);
          })
          .catch(error => {
            this.notifyAllSubscribers(Action.TOKEN_INCORRECT);
            reject(error);
          });
    });
  }

  getToken() {
    return JSON.parse(localStorage.getItem(AuthorizationService.TOKEN));
  }

  signIn(loginInfo) {
    return new Promise((resolve, reject) => {
      this._restClient
          .post("token")
          .accept(ContentType.APPLICATION_JSON)
          .set("Content-Type", ContentType.APPLICATION_JSON)
          .send(loginInfo)
          .then(response => {
            localStorage.setItem(AuthorizationService.TOKEN, response.text);
            this.notifyAllSubscribers(Action.SIGNED_IN, response.text);
            resolve(response);
          })
          .catch(reject);
    });
  }

  signOut() {
    this._restClient
        .delete("token")
        .accept(ContentType.APPLICATION_JSON)
        .set("Content-Type", ContentType.APPLICATION_JSON)
        .send(this.getToken())
        .then()
        .finally(() => {
          localStorage.removeItem(AuthorizationService.TOKEN);
          this.notifyAllSubscribers(Action.LOGGED_OUT, {});
        });
  }

  signUp(loginInfo) {
    return new Promise((resolve, reject) => {
      this._restClient
          .post("AdministratorService")
          .accept(ContentType.APPLICATION_JSON)
          .set("Content-Type", ContentType.APPLICATION_JSON)
          .send(loginInfo)
          .then(response => {
            resolve(response);
            this.notifyAllSubscribers(Action.SIGNED_UP);
          })
          .catch(reject);
    });
  }
}

AuthorizationService.TOKEN = "token";

export default AuthorizationService;
