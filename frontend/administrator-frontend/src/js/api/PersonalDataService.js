import { AuthorizationService, RestClient } from "/frontend/src/js/domain/api/index.js";
import { endpoint } from "/frontend/src/js/domain/config/index.js";

export class PersonalDataService {
  constructor(props) {
    this._locator = props.locator;
  }

  getFileActionUrl() {
    return RestClient.getActionUrl(endpoint.file.root);
  }

  getPersonalData() {
    return new Promise((resolve, reject) => this._restClient.get(endpoint.personal_data.all)
                                                .secured(this._authorizationService.getToken().token)
                                                .then(resolve)
                                                .catch(reject));
  }

  approvePersonalData(personalDataId) {
    return new Promise((resolve, reject) => this._restClient.post(endpoint.personal_data.approve_by_id(personalDataId))
                                                .secured(this._authorizationService.getToken().token)
                                                .then(resolve)
                                                .catch(reject));
  }

  rejectPersonalData(personalDataId) {
    return new Promise((resolve, reject) => this._restClient.post(endpoint.personal_data.reject_by_id(personalDataId))
                                                .secured(this._authorizationService.getToken().token)
                                                .then(resolve)
                                                .catch(reject));
  }

  get _authorizationService() {
    return this._locator.getServiceByClass(AuthorizationService);
  }

  get _restClient() {
    return this._locator.getServiceByClass(RestClient);
  }
}

export default PersonalDataService;
