import { AuthorizationService } from "./api";

import { Action } from "./constant";
import { TemplateHelper } from "./helper";
import { Home, Login, Requests, Users } from "./page";

const menuContainerId = "#menu";

const pages = {
  home: new Home(),
  requests: new Requests(),
  login: new Login(),
  users: new Users()
};

const formToObject = form => {
  const formData = new FormData(form);
  const formDataAsObject = {};
  formData.forEach((value, key) => {
    formDataAsObject[key] = value;
  });
  return formDataAsObject;
};

const capitalizeFirstLetter = string => {
  return string.charAt(0)
               .toUpperCase() + string.slice(1);
};

const loadMenu = menuName => {
  $(menuContainerId)
  .dimmer("show");
  $.get(templateHelper.getTemplatePath(`menu/${menuName}`))
   .done(
       menuTemplate => {
         $(menuContainerId)
         .dimmer("hide");
         $(menuContainerId)
         .html($.tmpl(menuTemplate, {}));
         $("#signout")
         .click(function (event) {
           event.preventDefault();
           authorizationService.signOut();
         });
       }
   );
};

export const checkIsAuthorized = () => {
  return new Promise((resolve, reject) => {
    if (authorizationService.hasToken()) {
      authorizationService
      .verifyToken()
      .then(response => {
        resolve();
      })
      .catch(reject);
    } else {
      reject();
    }
  });
};

export const defineFormSubmitCallback = (form, submitCallback) => {
  $(form)
  .submit(function (event) {
    event.preventDefault();
    $(form)
    .dimmer("show");
    submitCallback(formToObject(form));
  });
};

export const redirectToPage = pageName => {
  pages[pageName].process();
};

export const loadTemplate = (selector, link, templateData) =>
    new Promise((resolve, reject) => {
      $.get(link)
       .done(pageTemplate => {
         const pageHtml = $.tmpl(pageTemplate, templateData);
         $(selector)
         .html(pageHtml);
         resolve(pageHtml);
       })
       .fail(reject);
    });

export const replacePage = pageName =>
    new Promise((resolve, reject) => {
      document.title = `WorkRest | ${capitalizeFirstLetter(pageName)}`;
      loadTemplate(
          "#page",
          templateHelper.getPagePath(pageName.toLowerCase()),
          {}
      )
      .then(resolve)
      .catch(reject);
    });

export const limitContentText = (contentSelector, maxTextLength) => {
  $(contentSelector)
  .each(function (i) {
    const len = $(this)
    .text().length;
    if (len > maxTextLength) {
      $(this)
      .text(
          $(this)
          .text()
          .substr(0, maxTextLength) + "..."
      );
    }
  });
};

window.replacePage = replacePage;
window.authorizationService = new AuthorizationService();
window.redirectToPage = redirectToPage;
window.checkIsAuthorized = checkIsAuthorized;
window.limitContentText = limitContentText;
window.templateHelper = new TemplateHelper();

authorizationService
.subscribe(Action.SIGNED_IN, () => {
  loadMenu("authorized");
  redirectToPage("user");
})
.subscribe(Action.LOGGED_OUT, () => {
  loadMenu("main");
  redirectToPage("login");
})
.subscribe(Action.SIGNED_UP, () => {
  redirectToPage("home");
})
.subscribe(Action.TOKEN_CORRECT, () => {
  loadMenu("home");
})
.subscribe(Action.TOKEN_INCORRECT, () => {
  authorizationService.signOut();
});

checkIsAuthorized()
.then(() => loadMenu("authorized"))
.catch(() => loadMenu("main"))
.finally(() => redirectToPage("login"));
