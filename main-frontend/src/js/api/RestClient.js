export class RestClient {
  constructor() {
    this._superagent = superagent;
  }

  static getActionUrl(action) {
    return `${RestClient.URL}/${action}`;
  }

  post(action) {
    const request = this._superagent.post(RestClient.getActionUrl(action));
    return this._wrapRequest(request);
  }

  delete(action) {
    const request = this._superagent.delete(RestClient.getActionUrl(action));
    return this._wrapRequest(request);
  }

  _wrapRequest(request) {
    request.secured = this.secured.bind(request);
    return request;
  }

  secured(tokenValue) {
    return this.set(RestClient.AUTHORIZATION, tokenValue);
  }
}

RestClient.AUTHORIZATION = 'Authorization';
RestClient.HOST_PORT = 9091;
RestClient.HOST_URL = 'http://localhost';
RestClient.URL = RestClient.HOST_PORT
    ? `${RestClient.HOST_URL}:${RestClient.HOST_PORT}`
    : RestClient.HOST_URL;

export default RestClient;
