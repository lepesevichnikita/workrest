import { JobService } from "../api";
import { limitContentText } from "../main.js";
import { Page } from "./Page.js";

export class Jobs extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
    this._jobService = new JobService(props);
    this._cardTemplateName = "job/card";
    this._popupTemplateName = "job/popup";
    this._containerSelector = "#job-cards";
    this._maxDescriptionLength = 70;
    this._cardDescriptionSelector = ".ui.card > .content > .description";
    this._modalsSelector = ".ui.modals";
    this._cardSelector = ".ui.link.card";
  }

  _getModalSelectorById(id) {
    return `#job${id}.ui.modal`;
  }

  _loadData() {
    this._jobService.getJobs()
        .then(response => {
          const jobs = response.body;
          $.get(this.templateHelper.getTemplatePath(this._cardTemplateName), cardBody => {
            $.tmpl(cardBody, jobs)
             .appendTo(this._containerSelector);
            limitContentText(this._cardDescriptionSelector, this._maxDescriptionLength);
            $.get(this.templateHelper.getTemplatePath(this._popupTemplateName), popupBody => {
              $(this._modalsSelector)
              .remove();
              $.tmpl(popupBody, jobs)
               .appendTo(this._containerSelector);
              $(this._containerSelector)
              .dimmer("hide");
              $(this._cardSelector)
              .unbind();
              $(this._cardSelector)
              .click(event => {
                event.preventDefault();
                const item = $(event.currentTarget);
                const id = item.attr("id");
                $(this._getModalSelectorById(id))
                .modal("show");
              });
            });
          }
      );
    });
  }

  process() {
    this.replacePage("jobs")
        .then(() => this._loadData())
        .then(() => super.process());
  }
}

export default Jobs;
