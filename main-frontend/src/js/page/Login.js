import { checkIsAuthorized, redirectToPage } from "../main.js";
import Page from "./Page.js";

export class Login extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
  }

  process() {
    checkIsAuthorized()
    .then(() => redirectToPage("home"))
    .catch(() => this.replacePage("login")
                     .then(() => {
                       const form = document.getElementById("loginForm");
                       const sendForm = formData => this._authorizationService.signIn(formData)
                                                        .then(response => {
                                                          $(form)
                                                          .dimmer("hide");
                                                        });
                       this.defineFormSubmitCallback(form, sendForm);
                       super.process();
                     }));
  }
}
