import {Serializable} from "./Serializable.js";

export class LoginInfo extends Serializable {
  constructor(login, password) {
    super();
    this._login = login;
    this._password = password;
  }

  get login() {
    return this._login;
  }

  set login(value) {
    this._login = value;
  }

  get password() {
    return this._password;
  }

  set password(value) {
    this._password = value;
  }
}

export default LoginInfo;