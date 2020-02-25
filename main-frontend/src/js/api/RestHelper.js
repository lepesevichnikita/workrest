export class RestHelper {
  constructor() {
  }

  getActionUrl(action) {
    return `${RestHelper.URL}/${action}`;
  }
}

RestHelper.HOST_PORT = 9091;
RestHelper.HOST_URL = "http://localhost";
RestHelper.URL = RestHelper.HOST_PORT ? `${RestHelper.HOST_URL}:${RestHelper.HOST_PORT}` : RestHelper.HOST_URL;

export default RestHelper