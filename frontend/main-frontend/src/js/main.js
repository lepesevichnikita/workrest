import { AuthorizationService, FreelancerService, JobService, UserService } from "./api";

import { Action } from "./constant";
import { TemplateHelper } from "./helper";
import { Freelancers, Home, Jobs, Login, PersonalData, SignUp, User } from "./page";

const menuContainerId = "#menu";
const authorizationService = new AuthorizationService();

const pages = {
  home: new Home(),
  user: new User({authorizationService}),
  freelancers: new Freelancers({authorizationService}),
  jobs: new Jobs({authorizationService}),
  login: new Login({authorizationService}),
  signup: new SignUp({authorizationService}),
  personal_data: new PersonalData({authorizationService})
};

const capitalizeFirstLetter = string => {
  return string.charAt(0)
               .toUpperCase() + string.slice(1);
};

const loadMenu = menuName => {
  $(menuContainerId)
  .dimmer("show");
  $.get(templateHelper.getTemplatePath(`menu/${menuName}`))
   .done(menuTemplate => {
     const menuContainer = $(menuContainerId);
     menuContainer.dimmer("hide");
     menuContainer.html($.tmpl(menuTemplate, {}));
     const links = $(".ui.link");
     const signOutButton = $("#signout");
     links.unbind();
     links.click(function(event) {
       event.preventDefault();
       redirectToPage($(this)
                      .attr("name"));
     });
     signOutButton.click(function(event) {
       event.preventDefault();
       authorizationService.signOut();
     });
   });
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
  .each(function (i) {
    const len = $(this)
    .text().length;
    if (len > maxTextLength) {
      $(this)
      .text($(this)
            .text()
            .substr(0, maxTextLength) + "..."
      );
    }
  });
};

window.authorizationService = authorizationService;
window.freelancerService = new FreelancerService(authorizationService);
window.templateHelper = new TemplateHelper(authorizationService);
window.userService = new UserService(authorizationService);
window.jobService = new JobService(authorizationService);
window.checkIsAuthorized = checkIsAuthorized;
window.limitContentText = limitContentText;

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
  redirectToPage("user");
})
.subscribe(Action.TOKEN_CORRECT, () => {
  loadMenu("authorized");
})
.subscribe(Action.TOKEN_INCORRECT, () => {
  authorizationService.signOut();
});

checkIsAuthorized()
.then(() => loadMenu("authorized"))
.catch(() => loadMenu("main"))
.finally(() => redirectToPage("home"));

