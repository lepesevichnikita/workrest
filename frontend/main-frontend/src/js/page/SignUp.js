import { redirectToPage } from "../main.js";
import { Page } from "./Page.js";

export class SignUp extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
    this._loginInfo = {};
    this.addListener(SignUp.FORM_SELECTOR, ["submit", this._onFormSubmit.bind(this), false]);
  }

  process() {
    this.showDimmer();
    this._authorizationService.checkIsAuthorized()
        .then(() => redirectToPage("home"))
        .catch(() => this.replacePage("signup")
                         .then(() => this._setValidationOnSignUpForm())
                         .then(() => super.process()))
        .finally(() => this.hideDimmer());
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
}

SignUp.FORM_SELECTOR = "#signupForm";
export default SignUp;
