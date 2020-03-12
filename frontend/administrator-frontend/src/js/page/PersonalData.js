import { PersonalDataService } from "/frontend/administrator-frontend/src/js/api/index.js";
import { AuthorizationService } from "/frontend/src/js/domain/api/index.js";
import { Page } from "/frontend/src/js/domain/component/index.js";

export class PersonalData extends Page {
  constructor(props) {
    super(props);
    this.addListener(".button[data-action=approve]", ["click", this._onApproveClick.bind(this), false])
        .addListener(".button[data-action=reject]", ["click", this._onRejectClick.bind(this), false])
        .addListener(".button[data-action=show]", ["click", this._onShowClick.bind(this), false]);
  }

  get _authorizationService() {
    return this.locator.getServiceByClass(AuthorizationService);
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

  get _personalDataService() {
    return this.locator.getServiceByClass(PersonalDataService);
  }

  process() {
    this.showDimmer();
    this._authorizationService.checkIsAuthorized()
        .then(() => this._loadData())
        .catch(() => this.redirectToPage("login"))
        .finally(() => this.hideDimmer());
  }
}
