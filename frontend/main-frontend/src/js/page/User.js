import { JobService, UserService } from "/frontend/main-frontend/src/js/api/index.js";
import { AuthorizationService, RestClient } from "/frontend/src/js/domain/api/index.js";
import { Page } from "/frontend/src/js/domain/component/index.js";
import { Renderer } from "/frontend/src/js/domain/service/index.js";

export class User extends Page {
  constructor(props) {
    super(props);
    this._freelancerProfile = {};
    this._employerProfile = {};
    this._job = {};
    this.addListener(User.Selectors.FREELANCER_SKILL_ADD_BUTTON,
                     ["click", this._freelancerSkillAddClick.bind(this), false])
        .addListener(User.Selectors.FREELANCER_FORM, ["submit", this._freelancerFormSubmit.bind(this), false])
        .addListener(User.Selectors.EMPLOYER_FORM, ["submit", this._employerFormSubmit.bind(this), false])
        .addListener(User.Selectors.JOB_SKILL_ADD_BUTTON, ["click", this._jobSkillAddClick.bind(this), false])
        .addListener(User.Selectors.JOB_FORM, ["submit", this._jobFormSubmit.bind(this), false])
        .addListener(User.Selectors.EMPLOYER_PROFILE_UPDATE_BUTTON, ["click", this._editEmployerProfileClick.bind(this), false])
        .addListener(User.Selectors.FREELANCER_PROFILE_UPDATE_BUTTON, ["click", this._editFreelancerProfileClick.bind(this), false])
        .addListener(User.Selectors.JOB_UPDATE_BUTTON, ["click", this._updateJobClick.bind(this), false])
        .addListener(User.Selectors.JOB_CREATE_BUTTON, ["click", this._createJobClick.bind(this), false])
        .addListener(User.Selectors.JOB_DELETE_BUTTON, ["click", this._deleteJob.bind(this), false])
        .addListener(User.Selectors.JOB_START_BUTTON, ["click", this._startJob.bind(this), false])
        .addListener(User.Selectors.JOB_FINISH_BUTTON, ["click", this._finishJob.bind(this), false])
        .addListener(User.Selectors.FREELANCER_SKILL_REMOVE_BUTTON,
                     ["click", this._removeSkillFromFreelancerProfileForm.bind(this), false])
        .addListener(User.Selectors.JOB_SKILL_REMOVE_BUTTON, ["click", this._removeSkillFromJobForm.bind(this), false]);
  }

  process() {
    this.showDimmer();
    this._freelancerProfile = Object.assign({}, User.EMPTY_FREELANCER_PROFILE);
    this._employerProfile = Object.assign({}, User.EMPTY_EMPLOYER_PROFILE);
    this._job = Object.assign({}, User.EMPTY_JOB);
    this._authorizationService.checkIsAuthorized()
        .then((authorized) => {
          if (authorized) {
            this._loadCurrentUser();
          } else {
            this.redirectToPage("login");
          }
        })
        .catch(console.error);
  }

  get _userService() {
    return this.locator.getServiceByClass(UserService);
  }

  _startJob(event) {
    event.preventDefault();
    const id = event.currentTarget.getAttribute("data-id");
    this._jobService.startJob(id).then(() => this.process());
  }

  _deleteJob(event) {
    event.preventDefault();
    const id = event.currentTarget.getAttribute("data-id");
    this._jobService.deleteJob(id).then(() => this.process());
  }

  _finishJob(event) {
    event.preventDefault();
    const id = event.currentTarget.getAttribute("data-id");
    this._jobService.finishJob(id).then(() => this.process());
  }

  get _renderer() {
    return this.locator.getServiceByClass(Renderer);
  }

  get _restClient() {
    return this.locator.getServiceByClass(RestClient);
  }

  _editFreelancerProfileClick(event) {
    event.preventDefault();
    this._renderer.renderModal("freelancer/form", this._freelancerProfile)
        .finally(() => super.process());
  }

  _editEmployerProfileClick(event) {
    event.preventDefault();
    this._renderer.renderModal("employer/form", this._employerProfile)
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
          $(User.Selectors.FREELANCER_SKILL_REMOVE_BUTTON )
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
    skills = skills.filter(skillInArray => skillInArray !== skill);
    this[skillsOwnerName].skills = skills;
  }

  get _authorizationService() {
    return this.locator.getServiceByClass(AuthorizationService);
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
          $(User.Selectors.JOB_SKILL_REMOVE_BUTTON)
          .click(this._removeSkillFromJobForm.bind(this));
        });
  }

  _freelancerFormSubmit(event) {
    event.preventDefault();
    const freelancerForm = $(User.Selectors.FREELANCER_FORM);
    this._freelancerProfile.description = freelancerForm.find("textarea")
                                                        .val();
    this._userService.updateFreelancerProfile(this._freelancerProfile)
        .catch(console.error)
        .finally(() => {
          $(".ui.modals, .ui.modal")
          .remove();
          this.process();
          this.hideDimmer();
        });
  }

  _employerFormSubmit(event) {
    event.preventDefault();
    const employerForm = $(User.Selectors.EMPLOYER_FORM);
    this._employerProfile.description = employerForm.find("textarea")
                                                    .val();
    this._userService.updateEmployerProfile(this._employerProfile)
        .finally(() => {
          $(".ui.modals, .ui.modal").remove();
          this.process();
          this.hideDimmer();
        });
  }

  _jobFormSubmit(event) {
    event.preventDefault();
    const jobForm = $(User.Selectors.JOB_FORM);
    const jobId = jobForm.attr('data-id');
    this._job.description = jobForm.find("textarea")
                                   .val();
    this._job.endDateTime = jobForm.find("input[name=endDateTime]")
                                   .val();
    this.showDimmer();
    const promise = jobId == null
        ? this._jobService.createJob(this._job)
        : this._jobService.updateJob(jobId, this._job);
    promise.then(() => this.process())
        .finally(() => {
          $(".ui.modals, .ui.modal").remove();
          this.process();
          this.hideDimmer();
        });
  }

  _splitInputAndPushUniqueToArray(array, input) {
    const splittedInput = input.split(/[\s]+/);
    splittedInput.forEach(word => this._addUniqueItemToArray(array, $.trim(word)));
  }

  _addUniqueItemToArray(array, item) {
    if (!array.includes(item)) {
      array.push(item);
    }
  }

  get _jobService() {
    return this.locator.getServiceByClass(JobService);
  }

  get _jobSkillInput() {
    return document.querySelector(User.Selectors.JOB_SKILL_INPUT);
  }

  get _freelancerSkillInput() {
    return document.querySelector(User.Selectors.FREELANCER_SKILL_INPUT);
  }

  _createJobClick(event) {
    event.preventDefault();
    this._job = Object.assign({}, User.EMPTY_JOB);
    this._renderer.renderModal("job/form", this._job)
        .finally(() => super.process());
  }

  _updateJobClick(event) {
    event.preventDefault();
    const jobId = event.currentTarget.getAttribute("data-id");
    this._job = this._employerProfile.jobs.find(job => job.id == jobId);
    this._job.skills = this._job.skills.map(skill => skill.name ? skill.name : null)
                           .filter(skillName => skillName != null);
    this._renderer.renderModal("job/form", this._job)
        .catch((error) => {
          console.error(error);
          $(".ui.modals,.ui.modal").remove();
        })
        .finally(() => {
          super.process();
        })
  }

  async _renderSkillsInContainer(containerSelector, skills) {
    const skillsContainer = $(containerSelector);
    const builtTemplate = await this._renderer.buildTemplate("general/tags", {skills});
    return skillsContainer.html(builtTemplate);
  }

  _loadCurrentUser() {
    this._userService.getCurrentUser()
        .then(response => {
          this._user = response.body;
          if (this._user.freelancerProfile) {
            this._freelancerProfile = this._user.freelancerProfile;
          }
          if (this._freelancerProfile && this._freelancerProfile.skills) {
            this._freelancerProfile.skills = this._freelancerProfile.skills.map(skill => skill.name);
          }
          if (this._user.employerProfile) {
            this._employerProfile = this._user.employerProfile;
          }
        })
        .then(() => this.replacePage("user", this._user)
                        .finally(() => {
                          super.process();
                          this.hideDimmer();
                        }));
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
  JOB_FORM: "#jobForm",
  JOB_CREATE_BUTTON: "div[data-action=create][data-target=job]",
  JOB_UPDATE_BUTTON: "div[data-action=update][data-target=job]",
  JOB_DELETE_BUTTON: "div[data-action=delete]",
  JOB_START_BUTTON: "div[data-action=start]",
  JOB_FINISH_BUTTON: "div[data-action=finish]",
  FREELANCER_SKILL_REMOVE_BUTTON: "#freelancerSkills a",
  JOB_SKILL_REMOVE_BUTTON: "#jobSkills a"
};

User.EMPTY_FREELANCER_PROFILE = {description: null, skills: []};
User.EMPTY_EMPLOYER_PROFILE = {description: null};
User.EMPTY_JOB = {description: null, skills: [], id: null, endDateTime: null};

export default User;
