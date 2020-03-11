import { RestClient } from "/frontend/src/js/domain/api/index.js";
import { endpoint } from "/frontend/src/js/domain/config/index.js";

export class FreelancerService {
  constructor(props) {
    this._authorizationService = props.authorizationService;
    this._restClient = new RestClient();
  }

  getFreelancers() {
    return new Promise((resolve, reject) => {
      this._restClient.get(endpoint.freelancers.all)
          .secured(this._authorizationService.getToken().token)
          .then(resolve)
          .catch(reject);
    });
  }
}

export default FreelancerService;
