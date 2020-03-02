import {Page} from "./Page.js";

export class Jobs extends Page {
  constructor(props) {
    super(props);
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
    jobService.getJobs().then(response => {
      const jobs = response.body;
      $.get(
          templateHelper.getTemplatePath(this._cardTemplateName),
          cardBody => {
            $.tmpl(cardBody, jobs)
             .appendTo(this._containerSelector);
            limitContentText(
                this._cardDescriptionSelector,
                this._maxDescriptionLength
            );
            $.get(
                templateHelper.getTemplatePath(this._popupTemplateName),
                popupBody => {
                  $(this._modalsSelector)
                  .remove();
                  $.tmpl(popupBody, jobs)
                   .appendTo(this._containerSelector);
                  $(this._containerSelector)
                  .dimmer("hide");
                  $(this._cardSelector)
                  .unbind();
                  $(this._cardSelector)
                  .click(function (event) {
                    event.preventDefault();
                    const id = $(this)
                    .attr("id");
                    $(this._getModalSelectorById(id))
                    .modal("show");
                  });
                }
            );
          }
      );
    });
  }

  process() {
    replacePage("jobs")
    .then(this._loadData())
    .then(() => super.process());
  }
}

export default Jobs;
