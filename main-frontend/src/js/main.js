import {AuthorizationService} from './api';
import {TemplateHelper} from './helper';

let menuContainerId = '#menu';

const formToObject = form => {
  const formData = new FormData(form);
  const formDataAsObject = {};
  formData.forEach((value, key) => {
    formDataAsObject[key] = value;
  });
  return formDataAsObject;
};

const capitalizeFirstLetter = string => {
  return string.charAt(0).toUpperCase() + string.slice(1);
};

const loadMenu = () => {
  $(menuContainerId).dimmer('show');
  $.get(templateHelper.getTemplatePath('main/menu')).done(menuTemplate => {
    $(menuContainerId).dimmer('hide');
    $(menuContainerId).replaceWith($.tmpl(menuTemplate, {}));
    $('.ui.menu a.item').click(function(event) {
      event.preventDefault();
      const item = $(this);
      const pageName = item.attr('name');
      replacePage(templateHelper.getPagePath(pageName),
                  capitalizeFirstLetter(pageName));
    });
  });
};

export const checkIsAuthorized = (callbacks = {
  onUnauthorizedCallback: null,
  onAuthorizedCallback: null,
}) => {
  if (authorizationService.hasToken()) {
    authorizationService.verifyToken().then(response => {
      callbacks.onAuthorizedCallback && callbacks.onAuthorizedCallback();
    }).catch(error => {
      callbacks.onUnauthorizedCallback && callbacks.onUnauthorizedCallback();
    });
  } else {
    callbacks.onUnauthorizedCallback && callbacks.onUnauthorizedCallback();
  }
};

export const defineFormSubmitCallback = (form, submitCallback) => {
  $(form).submit(function(event) {
    event.preventDefault();
    $(form).dimmer('show');
    submitCallback(formToObject(form));
  });
};

export const redirectToPage = pageName => {
  replacePage(templateHelper.getPagePath(pageName),
              capitalizeFirstLetter(pageName));
};

export const loadScript = url => {
  return new Promise(function(resolve, reject) {
    $.get(url).done(response => {
      resolve();
    }).catch(err => reject(err));
  });
};

export const loadTemplate = (selector, link, templateData) => {
  $.get(link).done(pageTemplate => {
    const pageHtml = $.tmpl(pageTemplate, templateData);
    $(selector).html(pageHtml);
  });
};

export const replacePage = (link, pageName) => {
  document.title = `WorkRest | ${pageName}`;
  loadScript(`src/js/page/before/${pageName.toLowerCase()}.js`).
  then(() => loadTemplate('#page', link, {})).
  then(() => loadScript(`src/js/page/after/${pageName.toLowerCase()}.js`));
};

window.replacePage = replacePage;
window.cachedScript = replacePage;
window.authorizationService = new AuthorizationService();
window.templateHelper = new TemplateHelper();
window.redirectToPage = redirectToPage;
window.defineFormSubmitCallback = defineFormSubmitCallback;
window.checkIfAuthorized = checkIsAuthorized;

loadMenu();
redirectToPage('home');
