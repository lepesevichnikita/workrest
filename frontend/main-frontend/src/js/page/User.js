import { JobService, UserService } from "../api";
import { TemplateHelper } from "../helper";
import { checkIsAuthorized, loadTemplate, redirectToPage } from "../main.js";
import { Page } from "./Page.js";

export class User extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
    this._userService = new UserService(props);
    this._jobService = new JobService(props);
    this._templateHelper = new TemplateHelper();
    this._freelancerProfile = {};
    this._employerProfile = {};
    this._job = {};
    this.addListener(User.Selectors.FREELANCER_DESCRIPTION_INPUT, ["change", this._freelancerDescriptionChange.bind(this), false])
        .addListener(User.Selectors.FREELANCER_SKILL_ADD_BUTTON, ["click", this._freelancerSkillAddClick.bind(this), false])
        .addListener(User.Selectors.FREELANCER_FORM, ["submit", this._freelancerFormSubmit.bind(this), false])
        .addListener(User.Selectors.EMPLOYER_DESCRIPTION_INPUT, ["change", this._employerDescriptionChange.bind(this), false])
        .addListener(User.Selectors.EMPLOYER_FORM, ["submit", this._employerFormSubmit.bind(this), false])
        .addListener(User.Selectors.JOB_DESCRIPTION_INPUT, ["change", this._jobDescriptionChange.bind(this), false])
        .addListener(User.Selectors.JOB_SKILL_ADD_BUTTON, ["click", this._jobSkillAddClick.bind(this), false])
        .addListener(User.Selectors.JOB_FORM, ["submit", this._jobFormSubmit.bind(this), false]);
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

  get _jobSkillInput() {
    return document.querySelector(User.Selectors.JOB_SKILL_INPUT);
  }

  get _freelancerSkillInput() {
    return document.querySelector(User.Selectors.FREELANCER_SKILL_INPUT);
  }

  _renderPage() {
    this._authorizationService.checkIsAuthorized()
        .then(() => this._loadData());
  }

  _loadData() {
    this.showDimmer();
    this._userService.getCurrentUser()
        .then(response => {
          this.replacePage("user", this._user)
              .then(() => this.replacePage("user", response.body))
              .finally(() => super.process());
        });
  }

  _freelancerDescriptionChange(event) {
    event.preventDefault();
    this._freelancerProfile.description = event.target.value;
  }

  _employerDescriptionChange(event) {
    event.preventDefault();
    this._employerProfile.description = event.target.value;
  }

  _jobDescriptionChange(event) {
    event.preventDefault();
    this._job.description = event.target.value;
  }

  _freelancerSkillAddClick(event) {
    event.preventDefault();
    const freelancerSkills = this._freelancerProfile.skills || [];
    const input = this._freelancerSkillInput.value;
    this._freelancerSkillInput.value = "";
    this._splitInputAndPushUniqueToArray(freelancerSkills, input);
    this._freelancerProfile.skills = freelancerSkills;
    loadTemplate("#freelancerSkills", this._templateHelper.getTemplatePath("general/tags"), {skills: this._freelancerProfile.skills})
    .then(() => {
      $("#freelancerSkills span")
      .click((event) => {
        event.preventDefault();
        const skill = event.currentTarget.getAttribute("data-name");
        let skills = this._freelancerProfile.skills || [];
        skills = skills.filter(skillInArray => !skillInArray.includes(skill));
        this._freelancerProfile.skills = skills;
      });
    });
  }

  _jobSkillAddClick(event) {
    event.preventDefault();
    const jobSkills = this._job.skills || [];
    const input = this._jobSkillInput.value;
    this._jobSkillInput.value = "";
    this._splitInputAndPushUniqueToArray(jobSkills, input);
    this._job.skills = jobSkills;
    console.dir(this._job.skills);
  }

  _freelancerFormSubmit(event) {
    event.preventDefault();
    this.showDimmer();
    this._userService.updateFreelancerProfile(this._freelancerProfile)
        .then(response => this.replacePage("user", response.body));
  }

  _employerFormSubmit(event) {
    event.preventDefault();
    this.showDimmer();
    this._userService.updateEmployerProfile(this._employerProfile)
        .then(response => this.replacePage("user", response.body))
        .finally(() => this.hideDimmer());
  }

  _jobFormSubmit(event) {
    event.preventDefault();
    this.showDimmer();
    this._jobService.createJob(this._job)
        .then(() => this._loadData())
        .finally(() => this.hideDimmer());
  }

  _splitInputAndPushUniqueToArray(array, input) {
    const splittedInput = input.split(/[\s,]+/);
    splittedInput.forEach(word => this._addUniqueItemToArray(array, $.trim(word)));
  }

  _addUniqueItemToArray(array, item) {
    if (!array.includes(item)) {
      array.push(item);
    }
  }
}

User.Selectors = {
  FREELANCER_SKILL_ADD_BUTTON: "#freelancerProfileForm a[data-action=skillAdd]",
  FREELANCER_SKILL_INPUT: "#freelancerProfileForm input[name=skill]",
  JOB_SKILL_ADD_BUTTON: "#jobForm a[data-action=skillAdd]",
  JOB_SKILL_INPUT: "#jobForm input[name=skill]",
  FREELANCER_DESCRIPTION_INPUT: "#freelancerProfileForm textarea[name=description]",
  EMPLOYER_DESCRIPTION_INPUT: "#employerProfileForm textarea[name=description]",
  JOB_DESCRIPTION_INPUT: "#jobForm textarea[name=description]",
  FREELANCER_FORM: "#freelancerProfileForm",
  EMPLOYER_FORM: "#employerProfileForm",
  JOB_FORM: "#jobForm"
};

export default User;
