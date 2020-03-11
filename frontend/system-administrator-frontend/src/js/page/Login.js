import { redirectToPage } from "/frontend/system-administrator-frontend/src/js/main.js";
import { Page } from "./Page.js";

export class Login extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
    this.addListener(Login.FORM_SELECTOR, ["submit", this._onFormSubmit.bind(this), false]);
  }

  process() {
    this._authorizationService.checkIsAuthorized()
        .then(() => redirectToPage("administrators"))
        .catch(() => this.replacePage("login")
                         .then(() => this._setValidationOnLoginForm())
                         .finally(() => super.process()));
  }

  _setValidationOnLoginForm() {
    const loginForm = $(Login.FORM_SELECTOR);
    loginForm.form({
                     login: {identifier: "login", rules: [{type: "empty", prompt: "Login is required"}]},
                     password: {identifier: "password", rules: [{type: "empty", prompt: "Password is required"}]}
                   }, {
                     onSuccess: this._signIn.bind(this)
                   });
  }

  _onFormSubmit(event) {
    event.preventDefault();
    const loginForm = $(Login.FORM_SELECTOR);
    loginForm.form("validate form");
  }

  _signIn(event, fields) {
    event.preventDefault();
    this.showDimmer();
    this._authorizationService.signIn(fields)
        .catch(error => this.addErrorsToForm(Login.FORM_SELECTOR, error.response.body))
        .finally(() => {
          $(Login.FORM_SELECTOR)
          .form("reset");
          this.hideDimmer();
        });
  }
}

Login.FORM_SELECTOR = "#loginForm";

export default Login;
