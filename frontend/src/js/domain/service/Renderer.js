import TemplateProvider from "../api/TemplateProvider.js";

export class Renderer {
  constructor(props) {
    this._locator = props.locator;
  }

  async buildTemplate(templateName, templateData) {
    const templateBody = await this._locator.getServiceByClass(TemplateProvider)
                                   .getTemplate(templateName);
    return $.tmpl(templateBody, templateData);
  }

  async renderModal(templateName, templateData) {
    document.querySelectorAll(".ui.modal,.ui.modals")
            .forEach(node => node.remove());
    const modal = document.createElement("div");
    modal.classList.add("ui");
    modal.classList.add("modal");
    $(modal)
    .html(await this.buildTemplate(templateName, templateData));
    $(modal)
    .modal({detachable: true})
    .modal("show");
  }

}

export default Renderer;