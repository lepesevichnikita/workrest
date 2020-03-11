import { RestClient } from "/frontend/src/js/domain/api/RestClient.js";
import { endpoint } from "/frontend/src/js/domain/config/index.js";
import { Action, ContentType, Header } from "/frontend/src/js/domain/constant/index.js";
import { Subscribable } from "/frontend/src/js/domain/model/Subscribable.js";

export class AuthorizationService extends Subscribable {
  constructor() {
    super();
    this._restClient = new RestClient();
  }

  hasToken() {
    return localStorage.getItem(AuthorizationService.TOKEN) !== null;
  }

  checkIsAuthorized() {
    return new Promise((resolve, reject) => {
      if (this.hasToken()) {
        this.verifyToken()
            .then(resolve)
            .catch(reject);
      } else {
        reject();
      }
    });
  };

  verifyToken() {
    return new Promise((resolve, reject) => {
      this._restClient
          .post(endpoint.token.verify)
          .secured(this.getToken().token)
          .accept(ContentType.APPLICATION_JSON)
          .set(Header.CONTENT_TYPE, ContentType.APPLICATION_JSON)
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
          .post(endpoint.token.root)
          .accept(ContentType.APPLICATION_JSON)
          .set(Header.CONTENT_TYPE, ContentType.APPLICATION_JSON)
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
        .delete(endpoint.token.root)
        .secured(this.getToken().token)
        .accept(ContentType.APPLICATION_JSON)
        .set(Header.CONTENT_TYPE, ContentType.APPLICATION_JSON)
        .then()
        .finally(() => {
          localStorage.removeItem(AuthorizationService.TOKEN);
          this.notifyAllSubscribers(Action.LOGGED_OUT, {});
        });
  }

  signUp(loginInfo) {
    return new Promise((resolve, reject) => {
      this._restClient
          .post(endpoint.users.root)
          .accept(ContentType.APPLICATION_JSON)
          .set(Header.CONTENT_TYPE, ContentType.APPLICATION_JSON)
          .send(loginInfo)
          .then(response => {
            resolve(response);
            this.notifyAllSubscribers(Action.SIGNED_UP);
          })
          .catch(reject);
    });
  }
}

AuthorizationService.TOKEN = "admin_token";

export default AuthorizationService;
