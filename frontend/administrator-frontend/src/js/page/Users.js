import { UserService } from "../api";
import { redirectToPage } from "../main.js";
import { Page } from "./Page.js";

export class Users extends Page {
  constructor(props) {
    super(props);
    this._authorizationService = props.authorizationService;
    this._userService = new UserService(props);
    this.addListener(".button[data-action=delete]", ["click", this._onDeleteClick.bind(this), false])
        .addListener(".button[data-action=block]", ["click", this._onBlockClick.bind(this), false])
        .addListener(".button[data-action=unblock]", ["click", this._onUnblockClick.bind(this), false])
        .addListener(".button[data-action=restore]", ["click", this._onRestoreClick.bind(this), false]);
  }

  process() {
    this.showDimmer();
    this._authorizationService.checkIsAuthorized()
        .then(() => this._loadData())
        .catch(() => redirectToPage("login"))
        .finally(() => this.hideDimmer());
  }

  _onRestoreClick(event) {
    event.preventDefault();
    this.showDimmer();
    const userId = event.currentTarget.getAttribute("data-id");
    this._userService.restoreUser(userId)
        .then(() => this._loadData())
        .finally(() => this.hideDimmer());
  }

  _onUnblockClick(event) {
    event.preventDefault();
    this.showDimmer();
    const userId = event.currentTarget.getAttribute("data-id");
    this._userService.unblockUser(userId)
        .then(() => this._loadData())
        .finally(() => this.hideDimmer());
  }

  _onDeleteClick(event) {
    event.preventDefault();
    this.showDimmer();
    const userId = event.currentTarget.getAttribute("data-id");
    this._userService.deleteUser(userId)
        .then(() => this._loadData())
        .finally(() => this.hideDimmer());
  }

  _onBlockClick(event) {
    event.preventDefault();
    this.showDimmer();
    const userId = event.currentTarget.getAttribute("data-id");
    this._userService.blockUser(userId)
        .then(() => this._loadData())
        .finally(() => this.hideDimmer());
  }

  _loadData() {
    this._userService.getUsers()
        .then(response => this._users = response.body)
        .then(() => console.dir(this._users))
        .then(() => this.replacePage("users", {users: this._users})
                        .catch(console.error))
        .finally(() => super.process());
  }
}

export default Users;
