import { AuthorizationService, RestClient, TemplateProvider } from "/frontend/src/js/domain/api/index.js";
import { Action } from "/frontend/src/js/domain/constant/index.js";
import { TemplateHelper } from "/frontend/src/js/domain/helper/index.js";
import { Locator, Renderer } from "/frontend/src/js/domain/service/index.js";
import { AdministratorService } from "/frontend/system-administrator-frontend/src/js/api/index.js";
import { Administrators, Login } from "/frontend/system-administrator-frontend/src/js/page/index.js";

const MENU_CONTAINER_ID = "#menu";
AuthorizationService.TOKEN = "sa_token";

const locator = new Locator();
locator.registerServiceByClass(TemplateHelper, new TemplateHelper())
       .registerServiceByClass(RestClient, new RestClient())
       .registerServiceByClass(Renderer, new Renderer({locator}))
       .registerServiceByClass(AuthorizationService, new AuthorizationService({locator}))
       .registerServiceByClass(TemplateProvider, new TemplateProvider({locator}))
       .registerServiceByClass(AdministratorService, new AdministratorService({locator}))
       .registerServiceByName("login", new Login({locator}))
       .registerServiceByName("administrators", new Administrators({locator}));

const redirectToPage = (pageName) => locator.getServiceByName(pageName)
                                            .process();

const loadMenu = menuName => {
  const menuContainer = $(MENU_CONTAINER_ID);
  locator.getServiceByClass(Renderer)
         .buildTemplate(`menu/${menuName}`)
         .then(templateBody => {
           menuContainer.html(templateBody);
           const links = $(".ui.link");
           const signOutButton = $("#signout");
           links.unbind();
           links.click((event) => {
             event.preventDefault();
             const pageName = event.currentTarget.getAttribute("name");
             locator.getServiceByName(pageName)
                    .process();
           });
           signOutButton.click((event) => {
             event.preventDefault();
             locator.getServiceByClass(AuthorizationService)
                    .signOut();
           });
         });
};

locator.getServiceByClass(AuthorizationService)
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
       })
       .checkIsAuthorized()
       .then(() => loadMenu("authorized"))
       .catch(() => loadMenu("main"))
       .finally(() => redirectToPage("administrators"));

