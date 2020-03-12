import { UserService } from "/frontend/administrator-frontend/src/js/api/index.js";
import { AuthorizationService } from "/frontend/src/js/domain/api/index.js";
import { Page } from "/frontend/src/js/domain/component/index.js";

export class Users extends Page {
  constructor(props) {
    super(props);
    this.addListener(".button[data-action=delete]", ["click", this._onDeleteClick.bind(this), false])
        .addListener(".button[data-action=block]", ["click", this._onBlockClick.bind(this), false])
        .addListener(".button[data-action=unblock]", ["click", this._onUnblockClick.bind(this), false])
        .addListener(".button[data-action=restore]", ["click", this._onRestoreClick.bind(this), false]);
  }

  get _authorizationService() {
    return this.locator.getServiceByClass(AuthorizationService);
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

  get _userService() {
    return this.locator.getServiceByClass(UserService);
  }

  process() {
    this.showDimmer();
    this._authorizationService.checkIsAuthorized()
        .then((authorized) => {
          if (authorized) {
            this._loadData();
          } else {
            this.redirectToPage("login");
          }
        })
        .catch(console.error);
  }

  _onDeleteClick(event) {
    event.preventDefault();
    this.showDimmer();
    const userId = event.currentTarget.getAttribute("data-id");
    this._userService.deleteUser(userId)
        .then(() => this.process());
  }

  _onBlockClick(event) {
    event.preventDefault();
    this.showDimmer();
    const userId = event.currentTarget.getAttribute("data-id");
    this._userService.blockUser(userId)
        .then(() => this.process());
  }

  _loadData() {
    this._userService.getUsers()
        .then((response) => this.replacePage("users", {users: response.body})
                                .finally(() => {
                                  super.process();
                                  this.hideDimmer();
                                }))
        .catch(console.error);
  }
}

export default Users;
