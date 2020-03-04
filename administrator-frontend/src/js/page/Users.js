import {Page} from "./Page.js";
import {UserService} from "../api";

export class Users extends Page {
  constructor(props) {
    super(props);
    this._userService = new UserService();
  }

  _loadData() {
    $("#user-cards")
    .dimmer("show");
    this._userService.getUsers()
        .then(response => {
          const users = response.body;
          $.get("src/template/user/card.html", cardBody => {
            const users = $("#user-cards")
            .dimmer("hide");
            $.tmpl(cardBody, users)
             .appendTo("#user-cards");
          });
        });
  }

  process() {
    checkIsAuthorized()
    .then(() => {
      replacePage("users")
      .then(() => super.process());
    })
    .catch(() => {
      redirectToPage("login");
    });
  }
}

export default Users;
