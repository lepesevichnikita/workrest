import { AuthorizationService } from "/frontend/src/js/domain/api/index.js";
import { Page } from "/frontend/src/js/domain/component/index.js";
import { AdministratorService } from "/frontend/system-administrator-frontend/src/js/api/index.js";

export class Administrators extends Page {
  constructor(props) {
    super(props);
    this._loginInfo = {};
    this.addListener("input[name=login]", ["change", this._onLoginChange.bind(this), false])
        .addListener("input[name=password]", ["change", this._onPasswordChange.bind(this), false])
        .addListener("#administratorForm", ["submit", this._onFormSubmit.bind(this), false])
        .addListener(".ui.card > div.ui.bottom.attached.button", ["click", this._onDeleteClick.bind(this), false]);
  }

  get _administratorService() {
    return this.locator.getServiceByClass(AdministratorService);
  }

  _onLoginChange(event) {
    event.preventDefault();
    this._loginInfo.login = event.target.value;
  }

  _onPasswordChange(event) {
    event.preventDefault();
    this._loginInfo.password = event.target.value;
  }

  get _authorizationService() {
    return this.locator.getServiceByClass(AuthorizationService);
  }

  process() {
    this.showDimmer();
    this._authorizationService.checkIsAuthorized()
        .then((result) => {
          if (result) {
            this._loadData();
          } else {
            this.redirectToPage("login");
          }
        })
        .catch(console.error);
  }

  _onFormSubmit(event) {
    event.preventDefault();
    this.showDimmer();
    this._administratorService.createAdministrator(this._loginInfo)
        .finally(() => this.process());
  }

  _loadData() {
    this._administratorService.getAdministrators()
        .then(response => this.replacePage("administrators", {administrators: response.body})
                              .finally(() => {
                                super.process();
                                this.hideDimmer();
                              }));
  }

  _onDeleteClick(event) {
    event.preventDefault();
    this.showDimmer();
    const administratorId = event.currentTarget.getAttribute("data-id");
    this._administratorService.deleteAdministrator(administratorId)
        .finally(() => this.process());
  }
}
