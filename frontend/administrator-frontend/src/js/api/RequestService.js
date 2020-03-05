import requests from "./requests.js";

export class RequestService {
  getRequests() {
    return new Promise((resolve, reject) => {
      setTimeout(() => resolve({body: requests}), 100);
    });
  }
}

export default RequestService;
