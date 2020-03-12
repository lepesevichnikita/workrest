import { JobService } from "/frontend/main-frontend/src/js/api/index.js";
import { AuthorizationService } from "/frontend/src/js/domain/api/index.js";
import { Page } from "/frontend/src/js/domain/component/index.js";

export class Jobs extends Page {
  constructor(props) {
    super(props);
    this.addListener("div[data-action=show]", ["click", this._onShowClick.bind(this), false]);
  }

  get _authorizationService() {
    return this.locator.getServiceByClass(AuthorizationService);
  }

  _onShowClick(event) {
    event.preventDefault();
    const targetId = event.currentTarget.getAttribute("data-target");
    const modalWindow = $(`#${targetId}`);
    modalWindow.modal({detachable: false})
               .modal("show");
  }

  get _jobService() {
    return this.locator.getServiceByClass(JobService);
  }

  process() {
    this.showDimmer();
    this._jobService.getJobs()
        .then(response => this.replacePage("jobs", {
          jobs: response.body
        })
                              .finally(() => {
                                super.process();
                                this.hideDimmer();
                              }));
  }
}

export default Jobs;
