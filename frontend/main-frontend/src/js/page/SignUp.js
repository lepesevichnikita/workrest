import { AuthorizationService } from "/frontend/src/js/domain/api/index.js";
import { Page } from "/frontend/src/js/domain/component/index.js";

export class SignUp extends Page {
  constructor(props) {
    super(props);
    this.addListener(SignUp.FORM_SELECTOR, ["submit", this._onFormSubmit.bind(this), false]);
  }

  process() {
    this.showDimmer();
    this._authorizationService.checkIsAuthorized()
        .then((authorized) => {
          if (!authorized) {
            this.replacePage("signup")
                .then(() => this._setValidationOnSignUpForm())
                .finally(() => super.process())
          } else {
            this.redirectToPage("user");
          }
        })
        .catch(console.error)
        .finally(() => this.hideDimmer());
  }

  get _authorizationService() {
    return this.locator.getServiceByClass(AuthorizationService);
  }

  _onFormSubmit(event) {
    event.preventDefault();
    const loginForm = $(SignUp.FORM_SELECTOR);
    loginForm.form("validate form");
  }

  _signUp(event, fields) {
    fields.eulaAgreed = fields.eulaAgreed == "on";
    this.showDimmer();
    this._authorizationService.signUp(fields)
        .catch(error => this.addErrorsToForm(SignUp.FORM_SELECTOR, error.response.body))
        .finally(() => {
          this.hideDimmer();
        });
  }

  _setValidationOnSignUpForm() {
    const loginForm = $("#signupForm");
    loginForm.form({
                     login: {identifier: "login", rules: [{type: "empty", prompt: "Login is required"}]},
                     password: {identifier: "password", rules: [{type: "empty", prompt: "Password is required"}]},
                     passwordConfirmation: {
                       identifier: "passwordConfirmation",
                       depends: "password",
                       rules: [{type: "empty", prompt: "Password confirmation is required"}, {type: "match[password]", prompt: "Passwords should match"}]
                     },
                     eulaAgreed: {identifier: "eulaAgreed", rules: [{type: "checked", prompt: "EULA must be agreed"}]}
                   }, {
                     onSuccess: this._signUp.bind(this)
                   });
  }
}

SignUp.FORM_SELECTOR = "#signupForm";
export default SignUp;
