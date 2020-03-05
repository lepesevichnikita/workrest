export class TemplateHelper {
  constructor() {
  }

  getTemplatePath(templateName) {
    return `${TemplateHelper.TEMPLATES_PATH}/${templateName}.html`;
  }

  getPagePath(pageName) {
    return `${TemplateHelper.PAGES}/${pageName}.html`;
  }

}

TemplateHelper.TEMPLATES_PATH = "src/template";
TemplateHelper.PAGES = `${TemplateHelper.TEMPLATES_PATH}/page`;

export default TemplateHelper;