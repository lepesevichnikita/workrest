import { redirectToPage } from "../main.js";
import Page from "./Page.js";

export class Login extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
    this._loginInfo = {};
    this.addListener("input[name=login]", ["change", this._onLoginChange.bind(this), false])
        .addListener("input[name=password]", ["change", this._onPasswordChange.bind(this), false])
        .addListener("#loginForm", ["submit", this._onFormSubmit.bind(this), false]);
  }

  process() {
    this._authorizationService.checkIsAuthorized()
        .then(() => redirectToPage("home"))
        .catch(() => this.replacePage("login")
                         .then(() => super.process()));
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
    this._authorizationService.signIn(this._loginInfo);
  }
}

export default Login;