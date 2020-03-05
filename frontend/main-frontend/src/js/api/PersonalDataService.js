import { ContentType } from "../constant";
import RestClient from "./RestClient.js";

export class PersonalDataService {
  constructor(props) {
    this._authorizationService = props.authorizationService;
    this._restClient = new RestClient();
  }

  updatePersonalData(personalData) {
    return new Promise((resolve, reject) => {
      this._restClient
          .put("personal_data")
          .secured(this._authorizationService.getToken().token)
          .accept(ContentType.APPLICATION_JSON)
          .set("Content-Type", ContentType.APPLICATION_JSON)
          .send(personalData)
          .then(resolve)
          .catch(reject);
    });
  }
}

export default PersonalDataService;
