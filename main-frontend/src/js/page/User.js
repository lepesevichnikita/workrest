import { UserService } from "../api";
import { TemplateHelper } from "../helper";
import { checkIsAuthorized, loadTemplate, redirectToPage } from "../main.js";
import { Page } from "./Page.js";

export class User extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
    this._userService = new UserService(props);
    this._templateHelper = new TemplateHelper();
  }

  process() {
    checkIsAuthorized()
    .then(() => {
      this._userService.getCurrentUser()
          .then(response => {
            this._user = response.body;
            this._renderPage();
          });
    })
    .catch(() => {
      redirectToPage("login");
    });
  }

  _renderPage() {
    this.replacePage("user", this._user)
        .then(() => {
          this._renderProfile("freelancer");
          this._renderProfile("employer");
        })
        .then(() => {
          super.process();
        });
  }

  _renderProfile(profileType) {
    const profilePropertyPath = `${profileType}Profile`;
    const templateName = this._user[profilePropertyPath] ? `${profileType}/personal_card` : `${profileType}/form`;
    loadTemplate("#freelancerProfile",
                 this._templateHelper.getTemplatePath(templateName),
                 this._user[profilePropertyPath])
    .catch(console.error);
  }
}

export default User;
