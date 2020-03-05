import { endpoint } from "../config";
import { RestClient } from "./RestClient.js";

export class AdministratorService {
  constructor(props) {
    this._restClient = new RestClient();
    this._authorizationService = props.authorizationService;
  }

  getAdministrators() {
    return new Promise((resolve, reject) => {
      this._restClient
          .get(endpoint.administrators.root)
          .secured(this._authorizationService.getToken().token)
          .then(resolve)
          .catch(reject);
    });
  }

  createAdministrator(loginInfo) {
    return new Promise((resolve, reject) => {
      this._restClient.post(endpoint.administrators.root)
          .secured(this._authorizationService.getToken().token)
          .send(loginInfo)
          .then(resolve)
          .catch(reject);
    });
  }

  deleteAdministrator(administratorId) {
    return new Promise((resolve, reject) => {
      this._restClient.delete(endpoint.administrators.by_id(administratorId))
          .secured(this._authorizationService.getToken().token)
          .then(resolve)
          .catch(reject);
    });
  }
}

export default AdministratorService;
