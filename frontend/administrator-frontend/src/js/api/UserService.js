import { endpoint } from "../config";
import { RestClient } from "./RestClient.js";

export class UserService {
  constructor(props) {
    this._authorizationService = props.authorizationService;
    this._restClient = new RestClient();
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
}

export default UserService;
