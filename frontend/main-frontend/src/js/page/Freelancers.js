import { FreelancerService } from "../api";
import { TemplateHelper } from "../helper";
import { limitContentText, loadTemplate, redirectToPage } from "../main.js";
import Page from "./Page.js";

export class Freelancers extends Page {
  constructor(props) {
    super();
    this._cardTemplateName = "freelancer/card";
    this._popupTemplateName = "freelancer/popup";
    this._cardsConainerSeelector = "#freelancer-cards";
    this._popupConainerSeelector = "#freelancer-popups";
    this._cardDescriptionSelector = ".ui.card > .content > .description";
    this._modalsSelector = ".ui.modals";
    this._cardSelector = ".ui.link.card";
    this._freelancerService = new FreelancerService(props);
    this._templateHelper = new TemplateHelper();
  }

  static _getModalSelectorById(id) {
    return `#freelancer${id}.ui.modal`;
  }

  _loadData() {
    this._freelancerService.getFreelancers()
        .then(response => {
      const freelancers = response.body;
          loadTemplate(this._cardsConainerSeelector,
                       this._templateHelper.getTemplatePath(this._cardTemplateName),
                       freelancers)
          .then(response => {
            limitContentText(this._cardDescriptionSelector, Freelancers.DEFAULT_MAX_DESCRIPTION_LENGTH);
            $(this._modalsSelector)
            .remove(); // Remove previous placed by Semantic UI modals
            $(this._cardSelector)
            .unbind();
            loadTemplate(this._popupConainerSeelector,
                         this._templateHelper.getTemplatePath(this._popupTemplateName),
                         freelancers)
            .then(response => {
              $(this._cardSelector)
              .click(function(event) {
                event.preventDefault();
                const id = $(this)
                .attr("id");
                $(Freelancers._getModalSelectorById(id))
                .modal("show");
              });
            });
          });
    });
  }

  process() {
    checkIsAuthorized()
    .then(() => {
      this.replacePage("freelancers")
          .then(() => {
            this._loadData();
            super.process();
          });
    })
    .catch(() => redirectToPage("login"));
  }
}

Freelancers.DEFAULT_MAX_DESCRIPTION_LENGTH = 70;

export default Freelancers;
