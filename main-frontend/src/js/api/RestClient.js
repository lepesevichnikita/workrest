export class RestClient {
  constructor() {
    this._superagent = superagent;
  }

  post(action) {
    return this._superagent.post(this.getActionUrl(action)).
                accept('application/json').
                set('Content-Type', 'application/json');
  }

  delete(action) {
    return this._superagent.delete(this.getActionUrl(action)).
                accept('application/json').
                set('Content-Type', 'application/json');
  }

  getActionUrl(action) {
    return `${RestClient.URL}/${action}`;
  }
}

RestClient.HOST_PORT = 9091;
RestClient.HOST_URL = 'http://localhost';
RestClient.URL = RestClient.HOST_PORT
    ? `${RestClient.HOST_URL}:${RestClient.HOST_PORT}`
    : RestClient.HOST_URL;

export default RestClient;
