import {endpoint} from "../config";
import {RestClient} from "./RestClient.js";

export class JobService {
  constructor(props) {
    this._authorizationService = props.authorizationService;
    this._restClient = new RestClient();
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
}

export default JobService;
