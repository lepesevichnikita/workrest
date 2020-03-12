import { AuthorizationService, RestClient } from "/frontend/src/js/domain/api/index.js";
import { endpoint } from "/frontend/src/js/domain/config/index.js";

export class AdministratorService {
  constructor(props) {
    this._locator = props.locator;
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

  get _restClient() {
    return this._locator.getServiceByClass(RestClient);
  }

  get _authorizationService() {
    return this._locator.getServiceByClass(AuthorizationService);
  }
}

export default AdministratorService;
