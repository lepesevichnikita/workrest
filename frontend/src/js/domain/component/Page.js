import { Renderer } from "/frontend/src/js/domain/service/index.js";

export class Page {
  constructor(props) {
    this._eventsListeners = {};
    this._locator = props.locator;
    this.addListener(".ui.link[name]:not([name=''])", ["click", this._onLinkClick.bind(this), false]);
  }

  get locator() {
    return this._locator;
  }

  process() {
    this._initializeListeners();
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

  redirectToPage(pageName) {
    this._locator.getServiceByName(pageName)
        .process();
  }

  removeModals() {
    $('.ui.modal, .ui.modals').remove();
  }

  async replacePage(pageName, pageData = {}) {
    const pageSelector = "#page";
    const pageTemplateName = `page/${pageName.toLowerCase()}`;
    const builtTemplate = await this._locator.getServiceByClass(Renderer)
                                    .buildTemplate(pageTemplateName, pageData);
    const container = $(pageSelector);
    return container.html(builtTemplate);
  }

  _initializeListeners() {
    Object.keys(this._eventsListeners)
          .forEach(selector => {
            const listeners = this._eventsListeners[selector] || [];
            $(selector)
            .unbind();
            document.querySelectorAll(selector)
                    .forEach(node => listeners.forEach(listener => node.addEventListener(...listener)));
          });
  }

  _onLinkClick(event) {
    event.preventDefault();
    const pageName = event.currentTarget.getAttribute("name");
    this.redirectToPage(pageName);
  }
}

export default Page;
