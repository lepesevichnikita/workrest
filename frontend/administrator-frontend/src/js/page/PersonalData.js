import { PersonalDataService } from "../api";
import { redirectToPage } from "../main.js";
import { Page } from "./Page.js";

export class PersonalData extends Page {
  constructor(props) {
    super(props);
    this._authorizationService = props.authorizationService;
    this._personalDataService = new PersonalDataService(props);
  }

  process() {
    this._authorizationService.checkIsAuthorized()
        .then(() => {
          this._loadData();
        })
        .catch(() => redirectToPage("login"))
        .finally(() => this.process());
  }

  _loadData() {
    this._personalDataService.getPersonalData()
        .then(response => this.replacePage("personal_data", response.body));
  }
}
