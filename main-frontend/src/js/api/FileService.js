import {AuthorizationService} from './AuthorizationService.js';
import {RestClient} from './RestClient.js';
import {Subscribable} from '../model';
import {Action} from '../constant';

export class FileService extends Subscribable {
  constructor() {
    super();
    this._authorizationService = new AuthorizationService();
    this._restClient = new RestClient();
  }

  uploadFile(file) {
    return new Promise((resolve, reject) => {
      this._restClient.post('file').
           set(RestClient.AUTHORIZATION,
               this._authorizationService.getToken().token).
           attach('file', file).
           on('progress', event => {
             this.notifyAllSubscribers(Action.LOADING_PROGRESS, event);
           }).
           then(resolve).
           catch(reject);
    });
  }
}

export default FileService;
