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
}

export default Renderer;