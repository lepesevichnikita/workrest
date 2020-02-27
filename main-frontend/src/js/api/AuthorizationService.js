import {RestClient} from './RestClient.js';
import Action from './Action.js';

export class AuthorizationService {
  constructor() {
    this._restClient = new RestClient();
    this._subscribers = {};
  }

  subscribe(actionName, callback) {
    const callbacks = this._subscribers[actionName] || [];
    this._subscribers[actionName] = [...callbacks, callback];
    return this;
  }

  notifyAllSubscribers(actionName, data) {
    const callbacks = this._subscribers[actionName] || [];
    callbacks.forEach(callback => callback(data));
  }

  hasToken() {
    return localStorage.getItem(AuthorizationService.TOKEN) !== null;
  }

  verifyToken() {
    return new Promise((resolve, reject) => {
      this._restClient.post('token/verify').
           send(this.getToken()).
           then(response => {
             this.notifyAllSubscribers(Action.TOKEN_CORRECT);
             resolve(response);
           }).
           catch(error => {
             this.notifyAllSubscribers(Action.TOKEN_INCORRECT);
             reject(error);
           });
    });
  }

  getToken() {
    return localStorage.getItem(AuthorizationService.TOKEN);
  }

  signIn(loginInfo) {
    return new Promise((resolve, reject) => {
      this._restClient.post('token').send(loginInfo).then(response => {
        localStorage.setItem(AuthorizationService.TOKEN, response.text);
        this.notifyAllSubscribers(Action.SIGNED_IN, response.text);
        resolve(response);
      }).catch(reject);
    });
  }

  signOut() {
    this._restClient.delete('token').
         send(this.getToken()).
         then().
         finally(() => {
           localStorage.removeItem(AuthorizationService.TOKEN);
           this.notifyAllSubscribers(Action.LOGGED_OUT, {});
         });
  }

  signUp(loginInfo) {
    return new Promise((resolve, reject) => {
      this._restClient.post('users').send(loginInfo).then(response => {
        resolve(response);
        this.notifyAllSubscribers(Action.SIGNED_UP);
      }).catch(reject);
    });
  }
}

AuthorizationService.TOKEN = "token";

export default AuthorizationService;
