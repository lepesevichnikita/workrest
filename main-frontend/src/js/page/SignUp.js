import { checkIsAuthorized, redirectToPage } from "../main.js";
import { Page } from "./Page.js";

export class SignUp extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
  }

  process() {
    checkIsAuthorized()
    .then(() => redirectToPage("home"))
    .catch(() => this.replacePage("signup")
                     .then(() => {
                       const form = document.getElementById("signupForm");
                       const sendForm = formData => this._authorizationService
                                                        .signUp(formData)
                                                        .then(() => $(form)
                                                        .dimmer("hide"));
                       this.defineFormSubmitCallback(form, sendForm);
                     }));
  }
}

export default SignUp;
