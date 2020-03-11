import { JobService } from "/frontend/main-frontend/src/js/api/index.js";
import { Page } from "./Page.js";

export class Jobs extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
    this._jobService = new JobService(props);
    this.addListener("div[data-action=show]", ["click", this._onShowClick.bind(this), false]);
  }

  process() {
    this._loadData();
  }

  _onShowClick(event) {
    event.preventDefault();
    const targetId = event.currentTarget.getAttribute("data-target");
    const modalWindow = $(`#${targetId}`);
    modalWindow.modal({detachable: false})
               .modal("show");
  }

  _loadData() {
    this._jobService.getJobs()
        .then(response => this.replacePage("jobs", {
          jobs: response.body
        })
                              .finally(() => super.process()));
  }
}

export default Jobs;
