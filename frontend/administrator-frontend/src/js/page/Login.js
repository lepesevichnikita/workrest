import { AuthorizationService } from "/frontend/src/js/domain/api/index.js";
import { Page } from "/frontend/src/js/domain/component/index.js";

export class Login extends Page {
  constructor(props) {
    super(props);
    this.addListener(Login.FORM_SELECTOR, ["submit", this._onFormSubmit.bind(this), false]);
  }

  get _authorizationService() {
    return this.locator.getServiceByClass(AuthorizationService);
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
    this.showDimmer();
    this._authorizationService.signIn(fields)
        .catch(error => this.addErrorsToForm(Login.FORM_SELECTOR, error.response.body))
        .finally(() => {
          $(Login.FORM_SELECTOR)
          .form("reset");
          this.hideDimmer();
        });
  }

  process() {
    this.showDimmer();
    this._authorizationService.checkIsAuthorized()
        .then((authorized) => {
          if (authorized) {
            this.redirectToPage("users");
          } else {
            this.replacePage("login")
                .then(() => this._setValidationOnLoginForm())
                .finally(() => super.process());
          }
        })
        .catch(console.dir)
        .finally(() => this.hideDimmer());
  }
}

Login.FORM_SELECTOR = "#loginForm";

export default Login;
