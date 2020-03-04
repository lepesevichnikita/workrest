import Component from "./Component.js";

export class Page extends Component {
  constructor() {
    super();
    this._eventsListeners = {};
    this.addListener('.ui.link', ['click', this._onLinkClick.bind(this), false]);
  }

  addListener(selector, eventListener) {
    const listeners = this._eventsListeners[selector] || [];
    this._eventsListeners[selector] = [...listeners, eventListener];
    return this;
  }

  _initializeListeners() {
    Object.keys(this._eventsListeners)
          .forEach(selector => {
            const listeners = this._eventsListeners[selector] || [];
            document.querySelectorAll(selector)
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
