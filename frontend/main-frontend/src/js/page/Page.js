import { loadTemplate, redirectToPage } from "/frontend/main-frontend/src/js/main.js";
import { TemplateHelper } from "/frontend/src/js/domain/helper/index.js";

export class Page {
  constructor() {
    this._eventsListeners = {};
    this._templateHelper = new TemplateHelper();
    this.addListener(".ui.link", ["click", this._onLinkClick.bind(this), false]);
  }

  process() {
    this._initializeListeners();
  }

  get templateHelper() {
    return this._templateHelper;
  }

  addListener(selector, eventListener) {
    const listeners = this._eventsListeners[selector] || [];
    this._eventsListeners[selector] = [...listeners, eventListener];
    return this;
  }

  showDimmer() {
    $(".ui.dimmer")
    .dimmer("show");
  }

  hideDimmer() {
    $(".ui.dimmer")
    .dimmer("hide");
  }

  addErrorsToForm(formSelector, errors) {
    const globalErrors = errors.globalErrors || [];
    delete errors.globalErrors;
    const form = $(formSelector);
    form.form("add errors", [...Object.values(errors), ...globalErrors]);
    Object.keys(errors)
          .forEach(fieldName => {
            const message = errors[fieldName];
            form.form("add prompt", fieldName, message);
          });
  }

  replacePage(pageName, pageData = {}) {
    const pageSelector = "#page";
    return new Promise((resolve, reject) => loadTemplate(pageSelector,
                                                         this._templateHelper.getPagePath(pageName.toLowerCase()),
                                                         pageData)
    .then(resolve)
    .catch(reject));
  }

  _initializeListeners() {
    Object.keys(this._eventsListeners)
          .forEach(selector => {
            const listeners = this._eventsListeners[selector] || [];
            document
            .querySelectorAll(selector)
            .forEach(node => listeners.forEach(listener => node.addEventListener(...listener)));
          });
  }

  _onLinkClick(event) {
    event.preventDefault();
    const pageName = event.currentTarget.getAttribute("name");
    redirectToPage(pageName);
  }
}

export default Page;
