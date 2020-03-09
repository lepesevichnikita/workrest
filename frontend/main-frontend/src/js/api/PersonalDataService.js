import { endpoint } from "../config";
import { ContentType, Header } from "../constant";
import RestClient from "./RestClient.js";

export class PersonalDataService {
  constructor(props) {
    this._authorizationService = props.authorizationService;
    this._restClient = new RestClient();
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
}

export default PersonalDataService;
