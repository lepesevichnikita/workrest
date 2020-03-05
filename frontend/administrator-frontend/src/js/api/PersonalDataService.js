import {AuthorizationService} from "./AuthorizationService.js";
import {RestClient} from "./RestClient.js";
import {endpoint} from "../config";

export class PersonalDataService {
  constructor() {
    this._authorizationService = new AuthorizationService();
    this._restClient = new RestClient();
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
}

export default PersonalDataService;
