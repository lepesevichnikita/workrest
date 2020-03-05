import { endpoint } from "../config";
import { RestClient } from "./RestClient.js";

export class UserService {
  constructor(props) {
    this._restClient = new RestClient();
    this._authorizationService = props.authorizationService;
  }

  getCurrentUser() {
    return new Promise((resolve, reject) => {
      this._restClient
          .get(endpoint.users.root)
          .secured(this._authorizationService.getToken().token)
          .then(resolve)
          .catch(reject);
    });
  }
}

export default UserService;
