import { endpoint } from "../config";
import { Action } from "../constant";
import { Subscribable } from "../model";
import { RestClient } from "./RestClient.js";

export class FileService extends Subscribable {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;

    this._restClient = new RestClient();
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
}

export default FileService;
