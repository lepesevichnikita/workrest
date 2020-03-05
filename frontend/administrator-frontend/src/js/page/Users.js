import { UserService } from "../api";
import { redirectToPage } from "../main.js";
import { Page } from "./Page.js";

export class Users extends Page {
  constructor(props) {
    super(props);
    this._authorizationService = props.authorizationService;
    this._userService = new UserService(props);
    this.addListener(".button[data-action=deleteUser]", ["click", this._onDeleteClick.bind(this), false])
        .addListener(".button[data-action=blockUser]", ["click", this._onBlockClick.bind(this), false])
        .addListener(".button[data-action=unblockUser]", ["click", this._onUnblockClick.bind(this), false]);
  }

  process() {
    this._authorizationService.checkIsAuthorized()
        .then(() => {
          this._loadData();
        })
        .catch(() => {
          redirectToPage("login");
        });
  }

  _onUnblockClick(event) {
    event.preventDefault();
    const userId = event.currentTarget.getAttribute("data-id");
    this._userService.unblockUser(userId);
  }

  _onDeleteClick(event) {
    event.preventDefault();
    const userId = event.currentTarget.getAttribute("data-id");
    this._userService.deleteUser(userId);
  }

  _onBlockClick(event) {
    event.preventDefault();
    this.showDimmer();
    const userId = event.currentTarget.getAttribute("data-id");
    this._userService.blockUser(userId).;
  }

  _loadData() {
    this._userService.getUsers()
        .then(response => this.replacePage("users", {users: response.body})
                              .then(() => super.process()))
        .catch(console.error);
  }
}

export default Users;
