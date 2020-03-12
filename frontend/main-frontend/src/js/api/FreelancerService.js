import { AuthorizationService, RestClient } from "/frontend/src/js/domain/api/index.js";
import { endpoint } from "/frontend/src/js/domain/config/index.js";

export class FreelancerService {
  constructor(props) {
    this._locator = props.locator;
  }

  getFreelancers() {
    return new Promise((resolve, reject) => {
      this._restClient.get(endpoint.freelancers.all)
          .secured(this._authorizationService.getToken().token)
          .then(resolve)
          .catch(reject);
    });
  }

  get _authorizationService() {
    return this._locator.getServiceByClass(AuthorizationService);
  }

  get _restClient() {
    return this._locator.getServiceByClass(RestClient);
  }

  get _authorizationService() {
    return this._locator.getServiceByClass(AuthorizationService);
  }

  get _restClient() {
    return this._locator.getServiceByClass(RestClient);
  }
}

export default FreelancerService;
