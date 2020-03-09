import { PersonalDataService } from "../api";
import { redirectToPage } from "../main.js";
import { Page } from "./Page.js";

export class PersonalData extends Page {
  constructor(props) {
    super(props);
    this._authorizationService = props.authorizationService;
    this._personalDataService = new PersonalDataService(props);
    this.addListener(".button[data-action=approve]", ["click", this._onApproveClick.bind(this), false])
        .addListener(".button[data-action=reject]", ["click", this._onRejectClick.bind(this), false])
        .addListener(".button[data-action=show]", ["click", this._onShowClick.bind(this), false]);
  }

  process() {
    this.showDimmer();
    this._authorizationService.checkIsAuthorized()
        .then(() => this._loadData())
        .catch(() => redirectToPage("login"))
        .finally(() => this.hideDimmer());
  }

  _onApproveClick(event) {
    event.preventDefault();
    this.showDimmer();
    const personalDataId = event.currentTarget.getAttribute("data-id");
    this._personalDataService.approvePersonalData(personalDataId)
        .then(() => this._loadData())
        .finally(() => {
          const modalWindow = $(`#personalData${personalDataId}`);
          modalWindow.modal("hide");
        });
  }

  _onRejectClick(event) {
    event.preventDefault();
    this.showDimmer();
    const personalDataId = event.currentTarget.getAttribute("data-id");
    this._personalDataService.rejectPersonalData(personalDataId)
        .then(() => this._loadData())
        .finally(() => {
          const modalWindow = $(`#personalData${personalDataId}`);
          modalWindow.modal("hide");
        });
  }

  _onShowClick(event) {
    event.preventDefault();
    const id = event.currentTarget.getAttribute("data-id");
    const modalWindow = $(`#personalData${id}`);
    modalWindow.modal({detachable: false})
               .modal("show");
  }

  _loadData() {
    this._personalDataService.getPersonalData()
        .then(response => this.replacePage("personal_data", {
          fileUrl: this._personalDataService,
          personalData: response.body
        })
                              .finally(() => super.process()));
  }
}
