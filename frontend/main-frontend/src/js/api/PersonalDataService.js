import { AuthorizationService, RestClient } from "/frontend/src/js/domain/api/index.js";
import { endpoint } from "/frontend/src/js/domain/config/index.js";
import { ContentType, Header } from "/frontend/src/js/domain/constant/index.js";

export class PersonalDataService {
  constructor(props) {
    this._locator = props.locator;
  }

  getPersonalData() {
    return new Promise((resolve, reject) => {
      this._restClient
          .get(endpoint.personal_data.root)
          .secured(this._authorizationService.getToken().token)
          .accept(ContentType.APPLICATION_JSON)
          .then(resolve)
          .catch(reject);
    });
  }

  updatePersonalData(personalData) {
    return new Promise((resolve, reject) => {
      this._restClient
          .put(endpoint.personal_data.root)
          .secured(this._authorizationService.getToken().token)
          .accept(ContentType.APPLICATION_JSON)
          .set(Header.CONTENT_TYPE, ContentType.APPLICATION_JSON)
          .send(personalData)
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
}

export default PersonalDataService;
