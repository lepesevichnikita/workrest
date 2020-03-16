import { FileService, FreelancerService, JobService, PersonalDataService, UserService } from "/frontend/main-frontend/src/js/api/index.js";
import { Freelancers, Home, Jobs, Login, PersonalData, SignUp, User } from "/frontend/main-frontend/src/js/page/index.js";
import { AuthorizationService, RestClient, TemplateProvider } from "/frontend/src/js/domain/api/index.js";
import { Action } from "/frontend/src/js/domain/constant/index.js";
import { TemplateHelper } from "/frontend/src/js/domain/helper/index.js";
import { Locator, Renderer } from "/frontend/src/js/domain/service/index.js";

const MENU_CONTAINER_ID = "#menu";
AuthorizationService.TOKEN = 'm_token';

const locator = new Locator();

locator.registerServiceByClass(TemplateHelper, new TemplateHelper())
       .registerServiceByClass(RestClient, new RestClient())
       .registerServiceByClass(Renderer, new Renderer({locator}))
       .registerServiceByClass(AuthorizationService, new AuthorizationService({locator}))
       .registerServiceByClass(TemplateProvider, new TemplateProvider({locator}))
       .registerServiceByClass(FreelancerService, new FreelancerService({locator}))
       .registerServiceByClass(UserService, new UserService({locator}))
       .registerServiceByClass(JobService, new JobService({locator}))
       .registerServiceByClass(PersonalDataService, new PersonalDataService({locator}))
       .registerServiceByClass(FileService, new FileService({locator}))
       .registerServiceByName("home", new Home({locator}))
       .registerServiceByName("user", new User({locator}))
       .registerServiceByName("freelancers", new Freelancers({locator}))
       .registerServiceByName("jobs", new Jobs({locator}))
       .registerServiceByName("login", new Login({locator}))
       .registerServiceByName("signup", new SignUp({locator}))
       .registerServiceByName("personal_data", new PersonalData({locator}));

const capitalizeFirstLetter = string => {
  return string.charAt(0)
               .toUpperCase() + string.slice(1);
};

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
         redirectToPage("user");
       })
       .subscribe(Action.LOGGED_OUT, () => {
         loadMenu("main");
         redirectToPage("login");
       })
       .subscribe(Action.SIGNED_UP, () => redirectToPage("user"))
       .subscribe(Action.TOKEN_CORRECT, () => {
         loadMenu("authorized");
       })
       .subscribe(Action.TOKEN_INCORRECT, () => {
         locator.getServiceByClass(AuthorizationService)
                .signOut();
       });

locator.getServiceByClass(AuthorizationService)
       .checkIsAuthorized()
       .then((authorized) => authorized ? loadMenu("authorized") : loadMenu("main"))
       .catch(() => loadMenu("main"))
       .finally(() => locator.getServiceByName("home")
                             .process());

