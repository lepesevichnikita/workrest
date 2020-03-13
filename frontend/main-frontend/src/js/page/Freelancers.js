import { FreelancerService } from "/frontend/main-frontend/src/js/api/index.js";
import { AuthorizationService } from "/frontend/src/js/domain/api/index.js";
import { Page } from "/frontend/src/js/domain/component/index.js";

export class Freelancers extends Page {
  constructor(props) {
    super(props);
    this._freelancers = [];
    this.addListener("div[data-action=show]", ["click", this._onShowClick.bind(this), false]);
  }

  process() {
    this.showDimmer();
    this._authorizationService.checkIsAuthorized()
        .then((authorized) => {
          if (authorized) {
            this._loadData();
          } else {
            this.redirectToPage("login");
          }
        })
        .catch(error => {
          console.error(error);
          this._renderPage();
        });
  }

  _onShowClick(event) {
    event.preventDefault();
    const targetId = event.currentTarget.getAttribute("data-target");
    const modalWindow = $(`#${targetId}`);
    modalWindow.modal({detachable: false})
               .modal("show");
  }

  get _freelancerService() {
    return this.locator.getServiceByClass(FreelancerService);
  }

  get _authorizationService() {
    return this.locator.getServiceByClass(AuthorizationService);
  }

  _renderPage() {
    this.replacePage("freelancers", {freelancers: this._freelancers})
        .finally(() => {
          super.process();
          this.hideDimmer();
        });
  }

  _loadData() {
    this._freelancerService.getFreelancers()
        .then(response => this._freelancers = response.body)
        .finally(() => this._renderPage());
  }
}

Freelancers.DEFAULT_MAX_DESCRIPTION_LENGTH = 70;

export default Freelancers;
