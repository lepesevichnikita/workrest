import {RestHelper} from "./RestHelper.js";

export class AuthorizationService {
  constructor() {
    this._superAgent = superagent;
    this._restHelper = new RestHelper();
  }

  hasToken() {
    return sessionStorage.getItem(AuthorizationService.TOKEN) !== null
  }

  getToken() {
    return sessionStorage.getItem(AuthorizationService.TOKEN);
  }

  signIn(loginInfo) {
    this.post('token')
        .send(loginInfo)
        .then(response => sessionStorage.setItem(AuthorizationService.TOKEN, response.text))
  }

  signOut() {
    return this.post('token')
               .send(this.getToken())
               .then()
               .finally(() => sessionStorage.removeItem(AuthorizationService.TOKEN));
  }

  signUp(loginInfo) {
    return this.post('users')
               .send(loginInfo)
               .then(response => {
                 console.dir(response.body);
               })
  }

  post(action) {
    return this._superAgent.post(this._restHelper.getActionUrl(action))
               .set('Content-Type', 'application/json');
  }

  delete(action) {
    return this._superAgent.delete(this._restHelper.getActionUrl(action))
               .set('Content-Type', 'application/json');
  }
}

AuthorizationService.TOKEN = "token";

export default AuthorizationService;
