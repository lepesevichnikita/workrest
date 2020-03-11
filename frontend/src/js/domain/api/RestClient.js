import { Header } from "/frontend/src/js/domain/constant/index.js";
import backend from "/frontend/src/js/domain/config/backend.js";

export class RestClient {
  constructor() {
    this._superagent = superagent;
  }

  static getActionUrl(action) {
    return `${this.getHostUrl()}/${action}`;
  }

  static getHostUrl() {
    return backend.host.port ? `${backend.host.url}:${backend.host.port}` : backend.host.url;
  }

  post(action) {
    const request = this._superagent.post(RestClient.getActionUrl(action));
    return this._wrapRequest(request);
  }

  delete(action) {
    const request = this._superagent.delete(RestClient.getActionUrl(action));
    return this._wrapRequest(request);
  }

  update(action) {
    const request = this._superagent.delete(RestClient.getActionUrl(action));
    return this._wrapRequest(request);
  }

  put(action) {
    const request = this._superagent.put(RestClient.getActionUrl(action));
    return this._wrapRequest(request);
  }

  get(action) {
    const request = this._superagent.get(RestClient.getActionUrl(action));
    return this._wrapRequest(request);
  }

  _wrapRequest(request) {
    request.secured = this.secured.bind(request);
    return request;
  }

  secured(tokenValue) {
    return this.set(Header.AUTHORIZATION, tokenValue);
  }
}

export default RestClient;
