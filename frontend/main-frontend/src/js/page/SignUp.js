import {redirectToPage} from "../main.js";
import {Page} from "./Page.js";

export class SignUp extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
    this._loginInfo = {};
    this.addListener("input[name=login]", ["change", this._onLoginChange.bind(this), false])
        .addListener("input[name=password]", ["change", this._onPasswordChange.bind(this), false])
        .addListener("input[name=passwordConfirmation]",
                     ["change", this._onPasswordConfirmationChange.bind(this), false])
        .addListener("input[name=eulaAgreed]", ["change", this._onEulaAgreedChange.bind(this), false])
        .addListener("form", ["submit", this._onFormSubmit.bind(this), false]);
  }

  process() {
    this.showDimmer();
    return this._authorizationService.checkIsAuthorized()
               .then(() => redirectToPage("home"))
               .catch((error) => this.replacePage("signup")
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
    this._authorizationService.signUp(this._loginInfo)
        .catch(error => {
          if (error.status == 422) {
            this.addErrorsToForm("form", error.response.body);
          }
          return error;
        })
        .finally(() => {
          $('form')
          .form('clear');
          this.hideDimmer()
        });
  }

  _onPasswordConfirmationChange(event) {
    event.preventDefault();
    this._loginInfo.passwordConfirmation = event.target.value;
  }

  _onEulaAgreedChange(event) {
    event.preventDefault();
    this._loginInfo.eulaAgreed = event.target.value == 'on';
  }
}

export default SignUp;
