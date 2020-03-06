import { redirectToPage } from "../main.js";
import Page from "./Page.js";

export class Login extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
    this._loginInfo = {};
    this.addListener("input[name=login]", ["change", this._onLoginChange.bind(this), false])
        .addListener("input[name=password]", ["change", this._onPasswordChange.bind(this), false])
        .addListener("form", ["submit", this._onFormSubmit.bind(this), false]);
  }

  process() {
    this.showDimmer();
    return this._authorizationService.checkIsAuthorized()
               .then(() => redirectToPage("users"))
               .catch((error) => this.replacePage("login")
                                     .finally(() => super.process()));
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
    this._authorizationService.signIn(this._loginInfo)
        .finally(() => this.hideDimmer());
  }
}

export default Login;
