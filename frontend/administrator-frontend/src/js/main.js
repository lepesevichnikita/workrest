import { AuthorizationService } from "./api";

import { Action } from "./constant";
import { TemplateHelper } from "./helper";
import { Login, PersonalData, Users } from "./page";

const menuContainerId = "#menu";
const templateHelper = new TemplateHelper();
const authorizationService = new AuthorizationService();

const pages = {
  login: new Login({authorizationService}),
  users: new Users({authorizationService}),
  personal_data: new PersonalData({authorizationService})
};

const loadMenu = menuName => {
  $.get(templateHelper.getTemplatePath(`menu/${menuName}`))
   .done(menuTemplate => {
     $(menuContainerId)
     .html($.tmpl(menuTemplate, {}));
     const links = $(".ui.link");
     links.click(function(event) {
       event.preventDefault();
       redirectToPage($(this)
                      .attr("name"));
     });
     links.unbind();
     $("#signout")
     .click(function (event) {
       event.preventDefault();
       authorizationService.signOut();
     });
   });
};

export const redirectToPage = pageName => {
  pages[pageName].process();
};

export const loadTemplate = (selector, link, templateData) => new Promise((resolve, reject) => {
  superagent.get(link)
            .then(response => {
              const pageHtml = $.tmpl(response.text, templateData);
              $(selector)
              .html(pageHtml);
              resolve(pageHtml);
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

window.limitContentText = limitContentText;
window.authorizationService = authorizationService;

authorizationService
.subscribe(Action.SIGNED_IN, () => {
  loadMenu("authorized");
  redirectToPage("users");
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
                    .finally(() => redirectToPage("users"));

