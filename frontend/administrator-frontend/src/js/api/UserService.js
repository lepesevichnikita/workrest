import { AuthorizationService, RestClient } from "/frontend/src/js/domain/api/index.js";
import { endpoint } from "/frontend/src/js/domain/config/index.js";

export class UserService {
  constructor(props) {
    this._locator = props.locator;
  }

  getUsers() {
    return new Promise((resolve, reject) => this._restClient.get(endpoint.users.all)
                                                .secured(this._authorizationService.getToken().token)
                                                .then(resolve)
                                                .catch(reject));
  }

  deleteUser(userId) {
    return new Promise((resolve, reject) => this._restClient.delete(endpoint.users.by_id(userId))
                                                .secured(this._authorizationService.getToken().token)
                                                .then(resolve)
                                                .catch(reject));
  }

  blockUser(userId) {
    return new Promise((resolve, reject) => this._restClient.post(endpoint.users.block_by_id(userId))
                                                .secured(this._authorizationService.getToken().token)
                                                .then(resolve)
                                                .catch(reject));
  }

  unblockUser(userId) {
    return new Promise((resolve, reject) => this._restClient.post(endpoint.users.unblock_by_id(userId))
                                                .secured(this._authorizationService.getToken().token)
                                                .then(resolve)
                                                .catch(reject));
  }

  restoreUser(userId) {
    return new Promise((resolve, reject) => this._restClient.post(endpoint.users.unblock_by_id(userId))
                                                .secured(this._authorizationService.getToken().token)
                                                .then(resolve)
                                                .catch(reject));
  }

  get _authorizationService() {
    return this._locator.getServiceByClass(AuthorizationService);
  }

  get _restClient() {
    return this._locator.getServiceByClass(RestClient);
  }
}

export default UserService;
