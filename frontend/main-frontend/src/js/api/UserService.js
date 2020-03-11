import { RestClient } from "/frontend/src/js/domain/api/index.js";
import { endpoint } from "/frontend/src/js/domain/config/index.js";

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

  updateEmployerProfile(employerProfile) {
    return new Promise((resolve, reject) => {
      this._restClient
          .put(endpoint.users.employer)
          .secured(this._authorizationService.getToken().token)
          .send(employerProfile)
          .then(resolve)
          .catch(reject);
    });
  }

  updateFreelancerProfile(freelancerProfile) {
    return new Promise((resolve, reject) => {
      this._restClient
          .put(endpoint.users.freelancer)
          .secured(this._authorizationService.getToken().token)
          .send(freelancerProfile)
          .then(resolve)
          .catch(reject);
    });
  }
}

export default UserService;
