import { FreelancerService } from "/frontend/main-frontend/src/js/api/index.js";
import { redirectToPage } from "/frontend/main-frontend/src/js/main.js";
import { Page } from "./Page.js";

export class Freelancers extends Page {
  constructor(props) {
    super(props);
    this._authorizationService = props.authorizationService;
    this._freelancerService = new FreelancerService(props);
    this.addListener("div[data-action=show]", ["click", this._onShowClick.bind(this), false]);
  }

  process() {
    this.showDimmer();
    this._authorizationService.checkIsAuthorized()
        .then(() => this._loadData())
        .catch(() => redirectToPage("login"))
        .finally(() => this.hideDimmer());
  }

  _onShowClick(event) {
    event.preventDefault();
    const targetId = event.currentTarget.getAttribute("data-target");
    const modalWindow = $(`#${targetId}`);
    modalWindow.modal({detachable: false})
               .modal("show");
  }

  _loadData() {
    this._freelancerService.getFreelancers()
        .then(response => this.replacePage("freelancers", {
          freelancers: response.body
        })
                              .finally(() => super.process()));
  }
}

Freelancers.DEFAULT_MAX_DESCRIPTION_LENGTH = 70;

export default Freelancers;
