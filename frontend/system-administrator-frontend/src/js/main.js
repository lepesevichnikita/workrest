import { AuthorizationService } from "./api";

import { Action } from "./constant";
import { TemplateHelper } from "./helper";
import { Administrators, Login } from "./page";

const menuContainerId = "#menu";
const authorizationService = new AuthorizationService();

const pages = {
  login: new Login({authorizationService: authorizationService}),
  administrators: new Administrators({authorizationService: authorizationService})
};

const capitalizeFirstLetter = string => {
  return string.charAt(0)
               .toUpperCase() + string.slice(1);
};

const loadMenu = menuName => {
  $.get(templateHelper.getTemplatePath(`menu/${menuName}`))
   .done(menuTemplate => {
     $(menuContainerId)
     .html($.tmpl(menuTemplate, {}));
     $(".ui.link")
     .click(function(event) {
       event.preventDefault();
       redirectToPage($(this)
                      .attr("name"));
     });
     $("#signout")
     .click(function(event) {
       event.preventDefault();
       authorizationService.signOut();
     });
   });
};

export const redirectToPage = pageName => {
  pages[pageName].process();
};

export const loadTemplate = (selector, link, templateData) => new Promise((resolve, reject) => {
  superagent
  .get(link)
  .then(response => {
    const pageHtml = $.tmpl(response.text, templateData);
    $(selector)
    .html(pageHtml);
    resolve(response);
  })
  .catch(reject);
});

export const limitContentText = (contentSelector, maxTextLength) => {
  $(contentSelector)
  .each(function(i) {
    const len = $(this)
    .text().length;
    if (len > maxTextLength) {
      $(this)
      .text($(this)
            .text()
            .substr(0, maxTextLength) + "...");
    }
  });
};

window.authorizationService = authorizationService;
window.templateHelper = new TemplateHelper(authorizationService);
window.limitContentText = limitContentText;

authorizationService
.subscribe(Action.SIGNED_IN, () => {
  loadMenu("authorized");
  redirectToPage("administrators");
})
.subscribe(Action.LOGGED_OUT, () => {
  loadMenu("main");
  redirectToPage("login");
})
.subscribe(Action.TOKEN_CORRECT, () => {
  loadMenu("authorized");
})
.subscribe(Action.TOKEN_INCORRECT, () => {
  authorizationService.signOut();
});

authorizationService.checkIsAuthorized()
                    .then(() => loadMenu("authorized"))
                    .catch(() => loadMenu("main"))
                    .finally(() => redirectToPage("administrators"));

