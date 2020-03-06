import {JobService, UserService} from "../api";
import {TemplateHelper} from "../helper";
import {checkIsAuthorized, redirectToPage} from "../main.js";
import {Page} from "./Page.js";

export class User extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
    this._userService = new UserService(props);
    this._jobService = new JobService(props);
    this._templateHelper = new TemplateHelper();
    this.addListener('#freelancerProfileForm > input[name=description]',
                     ['change', this._freelancerDescriptionChange.bind(this), false])
        .addListener('#freelancerProfileForm > input[name=skillAdd]',
                     ['skill', this._freelancerSkillAddClick.bind(this), false])
        .addListener('#freelncerProfileForm', ['submit', this._freelancerFormSubmit.bind(this), false])
        .addListener('#employerProfileForm > input[name=description]',
                     ['change', this._employerDescriptionChange.bind(this), false])
        .addListener('#employerProfileForm', ['submit', this._employerFormSubmit.bind(this), false])
        .addListener('#jobProfileForm > input[name=description]',
                     ['change', this._jobDescriptionChange.bind(this), false])
        .addListener('#jobProfileForm > input[name=skillAdd]', ['skill', this._jobSkillAddClick.bind(this), false])
        .addListener('#freelncerProfileForm', ['submit', this._jobFormSubmit.bind(this), false])
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
          if (this._user.currentState.name == 'Verified') {
            this._addJobFormCallbacks();
          }
        })
        .then(() => {
          super.process();
        });
  }
}

export default User;
