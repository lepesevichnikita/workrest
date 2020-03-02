import {Page} from "./Page.js";

export class User extends Page {
  process() {
    checkIsAuthorized()
    .then(() => {
      replacePage("user")
      .then(() => super.process());
    })
    .catch(() => {
      redirectToPage("login");
    });
  }
}

export default User;
