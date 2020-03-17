import { JobService, UserService } from "/frontend/main-frontend/src/js/api/index.js";
import { AuthorizationService } from "/frontend/src/js/domain/api/index.js";
import { Page } from "/frontend/src/js/domain/component/index.js";
import { Renderer } from "/frontend/src/js/domain/service/index.js";

export class Jobs extends Page {
  constructor(props) {
    super(props);
    this._jobs = [];
    this.addListener("div[data-action=show]", ["click", this._onShowClick.bind(this), false]);
    this.addListener("form[data-action=send-message]", ["submit", this._onMessageSubmit.bind(this), false]);
  }

  process() {
    this.showDimmer();
    this._jobService.getJobs()
        .then(response => this._jobs = JSON.parse(response.text))
        .finally(() => this.replacePage("jobs", {jobs: this._jobs})
                           .finally(() => {
                             super.process();
                             this.hideDimmer();
                           }));
  }

  get _authorizationService() {
    return this.locator.getServiceByClass(AuthorizationService);
  }

  async _onMessageSubmit(event) {
    event.preventDefault();
    this.showDimmer();
    const form = $(event.currentTarget);
    const jobId = event.currentTarget.getAttribute("data-id");
    const text = form.find("input[name=text]").val();
    this._jobService.sendMessage(jobId, {text: text}).finally(() => {
      this.hideDimmer();
      this._showJobByIdInModal(jobId);
    });
  }

  async _onShowClick(event) {
    event.preventDefault();
    const jobId = event.currentTarget.getAttribute("data-id");
    this._showJobByIdInModal(jobId);
  }

  async _showJobByIdInModal(jobId) {
    this.showDimmer();
    const job = this._jobs.find(job => job.id == jobId);
    job.messages = [];
    const currentUser = (await this._userService.getCurrentUser()).body;
    let jobPopUpName = "job/popup";
    if (currentUser) {
      job.messages = (await this._jobService.getAllMessagesById(jobId)).body;
      const isCurrentUserOwnerOfJob = currentUser.employerProfile && currentUser.employerProfile.jobs.find(employerJob => employerJob.id == job.id);
      const alreadyMessaged = job.messages && job.messages.find(message => message.author.id == currentUser.id);
      if (!isCurrentUserOwnerOfJob && !alreadyMessaged && currentUser.freelancerProfile) {
        jobPopUpName = "job/popup_with_message_input";
      }
    }
    this.hideDimmer();
    this._renderer.renderModal(jobPopUpName, job)
        .catch(error => {
          console.error(error);
          $(".ui.modal, .ui.modals").remove();
        }).finally(() => super.process());
  }

  get _jobService() {
    return this.locator.getServiceByClass(JobService);
  }

  get _userService() {
    return this.locator.getServiceByClass(UserService);
  }

  get _renderer() {
    return this.locator.getServiceByClass(Renderer);
  }
}

export default Jobs;
