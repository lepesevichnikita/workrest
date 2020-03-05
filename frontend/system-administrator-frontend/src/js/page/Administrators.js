import { AdministratorService } from "../api";
import { redirectToPage } from "../main.js";
import { Page } from "./Page.js";

export class Administrators extends Page {
  constructor(props) {
    super(props);
    this._authorizationService = props.authorizationService;
    this._administratorService = new AdministratorService(props);
    this._loginInfo = {};
    this.addListener("input[name=login]", ["change", this._onLoginChange.bind(this), false])
        .addListener("input[name=password]", ["change", this._onPasswordChange.bind(this), false])
        .addListener("#administratorForm", ["submit", this._onFormSubmit.bind(this), false])
        .addListener(".ui.link.card > div.ui.bottom.attached.button", ["click", this._onDeleteClick.bind(this), false]);
  }

  process() {
    this._authorizationService.checkIsAuthorized()
        .then(() => {
          this.showDimmer();
          this._loadData();
        })
        .catch(() => redirectToPage("login"));
  }

  _onLoginChange(event) {
    event.preventDefault();
    this._loginInfo.login = event.target.value;
  }

  _onPasswordChange(event) {
    event.preventDefault();
    this._loginInfo.password = event.target.value;
  }

  _onFormSubmit(event) {
    event.preventDefault();
    this.showDimmer();
    this._administratorService.createAdministrator(this._loginInfo)
        .then(() => this._loadData());
  }

  _loadData() {
    this._administratorService.getAdministrators()
        .then(response => this.replacePage("administrators", {administrators: response.body})
                              .then(() => {
                                super.process();
                              }));
  }

  _onDeleteClick(event) {
    event.preventDefault();
    this.showDimmer();
    const administratorId = event.currentTarget.getAttribute("data-id");
    this._administratorService.deleteAdministrator(administratorId)
        .then(() => this._loadData());
  }
}
