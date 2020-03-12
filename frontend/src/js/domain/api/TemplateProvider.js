import { TemplateHelper } from "/frontend/src/js/domain/helper/index.js";

export class TemplateProvider {
  constructor(props) {
    this._locator = props.locator;
  }

  get _templateHelper() {
    return this._locator.getServiceByClass(TemplateHelper);
  }

  async getTemplate(templateName) {
    const templatePath = this._getTemplatePath(templateName);
    let result = sessionStorage.getItem(templatePath);
    if (!(result && result.length !== 0)) {
      const response = await superagent.get(this._templateHelper.getTemplatePath(templateName));
      result = response.text;
      sessionStorage.setItem(templatePath, result);
    }
    return result;
  }

  _getTemplatePath(templateName) {
    return `${window.location.href}${this._templateHelper.getTemplatePath(templateName)}`;
  }
}

TemplateProvider.DEFAULT_PROPS = {};

export default TemplateProvider;