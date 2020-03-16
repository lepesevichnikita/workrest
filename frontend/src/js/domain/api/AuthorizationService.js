import { endpoint } from "/frontend/src/js/domain/config/index.js";
import { Action, ContentType, Header } from "/frontend/src/js/domain/constant/index.js";
import { Subscribable } from "/frontend/src/js/domain/model/index.js";
import { RestClient } from "./RestClient.js";

export class AuthorizationService extends Subscribable {
  constructor(props) {
    super();
    this._locator = props.locator;
  }

  hasToken() {
    return localStorage.getItem(AuthorizationService.TOKEN) !== null;
  }

  get _restClient() {
    return this._locator.getServiceByClass(RestClient);
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

  async checkIsAuthorized() {
    return this.hasToken() && await this.verifyToken();
  }

  async verifyToken() {
    let result = false;
    const response = await this._restClient
                               .post(endpoint.token.verify)
                               .secured(this.getToken().token)
                               .accept(ContentType.APPLICATION_JSON)
                               .set(Header.CONTENT_TYPE, ContentType.APPLICATION_JSON);
    if (response.status >= 200 && response.status < 300) {
      result = true;
      this.notifyAllSubscribers(Action.TOKEN_CORRECT);
    } else {
      this.notifyAllSubscribers(Action.TOKEN_INCORRECT);
    }
    return result;
  }
}

AuthorizationService.TOKEN = "token";

export default AuthorizationService;
