import { AuthorizationService, RestClient } from "/frontend/src/js/domain/api/index.js";
import { endpoint } from "/frontend/src/js/domain/config/index.js";
import { Action } from "/frontend/src/js/domain/constant/index.js";
import { Subscribable } from "/frontend/src/js/domain/model/index.js";

export class FileService extends Subscribable {
  constructor(props) {
    super();
    this._locator = props.locator;
  }

  get _authorizationService() {
    return this._locator.getServiceByClass(AuthorizationService);
  }

  get _restClient() {
    return this._locator.getServiceByClass(RestClient);
  }

  getFileUrl(id) {
    return RestClient.getActionUrl(endpoint.file.by_id(id));
  }

  uploadFile(file) {
    return new Promise((resolve, reject) => {
      this._restClient
          .post(endpoint.file.root)
          .secured(this._authorizationService.getToken().token)
          .attach("file", file)
          .on("progress", event => {
            this.notifyAllSubscribers(Action.LOADING_PROGRESS, event);
          })
          .then(resolve)
          .catch(reject);
    });
  }

  get _authorizationService() {
    return this._locator.getServiceByClass(AuthorizationService);
  }

  get _restClient() {
    return this._locator.getServiceByClass(RestClient);
  }
}

export default FileService;
