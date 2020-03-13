import { AuthorizationService } from "/frontend/src/js/domain/api/index.js";
import { Page } from "/frontend/src/js/domain/component/index.js";

export class Login extends Page {
  constructor(props) {
    super(props);
    this.addListener(Login.FORM_SELECTOR, ["submit", this._onFormSubmit.bind(this), false]);
  }

  process() {
    this.showDimmer();
    this._authorizationService.checkIsAuthorized()
        .then((authorized) => {
          if (authorized) {
            this.redirectToPage("home");
          } else {
            this._renderPage();
          }
        })
        .catch(error => {
          console.error(error);
          this._renderPage();
        })
        .finally(() => {
          this.hideDimmer();
        });
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

  get _authorizationService() {
    return this.locator.getServiceByClass(AuthorizationService);
  }

  _renderPage() {
    this.replacePage("login")
        .then(() => {
          this._setValidationOnLoginForm();
          super.process();
        });
  }
}

Login.FORM_SELECTOR = "#loginForm";

export default Login;
