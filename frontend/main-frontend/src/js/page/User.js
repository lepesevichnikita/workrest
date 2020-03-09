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
    this.addListener(User.Selectors.FREELANCER_SKILL_ADD_BUTTON, ["click", this._freelancerSkillAddClick.bind(this), false])
        .addListener(User.Selectors.FREELANCER_FORM, ["submit", this._freelancerFormSubmit.bind(this), false])
        .addListener(User.Selectors.EMPLOYER_FORM, ["submit", this._employerFormSubmit.bind(this), false])
        .addListener(User.Selectors.JOB_SKILL_ADD_BUTTON, ["click", this._jobSkillAddClick.bind(this), false])
        .addListener(User.Selectors.JOB_FORM, ["submit", this._jobFormSubmit.bind(this), false])
        .addListener(User.Selectors.EMPLOYER_PROFILE_UPDATE_BUTTON, ["click", this._showModal.bind(this), false])
        .addListener(User.Selectors.FREELANCER_PROFILE_UPDATE_BUTTON, ["click", this._showModal.bind(this), false]);
  }

  process() {
    this.showDimmer();
    checkIsAuthorized()
    .then(() => {
      this._userService.getCurrentUser()
          .then(response => {
            this._user = response.body;
            if (this._user.freelancerProfile) {
              this._freelancerProfile = this._user.freelancerProfile;
            }
            if (this._freelancerProfile && this._freelancerProfile.skills) {
              this._freelancerProfile.skills = this._freelancerProfile.skills.map(skill => skill.name);
            }
            if (this._user._employerProfile) {
              this._employerProfile = this._user.employerProfile;
            }
          })
          .then(() => this._renderPage())
          .then(() => this._initFormsValidators())
    })
    .catch(() => {
      redirectToPage("login");
    })
    .finally(() => {
      this.hideDimmer();
      super.process();
    });
  }

  _showModal(event) {
    event.preventDefault();
    const targetModalId = event.currentTarget.getAttribute("data-target");
    $(`#${targetModalId}`)
    .modal({detachable: false})
    .modal("show");
  }

  _initFormsValidators() {
    const freelancerForm = $(User.Selectors.FREELANCER_FORM);
    const employerForm = $(User.Selectors.EMPLOYER_FORM);
    const jobForm = $(User.Selectors.JOB_FORM);
    freelancerForm.form({
                          description: {
                            identifier: "description", rules: [{
                              type: "empty", prompt: "Description is required"
                            }]
                          }
                        }, {onSuccess: this._updateFreelancerProfile.bind(this)});
    employerForm.form({
                        description: {
                          identifier: "description", rules: [{
                            type: "empty", prompt: "Description is required"
                          }]
                        }
                      }, {onSuccess: this._updateEmployerProfile.bind(this)});
    jobForm.form({
                   description: {
                     identifier: "description", rules: [{
                       type: "empty", prompt: "Description is required"
                     }]
                   }
                 }, {onSuccess: this._processJob.bind(this)});
  }

  get _jobSkillInput() {
    return document.querySelector(User.Selectors.JOB_SKILL_INPUT);
  }

  get _freelancerSkillInput() {
    return document.querySelector(User.Selectors.FREELANCER_SKILL_INPUT);
  }

  _renderPage() {
    this.replacePage("user", this._user)
        .finally(() => super.process());
  }

  _freelancerSkillAddClick(event) {
    event.preventDefault();
    const freelancerSkills = (this._freelancerProfile && this._freelancerProfile.skills) || [];
    const input = this._freelancerSkillInput.value;
    this._freelancerSkillInput.value = "";
    this._splitInputAndPushUniqueToArray(freelancerSkills, input);
    this._freelancerProfile.skills = freelancerSkills;
    this._renderSkillsInFreelancerProfileForm()
        .then(() => {
          $("#freelancerSkills a")
          .click(this._removeSkillFromFreelancerProfileForm.bind(this));
    });
  }

  _removeSkillFromFreelancerProfileForm(event) {
    event.preventDefault();
    this._removeSkillFromArray(event, "freelancerProfile");
    $(event.currentTarget)
    .remove();
  }

  _removeSkillFromJobForm(event) {
    event.preventDefault();
    this._removeSkillFromArray(event, "job");
    $(event.currentTarget)
    .remove();
  }

  _removeSkillFromArray(event, skillsOwner) {
    const skill = event.currentTarget.getAttribute("data-name");
    const skillsOwnerName = `_${skillsOwner}`;
    let skills = this[skillsOwnerName].skills || [];
    skills = skills.filter(skillInArray => !skillInArray.includes(skill));
    this[skillsOwnerName].skills = skills;
  }

  _renderSkillsInContainer(containerSelector, skills) {
    return loadTemplate(containerSelector, this._templateHelper.getTemplatePath("general/tags"), {skills});
  }

  _renderSkillsInFreelancerProfileForm() {
    return this._renderSkillsInContainer("#freelancerSkills", this._freelancerProfile.skills);
  }

  _renderSkillsInJobForm() {
    return this._renderSkillsInContainer("#jobSkills", this._job.skills);
  }

  _jobSkillAddClick(event) {
    event.preventDefault();
    const jobSkills = (this._job && this._job.skills) || [];
    const input = this._jobSkillInput.value;
    this._jobSkillInput.value = "";
    this._splitInputAndPushUniqueToArray(jobSkills, input);
    this._job.skills = jobSkills;
    this._renderSkillsInJobForm()
        .then(() => {
          $("#jobSkills a")
          .click(this._removeSkillFromJobForm.bind(this));
        });
  }

  _freelancerFormSubmit(event) {
    event.preventDefault();
    const freelancerForm = $(User.Selectors.FREELANCER_FORM);
    freelancerForm.form("validate form");
  }

  _employerFormSubmit(event) {
    event.preventDefault();
    const employerForm = $(User.Selectors.EMPLOYER_FORM);
    employerForm.form("validate form");
  }

  _jobFormSubmit(event) {
    event.preventDefault();
    const jobForm = $(User.Selectors.JOB_FORM);
    jobForm.form("validate form");
  }

  _updateEmployerProfile(event, fields) {
    this.showDimmer();
    this._userService.updateEmployerProfile(fields)
        .then(response => this.replacePage("user", response.body))
        .finally(() => this.hideDimmer());
  }

  _updateFreelancerProfile(event, fields) {
    this.showDimmer();
    fields.skills = this._freelancerProfile.skills;
    this._userService.updateFreelancerProfile(this._freelancerProfile)
        .then(response => this.replacePage("user", response.body))
        .finally(() => this.hideDimmer());
  }

  _processJob(event, fields) {
    this.showDimmer();
    let promise = null;
    fields.skills = this._job.skills;
    if (fields.id == 0) {
      promise = this._jobService.createJob(this._job);
    } else {
      promise = this._jobService.updateJob(fields.id, fields);
    }
    promise.then(() => this._renderPage())
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
  FREELANCER_PROFILE_UPDATE_BUTTON: "#freelancerProfile .ui.button[data-action=update]",
  EMPLOYER_PROFILE_UPDATE_BUTTON: "#employerProfile .ui.button[data-action=update]",
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
