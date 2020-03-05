import { TemplateHelper } from "../helper";
import { loadTemplate, redirectToPage } from "../main.js";
import { Component } from "./Component.js";

export class Page extends Component {
  constructor() {
    super();
    this._eventsListeners = {};
    this._templateHelper = new TemplateHelper();
    this.addListener(".ui.link", ["click", this._onLinkClick.bind(this), false]);
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

  replacePage(pageName, pageData = {}) {
    const pageSelector = "#page";
    this.showDimmer();
    return new Promise((resolve, reject) => {
      loadTemplate(pageSelector, this._templateHelper.getPagePath(pageName.toLowerCase()), pageData)
      .then(resolve)
      .catch(reject)
      .finally(() => this.hideDimmer());
    });
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

  process() {
    this._initializeListeners();
  }
}

export default Page;
