import {RestClient} from './RestClient.js';

export class AuthorizationService {
  constructor() {
    this._restClient = new RestClient();
  }

  hasToken() {
    return localStorage.getItem(AuthorizationService.TOKEN) !== null;
  }

  verifyToken() {
    return this._restClient.post('token/verify').send(this.getToken());
  }

  getToken() {
    return localStorage.getItem(AuthorizationService.TOKEN);
  }

  signIn(loginInfo, successCallback, finallyCallback) {
    this._restClient.post('token').send(loginInfo).then(response => {
      localStorage.setItem(AuthorizationService.TOKEN, response.text);
      successCallback(response);
    });
  }

  signOut() {
    this._restClient.delete('token').
         send(this.getToken()).
         then().
         finally(() => localStorage.removeItem(AuthorizationService.TOKEN));
  }

  async signUp(loginInfo) {
    return this._restClient.post('users').send(loginInfo);
  }
}

AuthorizationService.TOKEN = "token";

export default AuthorizationService;
