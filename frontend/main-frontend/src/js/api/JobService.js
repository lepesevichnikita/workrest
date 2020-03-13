import { AuthorizationService, RestClient } from "/frontend/src/js/domain/api/index.js";
import { endpoint } from "/frontend/src/js/domain/config/index.js";

export class JobService {
  constructor(props) {
    this._locator = props.locator;
  }

  deleteJob(jobId) {
    return new Promise((resolve, reject) => {
      this._restClient.delete(`${endpoint.jobs.root}/${jobId}`)
          .secured(this._authorizationService.getToken().token)
          .then(resolve)
          .catch(reject);
    })
  }

  updateJob(jobId, jobData) {
    return new Promise((resolve, reject) => {
      this._restClient.put(`${endpoint.jobs.root}/${jobId}`)
          .secured(this._authorizationService.getToken().token)
          .send(jobData)
          .then(resolve)
          .catch(reject);
    })
  }

  createJob(jobData) {
    return new Promise((resolve, reject) => {
      this._restClient.post(endpoint.jobs.root)
          .secured(this._authorizationService.getToken().token)
          .send(jobData)
          .then(resolve)
          .catch(reject);
    })
  }

  getJobs() {
    return new Promise((resolve, reject) => {
      this._restClient.get(endpoint.jobs.all)
          .then(resolve)
          .catch(reject);
    })
  }

  async getJobById(jobId) {
    return this._restClient.get(endpoint.jobs.by_id(jobId))
               .secured(this._authorizationService.getToken().token);
  }

  finishJob(id) {
    return new Promise((resolve, reject) => {
      this._restClient.post(endpoint.jobs.finish_by_id(id))
          .secured(this._authorizationService.getToken().token)
          .then(resolve)
          .catch(reject);
    })
  }

  startJob(id) {
    return new Promise((resolve, reject) => {
      this._restClient.post(endpoint.jobs.start_by_id(id))
          .secured(this._authorizationService.getToken().token)
          .then(resolve)
          .catch(reject);
    })
  }

  get _authorizationService() {
    return this._locator.getServiceByClass(AuthorizationService);
  }

  get _restClient() {
    return this._locator.getServiceByClass(RestClient);
  }
}

export default JobService;
