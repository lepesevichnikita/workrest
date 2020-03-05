import { AuthorizationService } from "./AuthorizationService.js";
import { RestClient } from "./RestClient.js";

export class PersonalDataService {
  constructor() {
    this._authorizationService = new AuthorizationService();
    this._restClient = new RestClient();
  }

  getPersonalData() {
    return new Promise((resolve, reject) => {
    });
  }
}

export default PersonalDataService;
