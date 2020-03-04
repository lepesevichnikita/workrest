import {Serializable} from "./Serializable.js";

export class Token extends Serializable {
  constructor(token) {
    super();
    this._token = token;
  }

  get token() {
    return this._token;
  }

  set token(value) {
    this._token = value;
  }
}

export default Token;
