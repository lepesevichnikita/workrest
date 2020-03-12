import { PersonalDataService, UserService } from "/frontend/administrator-frontend/src/js/api/index.js";
import { Login, PersonalData, Users } from "/frontend/administrator-frontend/src/js/page/index.js";
import { AuthorizationService, RestClient, TemplateProvider } from "/frontend/src/js/domain/api/index.js";
import { TemplateHelper } from "/frontend/src/js/domain/helper/index.js";
import { Locator, Renderer } from "/frontend/src/js/domain/service/index.js";

const MENU_CONTAINER_ID = "#menu";

const locator = new Locator();
locator.registerServiceByClass(TemplateHelper, new TemplateHelper())
       .registerServiceByClass(RestClient, new RestClient())
       .registerServiceByClass(Renderer, new Renderer({locator}))
       .registerServiceByClass(AuthorizationService, new AuthorizationService({locator}))
       .registerServiceByClass(TemplateProvider, new TemplateProvider({locator}))
       .registerServiceByClass(UserService, new UserService({locator}))
       .registerServiceByClass(PersonalDataService, new PersonalDataService({locator}))
       .registerServiceByName("users", new Users({locator}))
       .registerServiceByName("login", new Login({locator}))
       .registerServiceByName("personal_data", new PersonalData({locator}));

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
             redirectToPage(pageName);
           });
           signOutButton.click((event) => {
             event.preventDefault();
             locator.getServiceByClass(AuthorizationService)
                    .signOut();
           });
         });
};

locator.getServiceByClass(AuthorizationService)
       .checkIsAuthorized()
       .then((authorized) => authorized ? loadMenu("authorized") : loadMenu("main"))
       .finally(() => locator.getServiceByName("login")
                             .process());

